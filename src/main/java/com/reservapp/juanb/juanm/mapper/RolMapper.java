package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;
import com.reservapp.juanb.juanm.dto.RolDTO;
import com.reservapp.juanb.juanm.entities.Rol;

@Component
public class RolMapper {

    public RolDTO toResponseDTO(Rol rol) {
        if (rol == null) {
            return null;
        }
        return new RolDTO(
            rol.getIdRol(),
            rol.getNombre()
        );
    }
}