package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.reservapp.juanb.juanm.dto.TipoDTO;
import com.reservapp.juanb.juanm.entities.Tipo;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.TipoMapper;
import com.reservapp.juanb.juanm.repositories.TipoRepositorio;

@Service
public class TipoServicio {

    private final TipoRepositorio tipoRepositorio;
    private final TipoMapper tipoMapper;

    public TipoServicio(TipoRepositorio tipoRepositorio, TipoMapper tipoMapper) {
        this.tipoRepositorio = tipoRepositorio;
        this.tipoMapper = tipoMapper;
    }

    public List<TipoDTO> findAll() {
        return tipoRepositorio.findAll().stream()
                .map(tipoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public TipoDTO findById(UUID uuid) {
        Tipo tipo = tipoRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo no encontrado con ID: " + uuid));
        return tipoMapper.toResponseDTO(tipo);
    }
}