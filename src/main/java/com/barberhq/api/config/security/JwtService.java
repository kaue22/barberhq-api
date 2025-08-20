package com.barberhq.api.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret:barberhq-secret-key}")
    private String secret;

    @Value("${jwt.expiration:7200}") // 2 horas em segundos
    private Long expiration;

    public String generateToken(UUID userId, String email, String role) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("barberhq-api")
                    .withSubject(userId.toString())
                    .withClaim("email", email)
                    .withClaim("role", role)
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("barberhq-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("barberhq-api")
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    public String extractEmail(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT != null ? decodedJWT.getClaim("email").asString() : null;
    }

    public String extractRole(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT != null ? decodedJWT.getClaim("role").asString() : null;
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusSeconds(expiration).toInstant(ZoneOffset.UTC);
    }
}