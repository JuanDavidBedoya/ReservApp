package com.reservapp.juanb.juanm.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.dto.ReservaRequestDTO;
import com.reservapp.juanb.juanm.dto.ReservaResponseDTO;
import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.entities.Usuario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.ReservaMapper;
import com.reservapp.juanb.juanm.repositories.EstadoRepositorio;
import com.reservapp.juanb.juanm.repositories.MesaRepositorio;
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

    public ReservaServicio(
            ReservaRepositorio reservaRepositorio,
            UsuarioRepositorio usuarioRepositorio,
            MesaRepositorio mesaRepositorio,
            EstadoRepositorio estadoRepositorio,
            ReservaMapper reservaMapper,
            EmailServicio emailServicio, 
            NotificacionServicio notificacionServicio
    ) {
        this.reservaRepositorio = reservaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.mesaRepositorio = mesaRepositorio;
        this.estadoRepositorio = estadoRepositorio;
        this.reservaMapper = reservaMapper;
        this.emailServicio = emailServicio; 
        this.notificacionServicio = notificacionServicio;
    }

    public List<ReservaResponseDTO> findAll() {
        return reservaRepositorio.findAll()
                .stream()
                .map(reservaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReservaResponseDTO findById(UUID uuid) {
        Reserva reserva = reservaRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid));
        return reservaMapper.toResponseDTO(reserva);
    }
    // Crear una reserva
    public ReservaResponseDTO save(ReservaRequestDTO dto) {
        try {

            // Validar fecha y hora
            validarFechaYHora(dto.fecha(), dto.hora());

            Usuario usuario = usuarioRepositorio.findById(dto.cedulaUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // Buscar mesas con capacidad suficiente
            List<Mesa> mesasAdecuadas = mesaRepositorio.findByCapacidadGreaterThanEqual(dto.numeroPersonas());

            // Filtrar por mesas disponibles y libres en el horario solicitado
            Mesa mesaSeleccionada = mesasAdecuadas.stream()
                    .filter(m -> m.getEstado().getNombre().equalsIgnoreCase("Disponible"))
                    .filter(m -> isMesaDisponible(m, dto.fecha(), dto.hora()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("No hay mesas disponibles con capacidad suficiente y horario libre"));

            // Cambiar estado de la mesa a "Ocupada"
            Estado estadoOcupada = estadoRepositorio.findByNombre("Ocupada")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Ocupada' no encontrado"));
            mesaSeleccionada.setEstado(estadoOcupada);
            mesaRepositorio.save(mesaSeleccionada);

            // Estado de reserva "Confirmada"
            Estado estadoConfirmada = estadoRepositorio.findByNombre("Confirmada")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Confirmada' no encontrado"));

            // Crear entidad
            Reserva reserva = reservaMapper.fromRequestDTO(dto, usuario, mesaSeleccionada, estadoConfirmada);

            // Guardar
            Reserva guardada = reservaRepositorio.save(reserva);

            // Notificación
            enviarNotificacionReserva(guardada, "confirmada");

            return reservaMapper.toResponseDTO(guardada);

        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar la reserva: " + e.getMessage());
        }
    }

    public ReservaResponseDTO update(UUID uuid, ReservaRequestDTO dto) {

        // Validar fecha y hora
        validarFechaYHora(dto.fecha(), dto.hora());

        Reserva reservaExistente = reservaRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid));

        reservaExistente.setFecha(dto.fecha());
        reservaExistente.setHora(dto.hora());
        reservaExistente.setNumeroPersonas(dto.numeroPersonas());

        Mesa mesaActual = reservaExistente.getMesa();

        // Si la mesa actual no tiene capacidad o está ocupada en ese horario
        if (mesaActual.getCapacidad() < dto.numeroPersonas() || 
            !isMesaDisponible(mesaActual, dto.fecha(), dto.hora())) {

            // Liberar mesa actual
            Estado estadoDisponible = estadoRepositorio.findByNombre("Disponible")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Disponible' no encontrado"));
            mesaActual.setEstado(estadoDisponible);
            mesaRepositorio.save(mesaActual);

            // Buscar nueva mesa adecuada
            List<Mesa> mesasAdecuadas = mesaRepositorio.findByCapacidadGreaterThanEqual(dto.numeroPersonas());
            Mesa nuevaMesa = mesasAdecuadas.stream()
                    .filter(m -> m.getEstado().getNombre().equalsIgnoreCase("Disponible"))
                    .filter(m -> isMesaDisponible(m, dto.fecha(), dto.hora()))
                    .findFirst()
                    .orElseThrow(() -> new BadRequestException("No hay mesas disponibles con capacidad suficiente y horario libre"));

            // Ocupamos la nueva mesa
            Estado estadoOcupada = estadoRepositorio.findByNombre("Ocupada")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Ocupada' no encontrado"));
            nuevaMesa.setEstado(estadoOcupada);
            mesaRepositorio.save(nuevaMesa);

            reservaExistente.setMesa(nuevaMesa);
        }

        Reserva actualizada = reservaRepositorio.save(reservaExistente);

        // Notificación
        enviarNotificacionReserva(actualizada, "actualizada");

        return reservaMapper.toResponseDTO(actualizada);
    }

    //Cancelar una reserva

    public ReservaResponseDTO cancel(UUID uuid) {
        Reserva reserva = reservaRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid));

        Estado estadoCancelada = estadoRepositorio.findByNombre("Cancelada")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Cancelada' no encontrado"));
        reserva.setEstado(estadoCancelada);

        // Liberar mesa
        Mesa mesa = reserva.getMesa();
        Estado estadoDisponible = estadoRepositorio.findByNombre("Disponible")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Disponible' no encontrado"));
        mesa.setEstado(estadoDisponible);
        mesaRepositorio.save(mesa);

        Reserva cancelada = reservaRepositorio.save(reserva);

        // Notificación
        enviarNotificacionReserva(cancelada, "cancelada");

        return reservaMapper.toResponseDTO(cancelada);
    }

    public ReservaResponseDTO delete(UUID uuid) {
        Reserva reserva = reservaRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid));

        // Cambiar estado de la reserva a "Cancelada"
        Estado estadoCancelada = estadoRepositorio.findByNombre("Cancelada")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Cancelada' no encontrado"));
        reserva.setEstado(estadoCancelada);

        // Liberar la mesa
        Mesa mesa = reserva.getMesa();
        Estado estadoDisponible = estadoRepositorio.findByNombre("Disponible")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Disponible' no encontrado"));
        mesa.setEstado(estadoDisponible);
        mesaRepositorio.save(mesa);

        // Guardar los cambios en la reserva
        Reserva cancelada = reservaRepositorio.save(reserva);

        // Notificar al usuario
        enviarNotificacionReserva(cancelada, "cancelada");

        return reservaMapper.toResponseDTO(cancelada);
    }

    //Validar si la mesa está libre ese día (RF20)
    public boolean isMesaDisponible(Mesa mesa, LocalDate fecha, LocalTime horaInicioNueva) {
  
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

    public Long verificarTiempoParaReserva(UUID uuid) {
        // Para esta prueba, inyecta ReservaRepositorio en tu controlador si no lo tienes
        Reserva reserva = reservaRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + uuid));

        // 1. Combinamos la fecha y hora de la reserva
        LocalDateTime fechaReserva = LocalDateTime.of(reserva.getFecha(), reserva.getHora());

        // 2. Obtenemos la hora actual, USANDO LA MISMA ZONA HORARIA que el planificador
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Bogota"));

        // 3. Calculamos la duración entre ahora y la reserva
        Duration duracion = Duration.between(ahora, fechaReserva);

        // 4. Obtenemos las horas y minutos totales
        long horasFaltantes = duracion.toHours();
        long minutosRestantes = duracion.toMinutes() % 60; // El resto de los minutos

        // 5. Imprimimos un reporte detallado en la consola de Spring Boot
        System.out.println("======================================================");
        System.out.println("DIAGNÓSTICO DE TIEMPO PARA RESERVA: " + uuid);
        System.out.println("Hora Actual (Bogotá): " + ahora);
        System.out.println("Hora de la Reserva:   " + fechaReserva);
        System.out.println("------------------------------------------------------");
        System.out.println("Horas completas restantes: " + horasFaltantes);
        System.out.println("Minutos restantes (adicionales): " + minutosRestantes);
        System.out.println("Total de minutos restantes: " + duracion.toMinutes());
        System.out.println("======================================================");

        return horasFaltantes;
    }

    //  Notificaciones por email
    private void enviarNotificacionReserva(Reserva reserva, String tipo) {
        try {
            String asunto;
            String cuerpo;
            int numeroMesa = reserva.getMesa().getNumeroMesa();

            switch (tipo.toLowerCase()) {
                case "confirmada" -> {
                    asunto = "¡Reserva Confirmada!";
                    cuerpo = String.format(
                        "Hola %s,\n\nTu reserva ha sido confirmada:\n" +
                        "- Fecha: %s\n- Hora: %s\n- Mesa N°: %d\n\n¡Te esperamos!",
                        reserva.getUsuario().getNombre(),
                        reserva.getFecha(),
                        reserva.getHora(),
                        numeroMesa
                    );
                }
                case "actualizada" -> {
                    asunto = "Reserva Actualizada";
                    cuerpo = String.format(
                        "Hola %s,\n\nTu reserva ha sido actualizada:\n" +
                        "- Fecha: %s\n- Hora: %s\n- Mesa N°: %d\n\n¡Gracias por actualizar tu reserva!",
                        reserva.getUsuario().getNombre(),
                        reserva.getFecha(),
                        reserva.getHora(),
                        numeroMesa
                    );
                }
                case "cancelada" -> {
                    asunto = "Reserva Cancelada";
                    cuerpo = String.format(
                        "Hola %s,\n\nTu reserva ha sido cancelada:\n" +
                        "- Fecha: %s\n- Hora: %s\n\nEsperamos verte pronto.",
                        reserva.getUsuario().getNombre(),
                        reserva.getFecha(),
                        reserva.getHora()
                    );
                }
                default -> { return; }
            }

            emailServicio.enviarNotificacionSimple(reserva.getUsuario().getCorreo(), asunto, cuerpo);
            notificacionServicio.registrarNotificacion(reserva, tipo, cuerpo);
        } catch (Exception e) {
            System.err.println("Notificación de " + tipo + " falló: " + e.getMessage());
        }
    }

    // Validar fecha y hora de reserva
    private void validarFechaYHora(LocalDate fecha, LocalTime hora) {
        LocalDate hoy = LocalDate.now();

        if (fecha.isBefore(hoy)) {
            throw new BadRequestException("No se puede crear una reserva en una fecha pasada.");
        }

        LocalTime horaApertura = LocalTime.of(10, 0);
        LocalTime horaCierre = LocalTime.of(22, 0);

        if (hora.isBefore(horaApertura) || hora.isAfter(horaCierre)) {
            throw new BadRequestException("La hora de la reserva debe estar entre las 10:00 y las 22:00.");
        }
    }
}