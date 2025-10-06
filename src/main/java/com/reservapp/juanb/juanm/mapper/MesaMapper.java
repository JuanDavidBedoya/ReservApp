package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;
import com.reservapp.juanb.juanm.dto.MesaDTO;
import com.reservapp.juanb.juanm.entities.Mesa;

@Component
public class MesaMapper {

    public MesaDTO toResponseDTO(Mesa mesa) {
        if (mesa == null) {
            return null;
        }
        String nombreEstado = (mesa.getEstado() != null) ? mesa.getEstado().getNombre() : null;
        
        return new MesaDTO(
            mesa.getIdMesa(),
            mesa.getNumeroMesa(),
            mesa.getCapacidad(),
            nombreEstado
        );
    }
}