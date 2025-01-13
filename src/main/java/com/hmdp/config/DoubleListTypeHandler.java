package com.hmdp.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.util.List;

public class DoubleListTypeHandler extends BaseTypeHandler<List<Double>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(java.sql.PreparedStatement ps, int i, List<Double> parameter, JdbcType jdbcType) throws java.sql.SQLException {
        try {
            // 将 List<Double> 转换为 JSON 字符串
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (IOException e) {
            throw new java.sql.SQLException("Failed to convert List<Double> to JSON", e);
        }
    }

    @Override
    public List<Double> getNullableResult(java.sql.ResultSet rs, String columnName) throws java.sql.SQLException {
        try {
            String json = rs.getString(columnName);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<Double>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new java.sql.SQLException("Failed to convert JSON to List<Double>", e);
        }
    }

    @Override
    public List<Double> getNullableResult(java.sql.ResultSet rs, int columnIndex) throws java.sql.SQLException {
        try {
            String json = rs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<Double>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new java.sql.SQLException("Failed to convert JSON to List<Double>", e);
        }
    }

    @Override
    public List<Double> getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws java.sql.SQLException {
        try {
            String json = cs.getString(columnIndex);
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<Double>>() {});
            }
            return null;
        } catch (IOException e) {
            throw new java.sql.SQLException("Failed to convert JSON to List<Double>", e);
        }
    }
}
