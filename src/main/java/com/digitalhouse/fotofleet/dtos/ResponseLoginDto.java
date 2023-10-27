package com.digitalhouse.fotofleet.dtos;

import lombok.Data;

@Data
public class ResponseLoginDto {
    private String tokenType;
    private String accessToken;

    public ResponseLoginDto(String accessToken) {
        this.tokenType = "Bearer ";
        this.accessToken = accessToken;
    }
}
