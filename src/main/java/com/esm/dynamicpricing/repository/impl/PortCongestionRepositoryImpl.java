package com.esm.dynamicpricing.repository.impl;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;
import com.esm.dynamicpricing.repository.PortCongestionRepository;
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

    private final RowMapper<PortCongestionData> rowMapper = (rs, rowNum) -> PortCongestionData.builder()
            .id(rs.getInt("id"))
            .port(rs.getString("port"))
            .terminal(rs.getString("terminal"))
            .situationType(SituationType.valueOf(rs.getString("situation_type")))
            .startEvent(rs.getObject("start_event", OffsetDateTime.class))
            .endEvent(rs.getObject("end_event", OffsetDateTime.class))
            .startEffected(rs.getObject("start_effected", OffsetDateTime.class))
            .endEffected(rs.getObject("end_effected", OffsetDateTime.class))
            .maxCrane((Integer) rs.getObject("max_crane"))
            .operatingCrane((Integer) rs.getObject("operating_crane"))
            .maxTruckVessel((Integer) rs.getObject("max_truck_vessel"))
            .operatingTruckVessel((Integer) rs.getObject("operating_truck_vessel"))
            .maxBerth((Integer) rs.getObject("max_berth"))
            .availableBerth((Integer) rs.getObject("available_berth"))
            .stevedoreShortagePercent(rs.getBigDecimal("stevedore_shortage_percent"))
            .normalFormalityHrs(rs.getBigDecimal("normal_formality_hrs"))
            .abnormalFormalityHrs(rs.getBigDecimal("abnormal_formality_hrs"))
            .createdAt(rs.getObject("created_at", OffsetDateTime.class))
            .updatedAt(rs.getObject("updated_at", OffsetDateTime.class))
            .build();

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

    private PortCongestionData insert(PortCongestionData data) {
        String sql = """
            INSERT INTO port_congestion_data (
                port, terminal, situation_type,
                start_event, end_event, start_effected, end_effected,
                max_crane, operating_crane,
                max_truck_vessel, operating_truck_vessel,
                max_berth, available_berth,
                stevedore_shortage_percent,
                normal_formality_hrs, abnormal_formality_hrs,
                created_at, updated_at
            ) VALUES (
                :port, :terminal, :situationType,
                :startEvent, :endEvent, :startEffected, :endEffected,
                :maxCrane, :operatingCrane,
                :maxTruckVessel, :operatingTruckVessel,
                :maxBerth, :availableBerth,
                :stevedoreShortagePercent,
                :normalFormalityHrs, :abnormalFormalityHrs,
                :createdAt, :updatedAt
            )
            RETURNING *
            """;

        OffsetDateTime now = OffsetDateTime.now();

        MapSqlParameterSource params = mapParams(data)
                .addValue("createdAt", now)
                .addValue("updatedAt", now);

        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    private PortCongestionData update(PortCongestionData data) {
        String sql = """
            UPDATE port_congestion_data SET 
                port = :port,
                terminal = :terminal,
                situation_type = :situationType,
                start_event = :startEvent,
                end_event = :endEvent,
                start_effected = :startEffected,
                end_effected = :endEffected,
                max_crane = :maxCrane,
                operating_crane = :operatingCrane,
                max_truck_vessel = :maxTruckVessel,
                operating_truck_vessel = :operatingTruckVessel,
                max_berth = :maxBerth,
                available_berth = :availableBerth,
                stevedore_shortage_percent = :stevedoreShortagePercent,
                normal_formality_hrs = :normalFormalityHrs,
                abnormal_formality_hrs = :abnormalFormalityHrs,
                updated_at = :updatedAt
            WHERE id = :id
            RETURNING *
            """;

        MapSqlParameterSource params = mapParams(data)
                .addValue("id", data.getId())
                .addValue("updatedAt", OffsetDateTime.now());

        return jdbcTemplate.queryForObject(sql, params, rowMapper);
    }

    private MapSqlParameterSource mapParams(PortCongestionData data) {
        return new MapSqlParameterSource()
                .addValue("port", data.getPort())
                .addValue("terminal", data.getTerminal())
                .addValue("situationType", data.getSituationType().name())
                .addValue("startEvent", data.getStartEvent())
                .addValue("endEvent", data.getEndEvent())
                .addValue("startEffected", data.getStartEffected())
                .addValue("endEffected", data.getEndEffected())
                .addValue("maxCrane", data.getMaxCrane())
                .addValue("operatingCrane", data.getOperatingCrane())
                .addValue("maxTruckVessel", data.getMaxTruckVessel())
                .addValue("operatingTruckVessel", data.getOperatingTruckVessel())
                .addValue("maxBerth", data.getMaxBerth())
                .addValue("availableBerth", data.getAvailableBerth())
                .addValue("stevedoreShortagePercent", data.getStevedoreShortagePercent())
                .addValue("normalFormalityHrs", data.getNormalFormalityHrs())
                .addValue("abnormalFormalityHrs", data.getAbnormalFormalityHrs());
    }

    @Override
    public Optional<PortCongestionData> findById(Integer id) {
        String sql = "SELECT * FROM port_congestion_data WHERE id = :id";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id), rowMapper)
            );
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
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
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
    public List<PortCongestionData> findBySituationTypeAndField(
            SituationType type, String fieldName, Object fieldValue) {

        String sql = String.format(
                "SELECT * FROM port_congestion_data WHERE situation_type = :situationType AND %s = :fieldValue ORDER BY created_at DESC",
                fieldName
        );

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("situationType", type.name())
                .addValue("fieldValue", fieldValue);

        return jdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public List<PortCongestionData> findBySituationTypeAndFields(
            SituationType type, Map<String, Object> filters) {

        StringBuilder sql = new StringBuilder("SELECT * FROM port_congestion_data WHERE situation_type = :situationType");
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("situationType", type.name());

        filters.forEach((key, value) -> {
            sql.append(" AND ").append(key).append(" = :").append(key);
            params.addValue(key, value);
        });

        sql.append(" ORDER BY created_at DESC");

        return jdbcTemplate.query(sql.toString(), params, rowMapper);
    }
}
