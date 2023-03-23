package com.tth.id.model.dto;

import com.tth.id.auth.CustomUserDetails;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorizationResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String uuid;
    private String username;
    private String password;
    private Integer status;

    public AuthorizationResponseDTO(CustomUserDetails userDetails, String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.uuid = userDetails.getUser().getUuid();
        this.username = userDetails.getUser().getUsername();
        this.password = userDetails.getUser().getPassword();
        this.status = userDetails.getUser().getStatus();
    }
}
