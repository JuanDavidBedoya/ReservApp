package com.reservapp.juanb.juanm.dto;

import java.sql.Date;
import java.util.UUID;

public record NotificacionRequestDTO(
        String mensaje,
        Date fechaEnvio,
        UUID idTipo,
        UUID idReserva,
        UUID idEstado
) {}
