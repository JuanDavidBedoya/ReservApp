// Ubicación: com/reservapp/juanb/juanm/services/RolServicio.java
package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.reservapp.juanb.juanm.dto.RolDTO;
import com.reservapp.juanb.juanm.entities.Rol;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.RolMapper;
import com.reservapp.juanb.juanm.repositories.RolRepositorio;

@Service
public class RolServicio {

    private final RolRepositorio rolRepositorio;
    private final RolMapper rolMapper;

    public RolServicio(RolRepositorio rolRepositorio, RolMapper rolMapper) {
        this.rolRepositorio = rolRepositorio;
        this.rolMapper = rolMapper;
    }

    public List<RolDTO> findAll() {
        return rolRepositorio.findAll().stream()
                .map(rolMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public RolDTO findById(UUID uuid) {
        Rol rol = rolRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + uuid));
        return rolMapper.toResponseDTO(rol);
    }
}