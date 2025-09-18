package com.esm.dynamicpricing.repository.impl;

import com.esm.dynamicpricing.model.UserModel;
import com.esm.dynamicpricing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(UserModel user) {
        String sql = "INSERT INTO esm_admin_login (login_id, user_name, pswd, location, email_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getLoginId(), user.getUserName(), user.getPswd(), user.getLocation(), user.getEmailId());
    }

    @Override
    public UserModel findByLoginId(String loginId) {
        String sql = "SELECT * FROM esm_admin_login WHERE login_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserModel.class), loginId)
                .stream()
                .findFirst()
                .orElse(null);
    }
}
