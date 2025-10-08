package com.reservapp.juanb.juanm.services;

import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionProgramadaServicio {

    private final ReservaRepositorio reservaRepositorio;
    private final EmailServicio emailServicio;
    private final NotificacionServicio notificacionServicio;
    private final ReservaServicio reservaServicio;

    public NotificacionProgramadaServicio(
        ReservaRepositorio reservaRepositorio, 
        EmailServicio emailServicio, 
        NotificacionServicio notificacionServicio,
        ReservaServicio reservaServicio 
    ) {
        this.reservaRepositorio = reservaRepositorio;
        this.emailServicio = emailServicio;
        this.notificacionServicio = notificacionServicio;
        this.reservaServicio = reservaServicio;
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
}