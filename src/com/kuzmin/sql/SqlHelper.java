package com.kuzmin.sql;

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
