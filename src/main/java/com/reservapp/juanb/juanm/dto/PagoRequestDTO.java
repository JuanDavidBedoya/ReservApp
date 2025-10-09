package com.reservapp.juanb.juanm.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PagoRequestDTO(
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    double monto,

    @NotNull(message = "El ID del método no puede ser nulo")
    UUID idMetodo
) {}