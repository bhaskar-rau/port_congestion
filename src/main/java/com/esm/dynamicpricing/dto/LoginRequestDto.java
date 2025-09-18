package com.esm.dynamicpricing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Login ID cannot be blank")
    private String loginId;

    @NotBlank(message = "Password cannot be blank")
    private String pswd;
}
