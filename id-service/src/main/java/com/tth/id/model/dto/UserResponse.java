package com.tth.id.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {

    private String uuid;

    private String username;

    private String fullName;

    private String phoneNumber;

    private String email;

    private Integer gender;

    private Date birthday;

    private String address;

    private String avatar;

    private Integer role;
}
