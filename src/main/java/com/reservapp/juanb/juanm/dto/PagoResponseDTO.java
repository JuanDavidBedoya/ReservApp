package com.reservapp.juanb.juanm.dto;

import java.sql.Date;
import java.util.UUID;

public record PagoResponseDTO(
    UUID idPago,
    double monto,
    Date fechaPago,
    String nombreMetodo,
    String nombreEstado,
    UUID idReserva
) {}
