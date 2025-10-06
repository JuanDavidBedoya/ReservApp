package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;
import com.reservapp.juanb.juanm.dto.TipoDTO;
import com.reservapp.juanb.juanm.entities.Tipo;

@Component
public class TipoMapper {

    public TipoDTO toResponseDTO(Tipo tipo) {
        if (tipo == null) {
            return null;
        }
        return new TipoDTO(
            tipo.getIdTipo(),
            tipo.getNombre()
        );
    }
}