package com.kuzmin.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) throws SQLException {
        this.connectionFactory = connectionFactory;
    }

    public void executeStatement(String sql, PreparedStatement preparedStatement) {
        executeStatement(sql, preparedStatement);
    }

    public <T> T executeStatement(String statement, StatementExecutor<T> statementExecutor) throws SQLException {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(statement)) {
            return statementExecutor.execute(stmt);
        }
    }
}
