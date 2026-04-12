package com.GothWearShop.GothShop.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.GothWearShop.GothShop.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtValidationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain)
            throws IOException, ServletException {
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
                String name = jwtService.extractName(token);
                Long id = jwtService.extractId(token);
                String role = jwtService.extractRole(token);

                request.setAttribute("name", name);
                request.setAttribute("id", id);
                request.setAttribute("rol_name", role);
                request.setAttribute("id_user", id);

                // Si todo esta bien, continuamos al siguiente paso, puede ser otro filtro o ya
                // directamente al controller
                filterchain.doFilter(request, response);
           } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid token\"}");
                return;
}
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Validation failed\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path   = request.getRequestURI();
        String method = request.getMethod();

        if (path.equals("/auth/login") ||
            path.equals("/auth/registerUsers") ||
            path.equals("/auth/refreshToken")) {
            return true;
        } if (method.equals("GET") && path.startsWith("/products")) {
            return true;
        }

        return false;
    }

    
    
}

