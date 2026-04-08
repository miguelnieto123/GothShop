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
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Servicio encargado de la gestión de JSON Web Tokens (JWT).
 * Proporciona métodos para crear, validar y leer la información de los tokens
 * de acceso.
 */
@Service
public class JwtService {

    // Inyectamos la clave secreta desde el archivo de configuración
    // (application.properties/yml)
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    // Inyectamos el tiempo de vida del token (en milisegundos)
    @Value("${security.jwt.token-expiration}")
    private Long tokenExpiration;

    /**
     * Crea un JWT con la información del usuario.
     *
     * @param id   El ID del usuario.
     * @param name El nombre del usuario.
     * @param rol El rol del usuario.
     * @return Un token JWT firmado con la información proporcionada.
     */
    public String generateToken(Long id, String name, String email, String rol) {
         return Jwts.builder()
                .claims(Map.of("id_user", id)) // Agregamos datos personalizados (payload)
                .claims(Map.of("email", email))
                .claims(Map.of("Id_rol", rol))
                .subject(name) // Identificamos al dueñ""o del token
                .issuedAt(new Date()) // Fecha de creación
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration)) // Fecha de vencimiento
                .signWith(getSigningKey()) // Firma digital para evitar alteraciones
                .compact(); // Construye el String final
    }

    /**
     * Transforma la clave secreta de String (Base64) a un objeto SecretKey
     * utilizable por la librería.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Verifica si un token es íntegro y no ha sido manipulado.
     * 
     * @param token El JWT a validar.
     * @return true si la firma es válida, false en caso contrario.
     */
    public Boolean isTokenValid(String token) {
        try {
            // El parser intenta descifrar la firma con nuestra llave secreta
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            // Si el token expiró, la firma es incorrecta o está corrupto, entra aquí
            e.printStackTrace();
            return false;
        }
    }

    public <T> T exctractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    public String extractName(String token) {
        return exctractClaims(token, Claims::getSubject);
    }

    public String extractEmail(String token) {
        return exctractClaims(token, claims -> claims.get("email", String.class));
    }

    public Long extractId(String token) {
        return exctractClaims(token, claims -> claims.get("user_Id", Long.class));
    }

    public String extractRole(String token) {
        return exctractClaims(token, claims -> claims.get("rol_Id", String.class));
    }

    public String refreshToken(String token) {
        Claims claims;

        try {
            // Intento de lectura normal si aún es válido
            claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token is expired: " + e.getMessage());
        } catch (JwtException e) {
            throw new RuntimeException("Token is invalid: " + e.getMessage());
        }

        // Generamos un nuevo token con los datos recuperados
        return generateToken(claims.get("user_Id", Long.class), claims.getSubject(), claims.get("email", String.class), claims.get("rol_Id", String.class));
    }
}
