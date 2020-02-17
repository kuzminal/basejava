package com.kuzmin.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.exception.StorageException;
import com.kuzmin.model.*;
import com.kuzmin.sql.SqlHelper;
import com.kuzmin.util.JSONParser;

import java.sql.*;
import java.util.*;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String url, String dbUser, String dbPassword) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");    //загружаем драйвер
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(url, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.executeStatement("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume resume) {
        sqlHelper.executeTransactional(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name=? WHERE uuid=?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            removeContacts(resume, connection);
            removeSections(resume, connection);
            addContacts(resume, connection);
            addSections(resume, connection);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.executeTransactional(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            addContacts(resume, connection);
            addSections(resume, connection);
            return null;
        });

    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeTransactional(connection -> {
            Resume resume;
            try (PreparedStatement ps = connection.prepareStatement("SELECT *  FROM resume r " +
                    " WHERE r.uuid =? ")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT *  FROM contact c " +
                    " WHERE c.resume_uuid =? ")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String value = rs.getString("value");
                    ContactType type = ContactType.valueOf(rs.getString("type"));
                    resume.addContact(type, value);
                }
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT *  FROM section s " +
                    " WHERE s.resume_uuid =? ")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String content = rs.getString("content");
                    makeSection(resume, rs, content);
                }
            } catch (JsonProcessingException e) {
                throw new StorageException(e);
            }
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.executeStatement("DELETE FROM resume WHERE uuid=?", stmt -> {
            stmt.setString(1, uuid);
            if (stmt.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        Map<String, Resume> resumes = new LinkedHashMap<>();
        return sqlHelper.executeTransactional(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume r " +
                    " ORDER BY uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                    resumes.put(resume.getUuid(), resume);
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM contact c ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid");
                    Resume resume = resumes.get(uuid);
                    resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT *  FROM section s")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String content = rs.getString("content");
                    String uuid = rs.getString("resume_uuid");
                    Resume resume = resumes.get(uuid);
                    makeSection(resume, rs, content);
                }
            } catch (JsonProcessingException e) {
                throw new StorageException(e);
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int size() {
        return sqlHelper.executeStatement("SELECT count(uuid) FROM resume", stmt -> {
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Table is empty", "");
            }
            return rs.getInt("count");
        });
    }

    private void removeContacts(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void removeSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid=?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void addContacts(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSections(Resume resume, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, content) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, JSONParser.write(e.getValue()));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (JsonProcessingException e) {
            throw new StorageException(e);
        }
    }

    private void makeSection(Resume resume, ResultSet rs, String content) throws SQLException, JsonProcessingException {
        SectionType sectionType = SectionType.valueOf(rs.getString("type"));
        if (content != null) {
            switch (sectionType) {
                case PERSONAL:
                case OBJECTIVE: {
                    TextSection textSection = JSONParser.read(content, TextSection.class);
                    resume.addSection(sectionType, textSection);
                    break;
                }
                case ACHIEVEMENT:
                case QUALIFICATIONS: {
                    TextListSection textSection = JSONParser.read(content, TextListSection.class);
                    resume.addSection(sectionType, textSection);
                    break;
                }
                case EXPERIENCE:
                case EDUCATION: {
                    break;
                }
            }
        }
    }
}
