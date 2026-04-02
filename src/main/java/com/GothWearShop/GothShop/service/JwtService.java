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
public class JwtService<Claims> {

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
     * @param userId   Identificador único del usuario.
     * @param username Nombre de usuario (sujeto del token).
     * @param rolId    Identificador del rol del usuario.
     * @return Un String que contiene el JWT firmado.
     */
    public String generateToken(Long userId, String username, Long rolId) {
        return Jwts.builder()
                .Claims(Map.of("userId", userId)) // Agregamos datos personalizados (payload)
                .Claims(Map.of("rolId", rolId))
                .subject(username) // Identificamos al dueño del token
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
            claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
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