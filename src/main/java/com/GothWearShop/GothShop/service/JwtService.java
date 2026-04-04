package com.GothWearShop.GothShop.service;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.GothWearShop.GothShop.util.RoleMapper;

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

     private RoleMapper roleMapper;
    /**
     * Genera un nuevo token JWT para un usuario específico.
     * 
     * @param Id_user   Identificador único del usuario.
     * @param name Nombre de usuario (sujeto del token).
     * @param id_rol    Identificador del rol del usuario.
     * @return Un String que contiene el JWT firmado.
     */
   

    public String generateToken(Long id_user, String name, Long rol_Id) {

        String role = roleMapper.map(rol_Id); 

        return Jwts.builder()
                .setClaims(Map.of(
                        "id_user", id_user,
                        "role", role 
                ))
                .setSubject(name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSigningKey())
                .compact();
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
    return exctractClaims(token, claims -> claims.get("id_user", Long.class));
}

    public String extractRole(String token) {
    return exctractClaims(token, claims -> claims.get("role", String.class));
}

    public String refreshToken(String token) {
    Claims claims;

    try {
        claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    } catch (ExpiredJwtException e) {
        claims = e.getClaims();
    }

    return generateToken(
            claims.get("id_user", Long.class),
            claims.getSubject(),
            mapRoleBack(claims.get("role", String.class)) // 🔥 inverso
    );
}

        private Long mapRoleBack(String role) {
        if ("ROLE_ADMIN".equals(role)) return 1L;
        if ("ROLE_USER".equals(role)) return 2L;
        return 2L;
    }

   
    private SecretKey getSigningKey1() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
