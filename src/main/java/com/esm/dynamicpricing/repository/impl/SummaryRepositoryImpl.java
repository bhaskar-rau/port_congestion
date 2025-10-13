package com.esm.dynamicpricing.repository.impl;

import com.esm.dynamicpricing.model.PortCongestionData;
import com.esm.dynamicpricing.model.SituationType;
import com.esm.dynamicpricing.repository.SummaryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class SummaryRepositoryImpl implements SummaryRepository {

    private final JdbcTemplate jdbcTemplate;

    public SummaryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PortCongestionData> rowMapper = new RowMapper<>() {
        @Override
        public PortCongestionData mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PortCongestionData.builder()
                    .id(rs.getInt("id"))
                    .port(rs.getString("port"))
                    .terminal(rs.getString("terminal"))
                    .situationType(SituationType.valueOf(rs.getString("situation_type")))
                    .startEvent(rs.getObject("start_event", java.time.OffsetDateTime.class))
                    .endEvent(rs.getObject("end_event", java.time.OffsetDateTime.class))
                    .startEffected(rs.getObject("start_effected", java.time.OffsetDateTime.class))
                    .endEffected(rs.getObject("end_effected", java.time.OffsetDateTime.class))
                    .maxCrane(rs.getObject("max_crane", Integer.class))
                    .operatingCrane(rs.getObject("operating_crane", Integer.class))
                    .maxTruckVessel(rs.getObject("max_truck_vessel", Integer.class))
                    .operatingTruckVessel(rs.getObject("operating_truck_vessel", Integer.class))
                    .maxBerth(rs.getObject("max_berth", Integer.class))
                    .availableBerth(rs.getObject("available_berth", Integer.class))
                    .stevedoreShortagePercent(rs.getBigDecimal("stevedore_shortage_percent"))
                    .normalFormalityHrs(rs.getBigDecimal("normal_formality_hrs"))
                    .abnormalFormalityHrs(rs.getBigDecimal("abnormal_formality_hrs"))
                    .createdAt(rs.getObject("created_at", java.time.OffsetDateTime.class))
                    .updatedAt(rs.getObject("updated_at", java.time.OffsetDateTime.class))
                    .build();
        }
    };

    @Override
    public List<PortCongestionData> findAll(String port, String terminal,
                                            LocalDate minDate, LocalDate maxDate,
                                            int offset, int limit) {
        StringBuilder sql = new StringBuilder("SELECT * FROM port_congestion_data WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (port != null) {
            sql.append(" AND port = ?");
            params.add(port);
        }
        if (terminal != null) {
            sql.append(" AND terminal = ?");
            params.add(terminal);
        }
        if (minDate != null) {
            sql.append(" AND created_at >= ?");
            params.add(minDate.atStartOfDay());
        }
        if (maxDate != null) {
            sql.append(" AND created_at <= ?");
            params.add(maxDate.plusDays(1).atStartOfDay());
        }

        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), rowMapper, params.toArray());
    }

    @Override
    public int count(String port, String terminal, LocalDate minDate, LocalDate maxDate) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM port_congestion_data WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (port != null) {
            sql.append(" AND port = ?");
            params.add(port);
        }
        if (terminal != null) {
            sql.append(" AND terminal = ?");
            params.add(terminal);
        }
        if (minDate != null) {
            sql.append(" AND created_at >= ?");
            params.add(minDate.atStartOfDay());
        }
        if (maxDate != null) {
            sql.append(" AND created_at <= ?");
            params.add(maxDate.plusDays(1).atStartOfDay());
        }

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
    }

    @Override
    public Optional<PortCongestionData> findById(Integer id) {
        String sql = "SELECT * FROM port_congestion_data WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    @Override
    public int updateRecord(Integer id, PortCongestionData record) {
        String sql = """
                UPDATE port_congestion_data
                SET end_event = ?, end_effected = ?, updated_at = NOW()
                WHERE id = ?
                """;
        return jdbcTemplate.update(sql, record.getEndEvent(), record.getEndEffected(), id);
    }

    @Override
    public int deleteById(Integer id) {
        String sql = "DELETE FROM port_congestion_data WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
