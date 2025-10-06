// Ubicación: com/reservapp/juanb/juanm/services/MesaServicio.java
package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.reservapp.juanb.juanm.dto.MesaDTO;
import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.MesaMapper;
import com.reservapp.juanb.juanm.repositories.MesaRepositorio;

@Service
public class MesaServicio {

    private final MesaRepositorio mesaRepositorio;
    private final MesaMapper mesaMapper;

    public MesaServicio(MesaRepositorio mesaRepositorio, MesaMapper mesaMapper) {
        this.mesaRepositorio = mesaRepositorio;
        this.mesaMapper = mesaMapper;
    }

    public List<MesaDTO> findAll() {
        return mesaRepositorio.findAll().stream()
                .map(mesaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MesaDTO findById(UUID uuid) {
        Mesa mesa = mesaRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + uuid));
        return mesaMapper.toResponseDTO(mesa);
    }
}