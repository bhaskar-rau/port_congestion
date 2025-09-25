package com.esm.dynamicpricing.repository.impl;



import com.esm.dynamicpricing.repository.CamPortRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CamPortRepositoryImpl implements CamPortRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> findAllPortCodes() {
        String sql = "SELECT pk_port_code FROM cam_port";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}


