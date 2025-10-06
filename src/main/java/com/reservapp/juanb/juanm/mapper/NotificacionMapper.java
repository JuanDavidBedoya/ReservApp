package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;

import com.reservapp.juanb.juanm.dto.NotificacionRequestDTO;
import com.reservapp.juanb.juanm.dto.NotificacionResponseDTO;
import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Notificacion;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.entities.Tipo;

@Component
public class NotificacionMapper {

    //Convierte DTO de creación/actualización -> entidad
    public Notificacion fromRequestDTO(NotificacionRequestDTO dto, Tipo tipo, Reserva reserva, Estado estado) {
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje(dto.mensaje());
        notificacion.setFechaEnvio(dto.fechaEnvio());
        notificacion.setTipo(tipo);
        notificacion.setReserva(reserva);
        notificacion.setEstado(estado);
        return notificacion;
    }

    //Convierte entidad -> DTO de respuesta
    public NotificacionResponseDTO toResponseDTO(Notificacion notificacion) {
        return new NotificacionResponseDTO(
                notificacion.getIdNotificacion(),
                notificacion.getMensaje(),
                notificacion.getFechaEnvio(),
                notificacion.getTipo() != null ? notificacion.getTipo().getNombre() : null,
                notificacion.getEstado() != null ? notificacion.getEstado().getNombre() : null,
                notificacion.getReserva() != null ? notificacion.getReserva().getIdReserva() : null
        );
    }
}
