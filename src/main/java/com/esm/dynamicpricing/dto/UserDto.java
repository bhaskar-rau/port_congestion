package com.esm.dynamicpricing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "Login ID cannot be blank")
    @Size(max = 10, message = "Login ID must be at most 10 characters")
    private String loginId;

    @NotBlank(message = "Username cannot be blank")
    @Size(max = 20, message = "Username must be at most 20 characters")
    private String userName;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 30, message = "Password must be between 6 and 30 characters")
    private String pswd;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String emailId;
}
