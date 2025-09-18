package com.esm.dynamicpricing.service;

import com.esm.dynamicpricing.dto.LoginRequestDto;
import com.esm.dynamicpricing.dto.UserDto;
import com.esm.dynamicpricing.dto.UserResponseDto;

public interface UserService {
    UserResponseDto registerUser(UserDto userDto);
UserResponseDto loginUser(LoginRequestDto loginRequestDto);
}