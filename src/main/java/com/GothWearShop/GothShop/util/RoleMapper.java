package com.GothWearShop.GothShop.util;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    private static final Map<Long, String> ROLES = Map.of(
        1L, "ROLE_ADMIN",
        2L, "ROLE_USER"
    );

    public String map(Long idRol) {
        return ROLES.getOrDefault(idRol, "ROLE_USER");
    }
}