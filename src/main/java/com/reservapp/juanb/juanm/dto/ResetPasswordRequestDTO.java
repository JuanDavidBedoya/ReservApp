package com.reservapp.juanb.juanm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(
    @NotBlank(message = "El token es obligatorio")
    String token,

    @NotBlank(message = "La nueva contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String nuevaContrasena
) {}