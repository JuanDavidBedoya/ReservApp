package com.reservapp.juanb.juanm.services;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.reservapp.juanb.juanm.dto.PagoRequestDTO;
import com.reservapp.juanb.juanm.dto.PagoResponseDTO;
import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Metodo;
import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.PagoMapper;
import com.reservapp.juanb.juanm.repositories.EstadoRepositorio;
import com.reservapp.juanb.juanm.repositories.MetodoRepositorio;
import com.reservapp.juanb.juanm.repositories.PagoRepositorio;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;

import jakarta.annotation.Nullable;

@Service
public class PagoServicio {

    private final PagoRepositorio pagoRepositorio;
    private final MetodoRepositorio metodoRepositorio;
    private final EstadoRepositorio estadoRepositorio;
    private final ReservaRepositorio reservaRepositorio;
    private final EmailServicio emailServicio;
    private final NotificacionServicio notificacionServicio;
    private final PagoMapper pagoMapper;

    public PagoServicio(PagoRepositorio pagoRepositorio, MetodoRepositorio metodoRepositorio, EstadoRepositorio estadoRepositorio, ReservaRepositorio reservaRepositorio,EmailServicio emailServicio, NotificacionServicio notificacionServicio, PagoMapper pagoMapper) {
        this.pagoRepositorio = pagoRepositorio;
        this.metodoRepositorio = metodoRepositorio;
        this.estadoRepositorio = estadoRepositorio;
        this.reservaRepositorio = reservaRepositorio;
        this.emailServicio = emailServicio;
        this.notificacionServicio = notificacionServicio;
        this.pagoMapper = pagoMapper;
    }

    public List<PagoResponseDTO> findAll() {
        return pagoRepositorio.findAll()
                .stream()
                .map(pagoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PagoResponseDTO findById(UUID uuid) {
        Pago pago = pagoRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + uuid));
        return pagoMapper.toResponseDTO(pago);
    }

