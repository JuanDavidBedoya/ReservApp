package com.reservapp.juanb.juanm.dto;

import java.time.LocalDate;

public record PromedioSemanalDTO(
    Double promedio,
    Integer totalCalificaciones,
    LocalDate fechaInicio,
    LocalDate fechaFin
) {}