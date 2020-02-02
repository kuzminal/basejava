package com.kuzmin.storage;

import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.exception.StorageException;
import com.kuzmin.model.ContactType;
import com.kuzmin.model.Resume;
import com.kuzmin.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String url, String dbUser, String dbPassword) throws SQLException {
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
            manageContacts(resume, "UPDATE contact SET value=? " +
                    "WHERE resume_uuid=? AND type=?", connection);
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
            manageContacts(resume, "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)", connection);
            return null;
        });

    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.executeStatement("SELECT * FROM resume r " +
                "  LEFT JOIN contact c " +
                "    ON r.uuid = c.resume_uuid " +
                " WHERE r.uuid =? ", stmt -> {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            Resume resume = new Resume(uuid, rs.getString("full_name"));
            do {
                String value = rs.getString("value");
                ContactType type = ContactType.valueOf(rs.getString("type"));
                if (value != null) {
                    resume.addContact(type, value);
                }
            } while (rs.next());
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
                    " ORDER BY uuid")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                    resumes.put(resume.getUuid(), resume);
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM contact c ")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid");
                    Resume resume = resumes.get(uuid);
                    resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
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

    private void manageContacts(Resume resume, String statement, Connection conn) {
        for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.execute();
            } catch (SQLException ex) {
                throw new StorageException(ex);
            }
        }
    }
}
