package com.mega.core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter<T> {
    public void setValues(PreparedStatement ps, T t) throws SQLException;
}
