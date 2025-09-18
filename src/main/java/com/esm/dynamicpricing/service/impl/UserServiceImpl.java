package com.esm.dynamicpricing.service.impl;

import com.esm.dynamicpricing.dto.LoginRequestDto;
import com.esm.dynamicpricing.dto.UserDto;
import com.esm.dynamicpricing.dto.UserResponseDto;
import com.esm.dynamicpricing.exception.DuplicateUserException;
import com.esm.dynamicpricing.exception.InvalidCredentialsException;
import com.esm.dynamicpricing.exception.InvalidInputException;
import com.esm.dynamicpricing.exception.UserNotFoundException;
import com.esm.dynamicpricing.model.UserModel;
import com.esm.dynamicpricing.repository.UserRepository;
import com.esm.dynamicpricing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto registerUser(UserDto userDto) {

        if (userDto.getPswd() == null || userDto.getPswd().length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters long");
        }
        if (userDto.getEmailId() == null || !userDto.getEmailId().contains("@")) {
            throw new InvalidInputException("Email must be a valid address");
        }

        UserModel existing = userRepository.findByLoginId(userDto.getLoginId());
        if (existing != null) {
            throw new DuplicateUserException("User with loginId " + userDto.getLoginId() + " already exists");
        }

        UserModel user = new UserModel();
        user.setLoginId(userDto.getLoginId());
        user.setUserName(userDto.getUserName());
        user.setPswd(BCrypt.hashpw(userDto.getPswd(), BCrypt.gensalt()));
        user.setLocation(userDto.getLocation());
        user.setEmailId(userDto.getEmailId());

        userRepository.save(user);

        return new UserResponseDto(
                user.getLoginId(),
                user.getUserName(),
                user.getEmailId(),
                user.getLocation(),
                "User registered successfully"
        );
    }

    @Override
    public UserResponseDto loginUser(LoginRequestDto loginRequestDto) {
        UserModel user = userRepository.findByLoginId(loginRequestDto.getLoginId());
        if (user == null) {
            throw new UserNotFoundException("User not found with loginId: " + loginRequestDto.getLoginId());
        }

        if (!BCrypt.checkpw(loginRequestDto.getPswd(), user.getPswd())) {
            throw new InvalidCredentialsException("Password is incorrect");
        }

        return new UserResponseDto(
                user.getLoginId(),
                user.getUserName(),
                user.getEmailId(),
                user.getLocation(),
                "Login successful"
        );
    }
}
