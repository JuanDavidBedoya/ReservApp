package com.reservapp.juanb.juanm.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

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

    public void enviarNotificacionConAdjunto(String destinatario, String asunto, String cuerpo, byte[] adjunto, String nombreAdjunto) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpo);
            helper.addAttachment(nombreAdjunto, new ByteArrayResource(adjunto));

            mailSender.send(mensaje);
            
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo con adjunto: " + e.getMessage());
        }
    }

    @Async
    public void enviarCorreoRestablecimiento(String to, String token) {
        try {
            // La URL debe apuntar a la página de tu frontend en Angular
            String resetUrl = "http://localhost:4200/reset-password?token=" + token;
            
            String subject = "Restablecimiento de Contraseña - ReservApp";
            String text = "Hola,\n\n"
                        + "Has solicitado restablecer tu contraseña.\n\n"
                        + "Haz clic en el siguiente enlace para crear una nueva:\n" + resetUrl + "\n\n"
                        + "Si no solicitaste este cambio, puedes ignorar este correo de forma segura.\n\n"
                        + "Gracias,\nEl equipo de ReservApp";

            // Usamos tu método existente para mantener la consistencia
            enviarNotificacionSimple(to, subject, text);

        } catch (Exception e) {
            System.err.println("Error al enviar el correo de restablecimiento: " + e.getMessage());
        }
    }
}