package com.esm.dynamicpricing.repository.impl;




import com.esm.dynamicpricing.repository.CamTerminalDepotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CamTerminalDepotRepositoryImpl implements CamTerminalDepotRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<String> findTerminalsByPortCode(String portCode) {
        String sql = "SELECT pk_terminal FROM cam_terminal_depot WHERE fk_port_code = ?";
        return jdbcTemplate.queryForList(sql, String.class, portCode);
    }
}


