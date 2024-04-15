package org.example.MineBatisUtils.TypeHandler.Impl;

import org.example.MineBatisUtils.TypeHandler.TypeHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author ziyuan
 * @since 2024.04
 */
public class LocalDateTimeHandler implements TypeHandler<LocalDateTime>{

    @Override
    public void setParameter(PreparedStatement ps, int i, LocalDateTime parameter) throws SQLException {
        if (parameter != null) {
            ps.setTimestamp(i, Timestamp.valueOf(parameter));
        } else {
            ps.setTimestamp(i, null);
        }
    }

    @Override
    public LocalDateTime getResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        if (timestamp != null) {
            return timestamp.toLocalDateTime();
        } else {
            return null;
        }
    }
}
