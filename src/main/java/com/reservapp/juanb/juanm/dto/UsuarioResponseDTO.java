package com.reservapp.juanb.juanm.dto;

public record UsuarioResponseDTO(
    String cedula,
    String nombre,
    String correo,
    String telefono,
    String nombreRol
) {}