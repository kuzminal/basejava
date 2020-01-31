package com.kuzmin.sql;

import com.kuzmin.exception.ExistStorageException;
import com.kuzmin.exception.StorageException;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) throws SQLException {
        this.connectionFactory = connectionFactory;
    }

    public <T> T executeStatement(String statement, StatementExecutor<T> statementExecutor) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(statement)) {
            return statementExecutor.execute(stmt);
        } catch (PSQLException pe) {
            if (pe.getSQLState().equals("23505")) {
                throw new ExistStorageException(pe.getMessage());
            } else {
                throw new StorageException(pe);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public <T> T executeTransactional(SqlTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw new StorageException(e);
            }
        } catch (PSQLException pe) {
            if (pe.getSQLState().equals("23505")) {
                throw new ExistStorageException(pe.getMessage());
            } else {
                throw new StorageException(pe);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
