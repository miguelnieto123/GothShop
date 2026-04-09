package com.GothWearShop.GothShop.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Servicio encargado de la gestión de JSON Web Tokens (JWT).
 */
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token-expiration}")
    private Long tokenExpiration;

    /**
     * Genera el token JWT
     */
    public String generateToken(Long id, String name, String email, String rol) {
        return Jwts.builder()
                .addClaims(Map.of(
                        "id_user", id,
                        "email", email,
                        "rol", rol
                ))
                .setSubject(name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Genera la clave secreta
     * (VERSIÓN SIMPLE - NO BASE64)
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Valida el token
     */
    public Boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Extrae los claims del token
     */
    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .parseClaimsJws(token)
                .getBody();

        return resolver.apply(claims);
    }

    public String extractName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return extractClaims(token, claims -> claims.get("email", String.class));
    }

    public Long extractId(String token) {
        return extractClaims(token, claims -> claims.get("id_user", Long.class));
    }

    public String extractRole(String token) {
        return extractClaims(token, claims -> claims.get("rol", String.class));
    }

    /**
     * Refresca el token
     */
    public String refreshToken(String token) {
        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado: " + e.getMessage());
        } catch (JwtException e) {
            throw new RuntimeException("Token inválido: " + e.getMessage());
        }

        return generateToken(
                claims.get("id_user", Long.class),
                claims.getSubject(),
                claims.get("email", String.class),
                claims.get("rol", String.class)
        );
    }
}