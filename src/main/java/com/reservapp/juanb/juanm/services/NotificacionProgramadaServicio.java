package com.reservapp.juanb.juanm.services;

import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionProgramadaServicio {

    private final ReservaRepositorio reservaRepositorio;
    private final EmailServicio emailServicio;
    private final NotificacionServicio notificacionServicio;

    public NotificacionProgramadaServicio(ReservaRepositorio reservaRepositorio, EmailServicio emailServicio, NotificacionServicio notificacionServicio) {
        this.reservaRepositorio = reservaRepositorio;
        this.emailServicio = emailServicio;
        this.notificacionServicio = notificacionServicio;
    }

    // Se ejecuta cada hora
    @Scheduled(cron = "0 0 * * * *")
    public void enviarRecordatorios24h() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite24h = ahora.plusHours(24);
        
        List<Reserva> reservas = reservaRepositorio.findReservasActivasEnRango(ahora, limite24h);

        for (Reserva r : reservas) {
            if (!r.isRecordatorio24hEnviado()) {
                String asunto = "Recordatorio de tu reserva para mañana";
                String cuerpo = "Hola " + r.getUsuario().getNombre() + ",\nTe recordamos tu reserva para mañana " + r.getFecha() + " a las " + r.getHora() + ".";
                
                emailServicio.enviarNotificacionSimple(r.getUsuario().getCorreo(), asunto, cuerpo);
                notificacionServicio.registrarNotificacion(r, "Recordatorio", cuerpo);
                
                r.setRecordatorio24hEnviado(true);
                reservaRepositorio.save(r);
            }
        }
    }

    // Se ejecuta cada 10 minutos
    @Scheduled(cron = "0 */10 * * * *")
    public void enviarRecordatorios1h() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite1h = ahora.plusHours(1);

        List<Reserva> reservas = reservaRepositorio.findReservasActivasEnRango(ahora, limite1h);

        for (Reserva r : reservas) {
            if (!r.isRecordatorio1hEnviado()) {
                String asunto = "¡Tu reserva es en una hora!";
                String cuerpo = "Hola " + r.getUsuario().getNombre() + ",\nTu reserva es hoy a las " + r.getHora() + ". ¡Te esperamos!";
                
                emailServicio.enviarNotificacionSimple(r.getUsuario().getCorreo(), asunto, cuerpo);
                notificacionServicio.registrarNotificacion(r, "Recordatorio", cuerpo);

                r.setRecordatorio1hEnviado(true);
                reservaRepositorio.save(r);
            }
        }
    }

    // Se ejecuta cada hora
    @Scheduled(cron = "0 0 * * * *")
    public void enviarEncuestas() {
        // Consideramos una reserva "terminada" 2 horas después de su inicio
        LocalDateTime momentoFinalizado = LocalDateTime.now().minusHours(2);
        
        List<Reserva> reservasFinalizadas = reservaRepositorio.findReservasFinalizadasParaEncuesta(momentoFinalizado);

        for (Reserva r : reservasFinalizadas) {
            String asunto = "¿Cómo fue tu experiencia?";
            String cuerpo = "Hola " + r.getUsuario().getNombre() + ",\nGracias por visitarnos. Ayúdanos a mejorar con esta breve encuesta: https://tu-encuesta.com/reserva=" + r.getIdReserva();
            
            emailServicio.enviarNotificacionSimple(r.getUsuario().getCorreo(), asunto, cuerpo);
            notificacionServicio.registrarNotificacion(r, "Encuesta", cuerpo);

            r.setEncuestaEnviada(true);
            reservaRepositorio.save(r);
        }
    }
}