    public PagoResponseDTO pagarReserva(PagoRequestDTO pagoRequest) {
       // Validar tarjeta con algoritmo de Luhn
        if (!validarTarjetaPorLuhn(pagoRequest.numeroTarjeta())) {
            throw new BadRequestException("El número de tarjeta no es válido");
        }

        // Validar fecha de expiración
        if (!validarFechaExpiracion(pagoRequest.fechaExpiracion())) {
            throw new BadRequestException("La fecha de expiración no es válida o la tarjeta está vencida");
        }

        // Buscar la reserva
        Reserva reserva = reservaRepositorio.findById(pagoRequest.idReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        // Validar que no tenga ya un pago
        if (pagoRepositorio.existsByReserva(reserva)) {
            throw new BadRequestException("Esta reserva ya tiene un pago asociado");
        }

        // Validar hora límite (1 hora antes del inicio)
        LocalDate fecha = reserva.getFecha();
        LocalTime hora = reserva.getHora();
        LocalDateTime fechaHoraReserva = LocalDateTime.of(fecha, hora);
        LocalDateTime ahora = LocalDateTime.now();

        if (!ahora.isBefore(fechaHoraReserva.minusHours(1))) {
            // Cambiar reserva a "Cancelada"
            Estado estadoCancelada = estadoRepositorio.findByNombre("Cancelada")
                    .orElseThrow(() -> new ResourceNotFoundException("Estado 'Cancelada' no encontrado"));
            reserva.setEstado(estadoCancelada);
            reservaRepositorio.save(reserva);

            enviarNotificacionConAdjunto(reserva, "cancelada_pago", null);

            throw new BadRequestException("La reserva fue cancelada por no pagarse al menos 1 hora antes del inicio");
        }

        // Buscar método de pago
        Metodo metodo = metodoRepositorio.findById(pagoRequest.idMetodo())
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago no encontrado"));

        // Simular proceso de pago con tarjeta
        boolean pagoExitoso = simularProcesamientoTarjeta();

        // Determinar estados según el resultado
        Estado estadoPago = pagoExitoso ? 
            estadoRepositorio.findByNombre("Confirmado")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Confirmado' no encontrado")) :
            estadoRepositorio.findByNombre("Rechazado")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Rechazado' no encontrado"));

        Estado estadoReserva = pagoExitoso ? 
            estadoRepositorio.findByNombre("Pagada")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'Pagada' no encontrado")) :
            reserva.getEstado(); // Mantener el estado actual si el pago falla

        // Crear y guardar el pago
        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setMonto(pagoRequest.monto());
        pago.setMetodo(metodo);
        pago.setEstado(estadoPago);
        pago.setFechaPago(new Date(0)); // Fecha actual
        pagoRepositorio.save(pago);

        // Actualizar estado de la reserva si el pago fue exitoso
        if (pagoExitoso) {
            reserva.setEstado(estadoReserva);
            reservaRepositorio.save(reserva);
            enviarNotificacionConAdjunto(reserva, "pagada", pago);
        } else {
            enviarNotificacionConAdjunto(reserva, "pago_rechazado", pago);
        }

        return pagoMapper.toResponseDTO(pago);
    }

    private boolean simularProcesamientoTarjeta() {
        
        return true;
    }
    

    private void enviarNotificacionConAdjunto(Reserva reserva, String tipo, @Nullable Pago pago) {
        try {
            String asunto;
            String cuerpo;
            int numeroMesa = reserva.getMesa().getNumeroMesa();

            switch (tipo.toLowerCase()) {
                case "cancelada_pago" -> {
                    asunto = "Reserva Cancelada por falta de pago";
                    cuerpo = String.format(
                            "Hola %s,\n\nTu reserva ha sido cancelada automáticamente porque no fue pagada a tiempo:\n" +
                            "- Fecha: %s\n- Hora: %s\n\nPuedes realizar una nueva reserva cuando desees.",
                            reserva.getUsuario().getNombre(),
                            reserva.getFecha(),
                            reserva.getHora()
                    );
                    emailServicio.enviarNotificacionSimple(reserva.getUsuario().getCorreo(), asunto, cuerpo);
                    notificacionServicio.registrarNotificacion(reserva, "Cancelada", cuerpo);
                }
                case "pago_rechazado" -> {
                    asunto = "Pago Rechazado";
                    cuerpo = String.format(
                            "Hola %s,\n\nTu pago ha sido rechazado. Por favor revisa tu método de pago o intenta nuevamente.\n" +
                            "- Reserva: %s %s\n- Mesa N°: %d",
                            reserva.getUsuario().getNombre(),
                            reserva.getFecha(),
                            reserva.getHora(),
                            numeroMesa
                    );
                    emailServicio.enviarNotificacionSimple(reserva.getUsuario().getCorreo(), asunto, cuerpo);
                    notificacionServicio.registrarNotificacion(reserva, "Pago Rechazado", cuerpo);
                }
                case "pagada" -> {
                    asunto = "Pago Confirmado - Reserva Pagada";
                    cuerpo = String.format(
                            "Hola %s,\n\nTu pago ha sido confirmado exitosamente.\n" +
                            "- Reserva: %s %s\n- Mesa N°: %d\n- Monto: $%.2f\n\nAdjuntamos tu factura.\n\n¡Gracias por tu compra!",
                            reserva.getUsuario().getNombre(),
                            reserva.getFecha(),
                            reserva.getHora(),
                            numeroMesa,
                            pago.getMonto()
                    );

                    // Generar factura PDF
                    byte[] facturaPDF = generarFacturaPDF(reserva, pago);

                    // Enviar correo con adjunto
                    emailServicio.enviarNotificacionConAdjunto(
                            reserva.getUsuario().getCorreo(),
                            asunto,
                            cuerpo,
                            facturaPDF,
                            "factura_reserva.pdf"
                    );

                    notificacionServicio.registrarNotificacion(reserva, "Pagada", cuerpo);
                }
                default -> System.err.println("Tipo de notificación no reconocido: " + tipo);
            }
        } catch (Exception e) {
            System.err.println("Error al enviar notificación de " + tipo + ": " + e.getMessage());
        }
    }

    private byte[] generarFacturaPDF(Reserva reserva, Pago pago) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Factura de Pago"));
            document.add(new Paragraph("------------------------------"));
            document.add(new Paragraph("Cliente: " + reserva.getUsuario().getNombre()));
            document.add(new Paragraph("Correo: " + reserva.getUsuario().getCorreo()));
            document.add(new Paragraph("Fecha de Reserva: " + reserva.getFecha()));
            document.add(new Paragraph("Hora: " + reserva.getHora()));
            document.add(new Paragraph("Mesa N°: " + reserva.getMesa().getNumeroMesa()));
            document.add(new Paragraph("Monto: $" + pago.getMonto()));
            document.add(new Paragraph("Método: " + pago.getMetodo().getNombre()));
            document.add(new Paragraph("Estado del Pago: " + pago.getEstado().getNombre()));
            document.add(new Paragraph("------------------------------"));
            document.add(new Paragraph("¡Gracias por su pago!"));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la factura PDF: " + e.getMessage());
        }
    }

    public boolean validarTarjetaPorLuhn(String numeroTarjeta) {
        // Remover espacios y caracteres no numéricos
        String numeroLimpio = numeroTarjeta.replaceAll("[^0-9]", "");
        
        if (numeroLimpio.length() < 13 || numeroLimpio.length() > 19) {
            return false;
        }

        int suma = 0;
        boolean alternar = false;
        
        // Recorrer el número de derecha a izquierda
        for (int i = numeroLimpio.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(numeroLimpio.charAt(i));
            
            if (alternar) {
                digito *= 2;
                if (digito > 9) {
                    digito = (digito % 10) + 1;
                }
            }
            
            suma += digito;
            alternar = !alternar;
        }
        
        return (suma % 10) == 0;
    }

    public boolean validarFechaExpiracion(String fechaExpiracion) {
        try {
            String[] partes = fechaExpiracion.split("/");
            if (partes.length != 2) return false;
            
            int mes = Integer.parseInt(partes[0]);
            int año = Integer.parseInt(partes[1]);
            
            // Validar mes
            if (mes < 1 || mes > 12) return false;
            
            // Validar que no sea una fecha pasada
            Calendar ahora = Calendar.getInstance();
            int añoActual = ahora.get(Calendar.YEAR) % 100;
            int mesActual = ahora.get(Calendar.MONTH) + 1;
            
            if (año < añoActual) return false;
            if (año == añoActual && mes < mesActual) return false;
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String obtenerTipoTarjeta(String numeroTarjeta) {
        String numeroLimpio = numeroTarjeta.replaceAll("[^0-9]", "");
        
        if (numeroLimpio.startsWith("4")) {
            return "Visa";
        } else if (numeroLimpio.startsWith("5")) {
            return "Mastercard";
        } else if (numeroLimpio.startsWith("34") || numeroLimpio.startsWith("37")) {
            return "American Express";
        } else if (numeroLimpio.startsWith("6")) {
            return "Discover";
        } else {
            return "Desconocida";
        }
    }
}