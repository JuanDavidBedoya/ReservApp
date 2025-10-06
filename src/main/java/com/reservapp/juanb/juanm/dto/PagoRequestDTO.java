package com.reservapp.juanb.juanm.dto;

import java.sql.Date;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PagoRequestDTO(
    @Min(value = 1, message = "El monto debe ser mayor a 0")
    double monto,

    @NotNull(message = "La fecha de pago no puede estar vacía")
    Date fechaPago,

    @NotNull(message = "El ID del método no puede ser nulo")
    UUID idMetodo,

    @NotNull(message = "El ID del estado no puede ser nulo")
    UUID idEstado,

    @NotNull(message = "El ID de la reserva no puede ser nulo")
    UUID idReserva
) {}