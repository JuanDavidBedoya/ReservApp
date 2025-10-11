package com.reservapp.juanb.juanm.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PagoRequestDTO(
    @NotNull(message = "El ID del método no puede ser nulo")
    UUID idReserva,

    @NotNull(message = "El monto no puede ser nulo")
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    Double monto,

    @NotNull(message = "El ID del método no puede ser nulo")
    UUID idMetodo
) {}