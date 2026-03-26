package com.alibaba.smart.framework.engine.persister.database.handler;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * MyBatis TypeHandler for ID fields.
 * Automatically converts Serializable ID to the correct type based on IdGenerator configuration.
 *
 * @author SmartEngine Team
 */
public class SmartIdTypeHandler extends BaseTypeHandler<Serializable> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
            Serializable parameter, JdbcType jdbcType) throws SQLException {
        if (IdGenerator.getGlobalIdType() == Long.class) {
            ps.setLong(i, toLong(parameter));
        } else {
            ps.setString(i, parameter.toString());
        }
    }

    @Override
    public Serializable getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (IdGenerator.getGlobalIdType() == Long.class) {
            long value = rs.getLong(columnName);
            return rs.wasNull() ? null : value;
        }
        return rs.getString(columnName);
    }

    @Override
    public Serializable getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (IdGenerator.getGlobalIdType() == Long.class) {
            long value = rs.getLong(columnIndex);
            return rs.wasNull() ? null : value;
        }
        return rs.getString(columnIndex);
    }

    @Override
    public Serializable getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (IdGenerator.getGlobalIdType() == Long.class) {
            long value = cs.getLong(columnIndex);
            return cs.wasNull() ? null : value;
        }
        return cs.getString(columnIndex);
    }

    private Long toLong(Serializable value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.valueOf(value.toString());
    }
}
