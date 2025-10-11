package com.reservapp.juanb.juanm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UsuarioUpdateDTO(
    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre,

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    String correo,
    
    @NotBlank(message = "El telefono no puede estar vacío")
    String telefono,

    String contrasena
) {}