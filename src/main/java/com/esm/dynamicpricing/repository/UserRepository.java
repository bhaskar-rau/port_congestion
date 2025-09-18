package com.esm.dynamicpricing.repository;

import com.esm.dynamicpricing.model.UserModel;

public interface UserRepository {
    void save(UserModel user);
    UserModel findByLoginId(String loginId);
}
