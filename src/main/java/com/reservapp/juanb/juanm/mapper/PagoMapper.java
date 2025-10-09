package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;

import com.reservapp.juanb.juanm.dto.PagoRequestDTO;
import com.reservapp.juanb.juanm.dto.PagoResponseDTO;
import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Metodo;
import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.entities.Reserva;

@Component
public class PagoMapper {

    // Convierte entidad → DTO de respuesta
    public PagoResponseDTO toResponseDTO(Pago pago) {
        if (pago == null) return null;
        return new PagoResponseDTO(
            pago.getIdPago(),
            pago.getMonto(),
            pago.getFechaPago(),
            pago.getMetodo() != null ? pago.getMetodo().getNombre() : null,
            pago.getEstado() != null ? pago.getEstado().getNombre() : null,
            pago.getReserva() != null ? pago.getReserva().getIdReserva() : null
        );
    }

    // Convierte DTO de request → entidad
    public Pago fromRequestDTO(PagoRequestDTO dto, Metodo metodo, Estado estado, Reserva reserva) {
        Pago pago = new Pago();
        pago.setMonto(dto.monto());
        pago.setMetodo(metodo);
        pago.setEstado(estado);
        pago.setReserva(reserva);
        return pago;
    }
}
