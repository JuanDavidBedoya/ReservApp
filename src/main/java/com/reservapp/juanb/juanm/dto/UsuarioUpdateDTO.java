// Ubicación: com/reservapp/juanb/juanm/dto/UsuarioUpdateDTO.java
package com.reservapp.juanb.juanm.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioUpdateDTO(
    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre,

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    String correo,
    
    String telefono,

    @NotNull(message = "El ID del rol no puede ser nulo")
    UUID idRol
) {}