// Ubicación: com/reservapp/juanb/juanm/services/MetodoServicio.java
package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.reservapp.juanb.juanm.dto.MetodoDTO;
import com.reservapp.juanb.juanm.entities.Metodo;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.MetodoMapper;
import com.reservapp.juanb.juanm.repositories.MetodoRepositorio;

@Service
public class MetodoServicio {

    private final MetodoRepositorio metodoRepositorio;
    private final MetodoMapper metodoMapper;

    public MetodoServicio(MetodoRepositorio metodoRepositorio, MetodoMapper metodoMapper) {
        this.metodoRepositorio = metodoRepositorio;
        this.metodoMapper = metodoMapper;
    }

    public List<MetodoDTO> findAll() {
        return metodoRepositorio.findAll().stream()
                .map(metodoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MetodoDTO findById(UUID uuid) {
        Metodo metodo = metodoRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Método no encontrado con ID: " + uuid));
        return metodoMapper.toResponseDTO(metodo);
    }
}