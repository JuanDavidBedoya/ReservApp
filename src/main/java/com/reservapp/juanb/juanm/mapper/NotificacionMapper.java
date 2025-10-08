package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;

import com.reservapp.juanb.juanm.dto.NotificacionResponseDTO;
import com.reservapp.juanb.juanm.entities.Notificacion;

@Component
public class NotificacionMapper {

    // Convierte entidad -> DTO de respuesta
    public NotificacionResponseDTO toResponseDTO(Notificacion notificacion) {
        if (notificacion == null) {
            return null;
        }
        return new NotificacionResponseDTO(
                notificacion.getIdNotificacion(),
                notificacion.getMensaje(),
                notificacion.getFechaEnvio(),
                notificacion.getTipo() != null ? notificacion.getTipo().getNombre() : null,
                notificacion.getReserva() != null ? notificacion.getReserva().getIdReserva() : null
        );
    }
}