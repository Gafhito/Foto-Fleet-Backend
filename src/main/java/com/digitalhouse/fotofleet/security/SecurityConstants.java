package com.digitalhouse.fotofleet.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SecurityConstants {
    // Tiempo de expiración del JWT en milisegundos (el token dura 3hs en este caso)
    public static final long JWT_EXPIRATION_TOKEN = 60000 * 180;
    public static final String JWT_SIGNATURE = new String(Base64.getDecoder().decode("RmlybWEgc3VwZXIgbWVnYSB1bHRyYSByZWNvbnRyYSByZS1hcmNoaSBzZWNyZXRh"), StandardCharsets.UTF_8);
}
