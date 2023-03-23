package com.tth.management.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class AuthorizationResponseDTO {
    private String accessToken;
    private String refreshToken;
    private String uuid;
    private String username;
    private String password;
    private Integer status;

    public AuthorizationResponseDTO(Map<String, Object> map){
        if (map != null && map.size() > 0) {
            if (map.containsKey("uuid")) {
                this.uuid = (String) map.get("uuid");
            }
            if (map.containsKey("username")) {
                this.username = (String) map.get("username");
            }
            if (map.containsKey("password")) {
                this.password = (String) map.get("password");
            }
            if (map.containsKey("status")) {
                this.status = (Integer) map.get("status");
            }
        }
    }
}