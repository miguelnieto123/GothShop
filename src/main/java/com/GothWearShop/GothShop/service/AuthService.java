package com.GothWearShop.GothShop.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.GothWearShop.GothShop.dto.LoginRequestDTO;
import com.GothWearShop.GothShop.dto.LoginResponseDTO;
import com.GothWearShop.GothShop.dto.MessageResponseDTO;
import com.GothWearShop.GothShop.dto.RefreshTokenResponseDTO;
import com.GothWearShop.GothShop.dto.RegisterRequestDTO;
import com.GothWearShop.GothShop.entity.User;
import com.GothWearShop.GothShop.repository.RolRepository;
import com.GothWearShop.GothShop.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final UsersRepository usersRepository;

    private final RolRepository rolRepository;

    private final JwtService jwtService;

    public MessageResponseDTO register(RegisterRequestDTO request) {
    MessageResponseDTO response = new MessageResponseDTO();
    response.setMessage("Registro exitoso");

    if (usersRepository.findByUsername(request.getName()).isPresent()) {
        throw new RuntimeException("Este nombre de usuario ya está en uso");
    }

    User user = new User();
    user.setUsername(request.getName());
    user.setEmail(request.getEmail());
    user.setUserpassword(passwordEncoder.encode(request.getPassword()));

   
    user.setId_rol(2L); 

    usersRepository.save(user);

    return response;
}
    public LoginResponseDTO login(LoginRequestDTO request) {
        LoginResponseDTO response = new LoginResponseDTO();
        Optional<User> user = usersRepository.findByUsername(request.getName());

        if (user.isEmpty() && request.getName() != null) {
            response.setMessage("Este usuario no se encuentra registrado");
            return response;
        }

        User userFound = user.get();

        if (!passwordEncoder.matches(request.getPassword(), userFound.getUserpassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String jwt = jwtService.generateToken(userFound.getId_user(), userFound.getUsername(), userFound.getId_rol());

        response.setMessage("Inicio de sesión exitoso");
        response.setJwt(jwt);
        return response;
    }

    /**
     * Este método es para el refresco del token
     * @param token jwt viejo
     * @return nuevo token
     */
    public RefreshTokenResponseDTO refreshToken(String token) {
        String jwt = jwtService.refreshToken(token);
        RefreshTokenResponseDTO response = new RefreshTokenResponseDTO();
        response.setJwt(jwt);
        return response;
    }
}
