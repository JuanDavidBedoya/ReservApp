// Ubicación: com/reservapp/juanb/juanm/dto/UsuarioCreateDTO.java
package com.reservapp.juanb.juanm.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCreateDTO(
    @NotBlank(message = "La cédula no puede estar vacía")
    @Size(min = 5, max = 15, message = "La cédula debe tener entre 5 y 15 caracteres")
    String cedula,

    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre,

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    String correo,

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String contrasena,
    
    String telefono,

    @NotNull(message = "El ID del rol no puede ser nulo")
    UUID idRol
) {}