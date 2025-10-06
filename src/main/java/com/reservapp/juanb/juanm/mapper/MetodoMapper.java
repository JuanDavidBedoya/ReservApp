package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;
import com.reservapp.juanb.juanm.dto.MetodoDTO;
import com.reservapp.juanb.juanm.entities.Metodo;

@Component
public class MetodoMapper {

    public MetodoDTO toResponseDTO(Metodo metodo) {
        if (metodo == null) {
            return null;
        }
        return new MetodoDTO(
            metodo.getIdMetodo(),
            metodo.getNombre()
        );
    }
}