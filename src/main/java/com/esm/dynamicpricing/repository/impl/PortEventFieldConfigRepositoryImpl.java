package com.esm.dynamicpricing.repository.impl;

import com.esm.dynamicpricing.model.FieldType;
import com.esm.dynamicpricing.model.PortEventFieldConfig;
import com.esm.dynamicpricing.repository.PortEventFieldConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PortEventFieldConfigRepositoryImpl implements PortEventFieldConfigRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<PortEventFieldConfig> rowMapper = (ResultSet rs, int rowNum) -> {
        PortEventFieldConfig config = new PortEventFieldConfig();
        config.setId(rs.getInt("id"));
        config.setEventType(rs.getString("event_type"));
        config.setFieldName(rs.getString("field_name"));
        config.setFieldType(FieldType.valueOf(rs.getString("field_type").toUpperCase()));
        config.setIsRequired(rs.getBoolean("is_required"));
        config.setDisplayOrder(rs.getInt("display_order"));
        config.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class));
        return config;
    };

    @Override
    public PortEventFieldConfig save(PortEventFieldConfig config) {
        if (config.getId() == null) {
            return insert(config);
        } else {
            return update(config);
        }
    }

    private PortEventFieldConfig insert(PortEventFieldConfig config) {
        String sql = """
            INSERT INTO portevent_field_config
                (event_type, field_name, field_type, is_required, display_order)
            VALUES
                (:eventType, :fieldName, :fieldType, :isRequired, :displayOrder)
            RETURNING *
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("eventType", config.getEventType())
                .addValue("fieldName", config.getFieldName())
                .addValue("fieldType", config.getFieldType().name())
                .addValue("isRequired", config.getIsRequired())
                .addValue("displayOrder", config.getDisplayOrder());

        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    private PortEventFieldConfig update(PortEventFieldConfig config) {
        String sql = """
            UPDATE portevent_field_config SET
                event_type = :eventType,
                field_name = :fieldName,
                field_type = :fieldType,
                is_required = :isRequired,
                display_order = :displayOrder
            WHERE id = :id
            RETURNING *
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", config.getId())
                .addValue("eventType", config.getEventType())
                .addValue("fieldName", config.getFieldName())
                .addValue("fieldType", config.getFieldType().name())
                .addValue("isRequired", config.getIsRequired())
                .addValue("displayOrder", config.getDisplayOrder());

        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    @Override
    public List<PortEventFieldConfig> findByEventTypeOrderByDisplayOrder(String eventType) {
        String sql = """
            SELECT * FROM portevent_field_config
            WHERE event_type = :eventType
            ORDER BY display_order ASC
            """;

        return jdbcTemplate.query(sql, new MapSqlParameterSource("eventType", eventType), rowMapper);
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM portevent_field_config WHERE id = :id";
        int rowsAffected = jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
        log.info("Deleted {} rows for PortEventFieldConfig with id {}", rowsAffected, id);
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM portevent_field_config WHERE id = :id LIMIT 1";
        try {
            jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id), Integer.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }
}
