package com.reservapp.juanb.juanm.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservaRequestDTO(
    @NotNull(message = "La fecha no puede ser nula")
    LocalDate fecha,

    @NotNull(message = "La hora no puede ser nula") // <<< CORRECCIÓN: @NotBlank es para Strings, se usa @NotNull
    LocalTime hora,

    @Min(value = 1, message = "El número de personas debe ser mayor que 0")
    int numeroPersonas,

    @NotBlank(message = "La cédula del usuario es obligatoria")
    String cedulaUsuario
) {}