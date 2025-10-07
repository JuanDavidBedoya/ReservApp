package com.reservapp.juanb.juanm.dto;

import java.sql.Date;
import java.util.UUID;

public record ComentarioResponseDTO(
    UUID idComentario,
    int puntuacion,
    String mensaje,
    Date fechaComentario,
    String nombreUsuario
) {}