package com.digitalhouse.fotofleet.dtos;

import lombok.Data;

@Data
public class ResponseLoginDto {
    private String rol;
    private String tokenType;
    private String accessToken;

    public ResponseLoginDto(String rol, String accessToken) {
        this.rol = rol;
        this.tokenType = "Bearer ";
        this.accessToken = accessToken;
    }
}
