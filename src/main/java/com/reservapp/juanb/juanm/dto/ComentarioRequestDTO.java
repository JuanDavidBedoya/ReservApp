package com.reservapp.juanb.juanm.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComentarioRequestDTO(
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    Integer puntuacion,

    @NotBlank(message = "El mensaje no puede estar vacío")
    String mensaje,

    @NotNull(message = "El usuario es obligatorio")
    String idUsuario
) {}
