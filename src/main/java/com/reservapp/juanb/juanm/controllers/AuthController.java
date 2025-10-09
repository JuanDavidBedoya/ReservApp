package com.reservapp.juanb.juanm.controllers;

import com.reservapp.juanb.juanm.dto.AuthResponseDTO; // <-- NUEVO IMPORT
import com.reservapp.juanb.juanm.dto.LoginRequestDTO;
import com.reservapp.juanb.juanm.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        // 1. El servicio de autenticación ahora devuelve el token como un String.
        String jwtToken = authService.login(request);
        
        // 2. Creamos el DTO de respuesta con el token.
        AuthResponseDTO response = new AuthResponseDTO(jwtToken);
        
        // 3. Devolvemos la respuesta en el cuerpo del JSON.
        return ResponseEntity.ok(response);
    }
}