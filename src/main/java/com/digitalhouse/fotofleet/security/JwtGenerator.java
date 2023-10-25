package com.digitalhouse.fotofleet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtGenerator {
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date actualDate = new Date();
        Date expirationToken = new Date(actualDate.getTime() + SecurityConstants.JWT_EXPIRATION_TOKEN);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(actualDate)
                .setExpiration(expirationToken)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SIGNATURE)
                .compact();

        return token;
    }

    public String getEmailOfJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.JWT_SIGNATURE)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SecurityConstants.JWT_SIGNATURE).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt expirado o incorrecto");
        }
    }
}
