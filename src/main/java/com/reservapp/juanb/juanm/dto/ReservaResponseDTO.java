package com.reservapp.juanb.juanm.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


public record ReservaResponseDTO(
        UUID idReserva,
        LocalDate fecha,
        LocalTime hora,
        int numeroPersonas,
        String cedulaUsuario,
        UUID idMesa,
        UUID idEstadoMesa,
        String nombreEstado
) {}
