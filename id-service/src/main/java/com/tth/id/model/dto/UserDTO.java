package com.tth.id.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private String username;

    private String password;

    private String matchingPassword;

    private String fullName;

    private String phoneNumber;

    private String email;

    private Integer gender;

    private String birthday;

    private String address;

    private String avatar;

    private Integer role;
}
