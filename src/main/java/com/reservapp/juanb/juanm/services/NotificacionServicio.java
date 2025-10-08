package com.reservapp.juanb.juanm.services;

import com.reservapp.juanb.juanm.dto.NotificacionResponseDTO;
import com.reservapp.juanb.juanm.entities.Notificacion;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.entities.Tipo;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.NotificacionMapper;
import com.reservapp.juanb.juanm.repositories.NotificacionRepositorio;
import com.reservapp.juanb.juanm.repositories.TipoRepositorio;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificacionServicio {

    private final NotificacionRepositorio notificacionRepositorio;
    private final TipoRepositorio tipoRepositorio;
    private final NotificacionMapper notificacionMapper; 

    public NotificacionServicio(
        NotificacionRepositorio notificacionRepositorio, 
        TipoRepositorio tipoRepositorio, 
        NotificacionMapper notificacionMapper 
    ) {
        this.notificacionRepositorio = notificacionRepositorio;
        this.tipoRepositorio = tipoRepositorio;
        this.notificacionMapper = notificacionMapper;
    }

    public List<NotificacionResponseDTO> findAll() {
        return notificacionRepositorio.findAll().stream()
                .map(notificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public NotificacionResponseDTO findById(UUID uuid) {
        Notificacion notificacion = notificacionRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid));
        return notificacionMapper.toResponseDTO(notificacion);
    }

    public void registrarNotificacion(Reserva reserva, String nombreTipo, String mensaje) {
        Tipo tipo = tipoRepositorio.findByNombre(nombreTipo)
                .orElseThrow(() -> new IllegalStateException("Tipo de notificación no encontrado: " + nombreTipo));

        Notificacion notificacion = new Notificacion();
        notificacion.setReserva(reserva);
        notificacion.setTipo(tipo);
        notificacion.setMensaje(mensaje);
        notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));
        
        notificacionRepositorio.save(notificacion);
    }
}