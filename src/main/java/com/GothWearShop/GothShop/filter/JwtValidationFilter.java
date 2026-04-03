package com.GothWearShop.GothShop.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.GothWearShop.GothShop.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain)
            throws IOException {
        // Obtención del encabezado, buscar el header llamado "Authorization"
        String authHeader = request.getHeader("Authorization");

        // Un token legal debe existir y empezar con la palabra "Bearer " (portador)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Header Authorization is missing in the request\"}");
            return; // Cortamos el flujo y asi la petición no alcanza a llegar al controller
        }

        // Limpieza del token, extraemos solo el string del JWT, saltandonos los
        // primeros 7 caracteres ("Bearer ")
        String token = authHeader.substring(7);

        try {
            // Usamos el servicio para ver si la firma es real y si no ha expirado
            if (jwtService.isTokenValid(token)) {
                String name = jwtService.extractUsername(token);
                Long id_user = jwtService.extractUserId(token);
                Long id_rol = jwtService.extractRolId(token);

                request.setAttribute("name", name);
                request.setAttribute("id_user", id_user);
                request.setAttribute("rol_id", id_rol);

                // Si todo esta bien, continuamos al siguiente paso, puede ser otro filtro o ya
                // directamente al controller
                filterchain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token is invalid or expired\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Validation failed\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Estas rutas son publicas para que el usuario pueda entrar sin ningun token
        // /refresh es publico porque el JwtService maneja su propia logica de tokens
        // vencidos
        return path.equals("/auth/login") ||
                path.equals("/auth/register") ||
                path.equals("/auth/refreshToken");
    }
}