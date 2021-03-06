package com.mega.core.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper<T> {
    public T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
