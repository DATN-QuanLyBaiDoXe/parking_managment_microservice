package com.tth.id.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.id.auth.CustomUserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class AuthorizationResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String uuid;
    private String username;
    private String address;
    private String avatar;
    private Date birthday;
    private String email;
    private String fullName;
    private Integer gender;
    private String phoneNumber;
    private Integer role;


    public AuthorizationResponseDTO(CustomUserDetails userDetails, String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.uuid = userDetails.getUser().getUuid();
        this.username = userDetails.getUser().getUsername();
        this.address = userDetails.getUser().getAddress();
        this.avatar = userDetails.getUser().getAvatar();
        this.birthday = userDetails.getUser().getBirthday();
        this.email = userDetails.getUser().getEmail();
        this.fullName = userDetails.getUser().getFullName();
        this.gender = userDetails.getUser().getGender();
        this.phoneNumber = userDetails.getUser().getPhoneNumber();
        this.role = userDetails.getUser().getRole();
    }

}
