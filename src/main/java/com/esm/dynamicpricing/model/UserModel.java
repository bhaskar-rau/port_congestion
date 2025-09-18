package com.esm.dynamicpricing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserModel {
    private String loginId;
    private String userName;
    @JsonIgnore
    private String pswd;
    private String location;
    private String emailId;
}
