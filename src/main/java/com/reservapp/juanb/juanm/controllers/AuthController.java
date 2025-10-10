package com.reservapp.juanb.juanm.controllers;

import com.reservapp.juanb.juanm.dto.AuthResponseDTO;
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
        // AHORA: El servicio se encarga de toda la lógica y devuelve el DTO completo.
        // El controlador solo lo recibe y lo envía en la respuesta.
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
