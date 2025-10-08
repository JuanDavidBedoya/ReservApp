package com.reservapp.juanb.juanm.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.dto.ReservaRequestDTO;
import com.reservapp.juanb.juanm.dto.ReservaResponseDTO;
import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.entities.Usuario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.ReservaMapper;
import com.reservapp.juanb.juanm.repositories.EstadoRepositorio;
import com.reservapp.juanb.juanm.repositories.MesaRepositorio;
import com.reservapp.juanb.juanm.repositories.PagoRepositorio;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;
import com.reservapp.juanb.juanm.repositories.UsuarioRepositorio;

@Service
public class ReservaServicio {

    private final ReservaRepositorio reservaRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final MesaRepositorio mesaRepositorio;
    private final EstadoRepositorio estadoRepositorio;
    private final ReservaMapper reservaMapper;
    private final EmailServicio emailServicio;
    private final NotificacionServicio notificacionServicio;
    private final PagoRepositorio pagoRepositorio;

    public ReservaServicio(
            ReservaRepositorio reservaRepositorio,
            UsuarioRepositorio usuarioRepositorio,
            MesaRepositorio mesaRepositorio,
            EstadoRepositorio estadoRepositorio,
            ReservaMapper reservaMapper,
            EmailServicio emailServicio, 
            NotificacionServicio notificacionServicio,
            PagoRepositorio pagoRepositorio
    ) {
        this.reservaRepositorio = reservaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.mesaRepositorio = mesaRepositorio;
        this.estadoRepositorio = estadoRepositorio;
        this.reservaMapper = reservaMapper;
        this.emailServicio = emailServicio; 
        this.notificacionServicio = notificacionServicio;
        this.pagoRepositorio = pagoRepositorio;
    }

    //Obtener todas las reservas (GET)
    public List<ReservaResponseDTO> findAll() {
        return reservaRepositorio.findAll()
                .stream()
                .map(reservaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    //Buscar por ID (GET /{id})
    public ReservaResponseDTO findById(UUID uuid) {
        Reserva reserva = reservaRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid));
        return reservaMapper.toResponseDTO(reserva);
    }

    //Crear nueva reserva (POST)
    public ReservaResponseDTO save(ReservaRequestDTO dto) {
        try {
            Usuario usuario = usuarioRepositorio.findById(dto.cedulaUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            Mesa mesa = mesaRepositorio.findById(dto.idMesa())
                    .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));

            Estado estado = estadoRepositorio.findById(dto.idEstado())
                    .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));

            Reserva reserva = reservaMapper.fromRequestDTO(dto, usuario, mesa, estado);

            if (exceedsCapacity(reserva)) {
                throw new BadRequestException("El número de personas excede la capacidad de la mesa");
            }
            if (!isMesaDisponible(mesa, reserva.getFecha(), reserva.getHora())) {
                throw new BadRequestException("La mesa ya tiene una reserva en ese horario");
            } 

            Reserva guardada = reservaRepositorio.save(reserva);

            try {

                int numeroMesa = guardada.getMesa().getNumeroMesa();
                Optional<Pago> pagoOpt = pagoRepositorio.findByReserva(guardada);
                double monto = pagoOpt.map(Pago::getMonto).orElse(0.0);

                String asunto = "¡Reserva Confirmada!";
                String cuerpo = String.format(
                    "Hola %s,\n\nTu reserva ha sido confirmada con los siguientes detalles:\n" +
                    "- Fecha: %s\n" +
                    "- Hora: %s\n" +
                    "- Mesa N°: %d\n" +
                    "- Monto: $%.2f\n\n" +
                    "¡Te esperamos!",
                    guardada.getUsuario().getNombre(),
                    guardada.getFecha(),
                    guardada.getHora(),
                    numeroMesa,
                    monto
                );
                
                emailServicio.enviarNotificacionSimple(guardada.getUsuario().getCorreo(), asunto, cuerpo);
                notificacionServicio.registrarNotificacion(guardada, "Confirmación", cuerpo);

            } catch (Exception e) {
                System.err.println("La reserva se guardó, pero falló el envío de la notificación: " + e.getMessage());
            }
            
            return reservaMapper.toResponseDTO(guardada);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar la reserva: " + e.getMessage());
        }
    }

    //Actualizar reserva (PUT)
    public ReservaResponseDTO update(UUID uuid, ReservaRequestDTO dto) {
        if (!reservaRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid);
        }

        Usuario usuario = usuarioRepositorio.findById(dto.cedulaUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Mesa mesa = mesaRepositorio.findById(dto.idMesa())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada"));

        Estado estado = estadoRepositorio.findById(dto.idEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));

        Reserva reserva = reservaMapper.fromRequestDTO(dto, usuario, mesa, estado);
        reserva.setIdReserva(uuid); // mantener ID original

        if (exceedsCapacity(reserva)) {
            throw new BadRequestException("El número de personas excede la capacidad de la mesa");
        }

        Reserva actualizada = reservaRepositorio.save(reserva);
        return reservaMapper.toResponseDTO(actualizada);
    }

    //Eliminar reserva (DELETE)
    public void delete(UUID uuid) {
        try {
            if (!reservaRepositorio.existsById(uuid)) {
                throw new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid);
            }
            reservaRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar la reserva: " + e.getMessage());
        }
    }

    //Validación de capacidad de personas en la mesa
    public boolean exceedsCapacity(Reserva reserva) {
        return reserva.getMesa() != null &&
                reserva.getNumeroPersonas() > reserva.getMesa().getCapacidad();
    }

    //Validar si la mesa está libre ese día (RF20)
    public boolean isMesaDisponible(Mesa mesa, LocalDate fecha, LocalTime horaInicioNueva) {
        // Calcular hora de fin (2 horas después)
        LocalTime inicio = horaInicioNueva;
        LocalTime fin = inicio.plusHours(2);

        // Buscar reservas existentes en esa fecha
        List<Reserva> reservasExistentes = reservaRepositorio.findByMesaAndFecha(mesa, fecha);

        for (Reserva r : reservasExistentes) {
            LocalTime inicioExistente = r.getHora();
            LocalTime finExistente = inicioExistente.plusHours(2);

            // Verificar solapamiento: si los intervalos se cruzan
            boolean solapan = inicio.isBefore(finExistente) && inicioExistente.isBefore(fin);
            if (solapan) {
                return false; // hay cruce, no disponible
            }
        }
        return true; // libre
    }

}