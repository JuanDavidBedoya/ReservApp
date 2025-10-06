
package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;
import com.reservapp.juanb.juanm.dto.EstadoDTO;
import com.reservapp.juanb.juanm.entities.Estado;

@Component
public class EstadoMapper {

    public EstadoDTO toResponseDTO(Estado estado) {
        if (estado == null) {
            return null;
        }
        return new EstadoDTO(
            estado.getIdEstado(),
            estado.getNombre()
        );
    }
}