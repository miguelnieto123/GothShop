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
 * @param <Claims>
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
     * Genera un nuevo token JWT para un usuario específico.
     * 
     * @param Id_user   Identificador único del usuario.
     * @param name Nombre de usuario (sujeto del token).
     * @param id_rol    Identificador del rol del usuario.
     * @return Un String que contiene el JWT firmado.
     */
    public String generateToken(Long id_user, String name, Long rol_Id) {
        return Jwts.builder()
                .setClaims(Map.of("id_user", id_user, "rol_id", rol_Id)) // Agregamos datos personalizados (payload)
                .setSubject(name) // Identificamos al dueño del token
                .setIssuedAt(new Date()) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) // Fecha de vencimiento
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
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // Si el token expiró, la firma es incorrecta o está corrupto, entra aquí
            e.printStackTrace();
            return false;
        }
    }

    public <T> T exctractClaims(String token, Function<io.jsonwebtoken.Claims, T> resolver) {
        final io.jsonwebtoken.Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return resolver.apply(claims);
    }

    public String extractUsername(String token) {
        return exctractClaims(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        return exctractClaims(token, claims -> claims.get("userId", Long.class));
    }

    public Long extractRolId(String token) {
        return exctractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    public String refreshToken(String token) {
        Claims claims;

        try {
            // Intento de lectura normal si aún es válido
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // Si expiró, recuperamos los datos del cuerpo del error
            // Esto permite que el usuario no pierda su sesión de inmediato
            claims = (Claims) e.getClaims();
        } catch (JwtException e) {
            throw new RuntimeException("Token is invalid: " + e.getMessage());
        }

        // Generamos un nuevo token con los datos recuperados
        return generateToken(claims.get("userId", Long.class), claims.getSubject(), claims.get("rolId", Long.class));
    }
}