package com.kuzmin.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementExecutor<T> {
    T execute(PreparedStatement st) throws SQLException;
}
