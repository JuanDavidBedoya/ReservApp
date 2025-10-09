package com.reservapp.juanb.juanm.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "La cédula no puede estar vacía")
    String cedula,

    @NotBlank(message = "La contraseña не puede estar vacía")
    String contrasena
) {}