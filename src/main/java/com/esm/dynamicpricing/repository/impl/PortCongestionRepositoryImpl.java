package com.esm.dynamicpricing.repository.impl;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;
import com.esm.dynamicpricing.repository.PortCongestionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PortCongestionRepositoryImpl implements PortCongestionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RowMapper<PortCongestionData> rowMapper = (rs, rowNum) -> {
        try {
            return PortCongestionData.builder()
                    .id(rs.getInt("id"))
                    .port(rs.getString("port"))
                    .terminal(rs.getString("terminal"))
                    .situationType(SituationType.valueOf(rs.getString("situation_type")))
                    .details(objectMapper.readValue(
                            rs.getString("details"),
                            new TypeReference<Map<String, Object>>() {}
                    ))
                    .createdAt(rs.getObject("created_at", OffsetDateTime.class))
                    .updatedAt(rs.getObject("updated_at", OffsetDateTime.class))
                    .build();
        } catch (Exception e) {
            log.error("Error mapping PortCongestionData row: {}", e.getMessage(), e);
            throw new RuntimeException("Error mapping PortCongestionData", e);
        }
    };

    @Override
    public PortCongestionData save(PortCongestionData data) {
        try {
            if (data.getId() == null) {
                return insert(data);
            } else {
                return update(data);
            }
        } catch (Exception e) {
            log.error("Error saving PortCongestionData: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving port congestion data", e);
        }
    }

    private PortCongestionData insert(PortCongestionData data) throws Exception {
        String sql = """
            INSERT INTO port_congestion_data (
                port, terminal, situation_type, details, created_at, updated_at
            ) VALUES (
                :port, :terminal, :situationType, :details::jsonb, :createdAt, :updatedAt
            )
            RETURNING *
            """;

        OffsetDateTime now = OffsetDateTime.now();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("port", data.getPort())
                .addValue("terminal", data.getTerminal())
                .addValue("situationType", data.getSituationType().name())
                .addValue("details", objectMapper.writeValueAsString(data.getDetails()))
                .addValue("createdAt", now)
                .addValue("updatedAt", now);

        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    private PortCongestionData update(PortCongestionData data) throws Exception {
        String sql = """
            UPDATE port_congestion_data SET 
                port = :port,
                terminal = :terminal,
                situation_type = :situationType,
                details = :details::jsonb,
                updated_at = :updatedAt
            WHERE id = :id
            RETURNING *
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", data.getId())
                .addValue("port", data.getPort())
                .addValue("terminal", data.getTerminal())
                .addValue("situationType", data.getSituationType().name())
                .addValue("details", objectMapper.writeValueAsString(data.getDetails()))
                .addValue("updatedAt", OffsetDateTime.now());

        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    @Override
    public Optional<PortCongestionData> findById(Integer id) {
        String sql = "SELECT * FROM port_congestion_data WHERE id = :id";
        try {
            PortCongestionData data = jdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource("id", id),
                    rowMapper
            );
            return Optional.ofNullable(data);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PortCongestionData> findAll() {
        String sql = "SELECT * FROM port_congestion_data ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<PortCongestionData> findBySituationType(SituationType situationType) {
        String sql = "SELECT * FROM port_congestion_data WHERE situation_type = :situationType ORDER BY created_at DESC";
        return jdbcTemplate.query(
                sql,
                new MapSqlParameterSource("situationType", situationType.name()),
                rowMapper
        );
    }

    @Override
    public int countBySituationType(SituationType situationType) {
        String sql = "SELECT COUNT(*) FROM port_congestion_data WHERE situation_type = :situationType";
        return jdbcTemplate.queryForObject(
                sql,
                new MapSqlParameterSource("situationType", situationType.name()),
                Integer.class
        );
    }

    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM port_congestion_data WHERE id = :id";
        int rows = jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
        if (rows == 0) {
            log.warn("No record found to delete with id {}", id);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM port_congestion_data WHERE id = :id LIMIT 1";
        try {
            jdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource("id", id),
                    Integer.class
            );
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
public List<PortCongestionData> findBySituationTypeAndCreatedAtBetween(
        SituationType type, OffsetDateTime start, OffsetDateTime end) {

    String sql = """
        SELECT * FROM port_congestion_data
        WHERE situation_type = :situationType
        AND created_at BETWEEN :start AND :end
        ORDER BY created_at DESC
    """;

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("situationType", type.name())
            .addValue("start", start)
            .addValue("end", end);

    return jdbcTemplate.query(sql, params, rowMapper);
}

@Override
public List<PortCongestionData> findBySituationTypeAndEventDetail(
        SituationType type, String fieldName, String fieldValue) {

    String sql = """
        SELECT * FROM port_congestion_data
        WHERE situation_type = :situationType
        AND event_details ->> :fieldName = :fieldValue
        ORDER BY created_at DESC
    """;

    MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("situationType", type.name())
            .addValue("fieldName", fieldName)
            .addValue("fieldValue", fieldValue);

    return jdbcTemplate.query(sql, params, rowMapper);
}

@Override
public List<PortCongestionData> findBySituationTypeAndEventDetailsContaining(
        SituationType type, Map<String, Object> eventDetailFilters) {

    try {
        String jsonFilter = objectMapper.writeValueAsString(eventDetailFilters);

        String sql = """
            SELECT * FROM port_congestion_data
            WHERE situation_type = :situationType
            AND event_details @> :jsonFilter::jsonb
            ORDER BY created_at DESC
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("situationType", type.name())
                .addValue("jsonFilter", jsonFilter);

        return jdbcTemplate.query(sql, params, rowMapper);
    } catch (Exception e) {
        log.error("Error querying with event detail filters: {}", e.getMessage());
        throw new RuntimeException("Error querying event details", e);
    }
}

}
