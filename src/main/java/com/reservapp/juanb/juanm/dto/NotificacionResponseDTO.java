package com.reservapp.juanb.juanm.dto;

import java.sql.Date;
import java.util.UUID;

public record NotificacionResponseDTO(
        UUID idNotificacion,
        String mensaje,
        Date fechaEnvio,
        String tipo,
        String estado,
        UUID idReserva
) {}