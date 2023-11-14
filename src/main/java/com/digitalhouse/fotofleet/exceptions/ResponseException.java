package com.digitalhouse.fotofleet.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseException {
    private LocalDateTime timestamp;
    private Integer code;
    private String status;
    private String message;

    public ResponseException(Integer code, String status, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "\"timestamp\": \"" + timestamp + "\", " +
                "\"code\": \"" + code + "\", " +
                "\"status\": \"" + status + "\", " +
                "\"message\": \"" + message + "\"" +
                "}";
    }
}
