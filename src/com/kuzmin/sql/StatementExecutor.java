package com.kuzmin.sql;

import java.sql.PreparedStatement;

public interface StatementExecutor<T> {
    T execute(PreparedStatement st);
}
