package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.dto.NotificacionRequestDTO;
import com.reservapp.juanb.juanm.dto.NotificacionResponseDTO;
import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Notificacion;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.entities.Tipo;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.NotificacionMapper;
import com.reservapp.juanb.juanm.repositories.EstadoRepositorio;
import com.reservapp.juanb.juanm.repositories.NotificacionRepositorio;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;
import com.reservapp.juanb.juanm.repositories.TipoRepositorio;

@Service
public class NotificacionServicio {

    private NotificacionRepositorio notificacionRepositorio;
    private TipoRepositorio tipoRepositorio;
    private EstadoRepositorio estadoRepositorio;
    private ReservaRepositorio reservaRepositorio;
    private NotificacionMapper notificacionMapper;

    public NotificacionServicio(
            NotificacionRepositorio notificacionRepositorio,
            TipoRepositorio tipoRepositorio,
            EstadoRepositorio estadoRepositorio,
            ReservaRepositorio reservaRepositorio,
            NotificacionMapper notificacionMapper
    ) {
        this.notificacionRepositorio = notificacionRepositorio;
        this.tipoRepositorio = tipoRepositorio;
        this.estadoRepositorio = estadoRepositorio;
        this.reservaRepositorio = reservaRepositorio;
        this.notificacionMapper = notificacionMapper;
    }

    //Listar todas
    public List<NotificacionResponseDTO> findAll() {
        return notificacionRepositorio.findAll()
                .stream()
                .map(notificacionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    //Buscar por ID
    public NotificacionResponseDTO findById(UUID uuid) {
        Notificacion notificacion = notificacionRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid));
        return notificacionMapper.toResponseDTO(notificacion);
    }

    //Crear
    public NotificacionResponseDTO save(NotificacionRequestDTO dto) {
        try {
            Tipo tipo = tipoRepositorio.findById(dto.idTipo())
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo no encontrado"));
            Estado estado = estadoRepositorio.findById(dto.idEstado())
                    .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));
            Reserva reserva = reservaRepositorio.findById(dto.idReserva())
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

            Notificacion notificacion = notificacionMapper.fromRequestDTO(dto, tipo, reserva, estado);
            Notificacion guardada = notificacionRepositorio.save(notificacion);

            return notificacionMapper.toResponseDTO(guardada);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar la notificación: " + e.getMessage());
        }
    }

    //Actualizar
    public NotificacionResponseDTO update(UUID uuid, NotificacionRequestDTO dto) {
        if (!notificacionRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid);
        }

        Tipo tipo = tipoRepositorio.findById(dto.idTipo())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo no encontrado"));
        Estado estado = estadoRepositorio.findById(dto.idEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));
        Reserva reserva = reservaRepositorio.findById(dto.idReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        Notificacion notificacion = notificacionMapper.fromRequestDTO(dto, tipo, reserva, estado);
        notificacion.setIdNotificacion(uuid);

        Notificacion actualizada = notificacionRepositorio.save(notificacion);
        return notificacionMapper.toResponseDTO(actualizada);
    }

    //Eliminar
    public void delete(UUID uuid) {
        if (!notificacionRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Notificación no encontrada con ID: " + uuid);
        }
        try {
            notificacionRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar la notificación: " + e.getMessage());
        }
    }
}