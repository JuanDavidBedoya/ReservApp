package com.reservapp.juanb.juanm.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    private final JavaMailSender mailSender;

    public EmailServicio(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async // Se ejecuta en un hilo separado para no bloquear la aplicación
    public void enviarNotificacionSimple(String to, String subject, String text) {
        try {
            //Crea el correo a enviar
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("juans.martinezb@uqvirtual.edu.co"); 
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }
}