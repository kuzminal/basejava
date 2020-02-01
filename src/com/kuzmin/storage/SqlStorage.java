package com.kuzmin.storage;

import com.kuzmin.exception.NotExistStorageException;
import com.kuzmin.exception.StorageException;
import com.kuzmin.model.ContactType;
import com.kuzmin.model.Resume;
import com.kuzmin.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            return null;
        });
        changeContacts(resume, "UPDATE contact SET value=? " +
                "WHERE resume_uuid=? AND type=?");
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.executeTransactional(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            return null;
        });
        changeContacts(resume, "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)");
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
        MapStorage resumeList = sqlHelper.executeStatement("SELECT * FROM resume r " +
                " ORDER BY uuid", stmt -> {
            ResultSet rs = stmt.executeQuery();
            MapStorage resumes = new MapStorage();
            while (rs.next()) {
                Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                resumes.save(resume);
            }
            return resumes;
        });
        List<Resume> resumes = sqlHelper.executeStatement("SELECT * FROM contact c ",
                stmt -> {
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("resume_uuid");
                        Resume resume = resumeList.get(uuid);
                        resume.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                    }
                    return resumeList.getAllSorted();
                });
        return resumes;
    }

    @Override
    public int size() {
        return sqlHelper.executeStatement("SELECT count(uuid) FROM resume", stmt -> {
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Table is empty", "");
            }
            return Integer.parseInt(rs.getString("count"));
        });
    }

    private void changeContacts(Resume resume, String statement) {
        for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
            sqlHelper.executeTransactional(connection -> {
                try (PreparedStatement ps = connection.prepareStatement(statement)) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.execute();
                }
                return null;
            });
        }
    }
}
