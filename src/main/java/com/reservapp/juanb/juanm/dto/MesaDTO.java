package com.reservapp.juanb.juanm.dto;

import java.util.UUID;

public record MesaDTO(
    UUID idMesa,
    int numeroMesa,
    int capacidad,
    String nombreEstado 
) {}