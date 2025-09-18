package com.esm.dynamicpricing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String loginId;
    private String userName;
    private String emailId;
    private String location;
    private String message; 
}
