// Ubicación: com/reservapp/juanb/juanm/services/EstadoServicio.java
package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.reservapp.juanb.juanm.dto.EstadoDTO;
import com.reservapp.juanb.juanm.mapper.EstadoMapper;
import com.reservapp.juanb.juanm.repositories.EstadoRepositorio;

@Service
public class EstadoServicio {

    private final EstadoRepositorio estadoRepositorio;
    private final EstadoMapper estadoMapper;

    public EstadoServicio(EstadoRepositorio estadoRepositorio, EstadoMapper estadoMapper) {
        this.estadoRepositorio = estadoRepositorio;
        this.estadoMapper = estadoMapper;
    }

    public List<EstadoDTO> findAll() {
        return estadoRepositorio.findAll().stream()
                .map(estadoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}