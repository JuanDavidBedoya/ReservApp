package com.reservapp.juanb.juanm.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservaRequestDTO(
    
        @NotNull(message = "La fecha no puede ser nula")
        LocalDate fecha,

        @NotBlank(message = "La hora no puede estar vacía")
        LocalTime hora,

        @Min(value = 1, message = "El número de personas debe ser mayor que 0")
        int numeroPersonas,

        @NotBlank(message = "La cédula del usuario es obligatoria")
        String cedulaUsuario,

        @NotNull(message = "El ID de la mesa es obligatorio")
        UUID idMesa,

        @NotNull(message = "El ID del estado es obligatorio")
        UUID idEstado
) {}