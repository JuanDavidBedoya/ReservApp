package com.reservapp.juanb.juanm.services;

import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.repositories.EstadoRepositorio;
import com.reservapp.juanb.juanm.repositories.MesaRepositorio;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionProgramadaServicio {

    private final ReservaRepositorio reservaRepositorio;
    private final EmailServicio emailServicio;
    private final NotificacionServicio notificacionServicio;
    private final ReservaServicio reservaServicio;
    private final EstadoRepositorio estadoRepositorio;
    private final MesaRepositorio mesaRepositorio;

    public NotificacionProgramadaServicio(
        ReservaRepositorio reservaRepositorio, 
        EmailServicio emailServicio, 
        NotificacionServicio notificacionServicio,
        ReservaServicio reservaServicio, 
        EstadoRepositorio estadoRepositorio,
        MesaRepositorio mesaRepositorio
    ) {
        this.reservaRepositorio = reservaRepositorio;
        this.emailServicio = emailServicio;
        this.notificacionServicio = notificacionServicio;
        this.reservaServicio = reservaServicio;
        this.estadoRepositorio = estadoRepositorio;
        this.mesaRepositorio = mesaRepositorio;
    }


    @Scheduled(cron = "0 */10 * * * *") // Se ejecuta automaticamente cada 10 min
    public void enviarRecordatorios1h() {
        
        System.out.println("--- Ejecutando tarea de recordatorios-----");

        List<Reserva> todasLasReservas = reservaRepositorio.findAll();

        for (Reserva r : todasLasReservas) {

            long horasFaltantes = reservaServicio.verificarTiempoParaReserva(r.getIdReserva());
            System.out.println("Revisando reserva " + r.getIdReserva() + ". Minutos faltantes: " + horasFaltantes);

            if (horasFaltantes == 0 && !r.isRecordatorio1hEnviado()) {
                System.out.println("¡CONDICIÓN CUMPLIDA! Enviando notificación para reserva " + r.getIdReserva());

                String asunto = "¡Tu reserva es en menos de una hora!";
                String cuerpo = "Hola " + r.getUsuario().getNombre() + ",\nTu reserva es hoy a las " + r.getHora() + ". ¡Te esperamos!";
                
                emailServicio.enviarNotificacionSimple(r.getUsuario().getCorreo(), asunto, cuerpo);
                notificacionServicio.registrarNotificacion(r, "Recordatorio", cuerpo);
                
                r.setRecordatorio1hEnviado(true);
                reservaRepositorio.save(r);
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Se ejecuta automaticamente cada hora
    public void enviarRecordatorios24h() {
        
        System.out.println("--- Ejecutando tarea de recordatorios-----");

        List<Reserva> todasLasReservas = reservaRepositorio.findAll();

        for (Reserva r : todasLasReservas) {

            long horasFaltantes = reservaServicio.verificarTiempoParaReserva(r.getIdReserva());
            System.out.println("Revisando reserva " + r.getIdReserva() + ". Minutos faltantes: " + horasFaltantes);

            if (horasFaltantes == 24 && !r.isRecordatorio24hEnviado()) {
                System.out.println("¡CONDICIÓN CUMPLIDA! Enviando notificación para reserva " + r.getIdReserva());

                String asunto = "¡Tu reserva es mañana!";
                String cuerpo = "Hola " + r.getUsuario().getNombre() + ",\nTu reserva es mañana a las " + r.getHora() + ". ¡Te esperamos!";
                
                emailServicio.enviarNotificacionSimple(r.getUsuario().getCorreo(), asunto, cuerpo);
                notificacionServicio.registrarNotificacion(r, "Recordatorio", cuerpo);
                
                r.setRecordatorio24hEnviado(true);
                reservaRepositorio.save(r);
            }
        }
    }

    @Scheduled(cron = "0 0 * * * *") // Se ejecuta automaticamente cada hora
    public void enviarEncuestas() {
        
        System.out.println("--- Ejecutando tarea de recordatorios-----");

        List<Reserva> todasLasReservas = reservaRepositorio.findAll();

        for (Reserva r : todasLasReservas) {

            long horasFaltantes = reservaServicio.verificarTiempoParaReserva(r.getIdReserva());
            System.out.println("Revisando reserva " + r.getIdReserva() + ". Minutos faltantes: " + horasFaltantes);

            if ((horasFaltantes == -2 || horasFaltantes == -3) && !r.isEncuestaEnviada()) {
                System.out.println("¡CONDICIÓN CUMPLIDA! Enviando notificación para reserva " + r.getIdReserva());

                String asunto = "¡Gracias por Visitarnos!";
                String cuerpo = "Hola " + r.getUsuario().getNombre() + ",\nGracias por tu visita, por favor completa la siguiente encuesta: ";
                
                emailServicio.enviarNotificacionSimple(r.getUsuario().getCorreo(), asunto, cuerpo);
                notificacionServicio.registrarNotificacion(r, "Recordatorio", cuerpo);
                
                r.setEncuestaEnviada(true);
                reservaRepositorio.save(r);
            }
        }
    }

    // Se ejecuta cada 15 minutos
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void gestionarReservas() {
        List<Reserva> reservas = reservaRepositorio.findAll();

        reservas.forEach(reserva -> {
            LocalDateTime fechaHoraReserva = LocalDateTime.of(reserva.getFecha(), reserva.getHora());
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime horaFin = fechaHoraReserva.plusHours(2); // duración de la reserva

            String estadoActual = reserva.getEstado().getNombre();

            try {
                // Cancelar reservas no pagadas faltando 1 hora
                if (estadoActual.equalsIgnoreCase("Confirmada")) {
                    boolean tienePagoConfirmado = reserva.getEstado() != null &&
                            reserva.getEstado().getNombre().equalsIgnoreCase("Pagada");

                    if (!tienePagoConfirmado &&
                            Duration.between(ahora, fechaHoraReserva).toMinutes() <= 60 &&
                            ahora.isBefore(fechaHoraReserva)) {

                        Estado estadoCancelada = estadoRepositorio.findByNombre("Cancelada")
                                .orElseThrow(() -> new RuntimeException("Estado 'Cancelada' no encontrado"));
                        reserva.setEstado(estadoCancelada);
                        reservaRepositorio.save(reserva);

                        // Liberar mesa también
                        Estado estadoDisponible = estadoRepositorio.findByNombre("Disponible")
                                .orElseThrow(() -> new RuntimeException("Estado 'Disponible' no encontrado"));
                        var mesa = reserva.getMesa();
                        mesa.setEstado(estadoDisponible);
                        mesaRepositorio.save(mesa);

                        System.out.println(" Reserva cancelada automáticamente: " + reserva.getIdReserva());
                    }
                }

                // Liberar mesas cuando termine la reserva
                if (estadoActual.equalsIgnoreCase("Pagada") ||
                    estadoActual.equalsIgnoreCase("Confirmada")) {

                    if (ahora.isAfter(horaFin)) {
                        // Cambiar estado de mesa a disponible
                        Estado estadoDisponible = estadoRepositorio.findByNombre("Disponible")
                                .orElseThrow(() -> new RuntimeException("Estado 'Disponible' no encontrado"));
                        var mesa = reserva.getMesa();
                        mesa.setEstado(estadoDisponible);
                        mesaRepositorio.save(mesa);

                        System.out.println("Mesa liberada automáticamente (reserva finalizada): " + mesa.getNumeroMesa());
                    }
                }

            } catch (Exception e) {
                System.err.println("Error en job automático de reserva " + reserva.getIdReserva() + ": " + e.getMessage());
            }
        });
    }
}