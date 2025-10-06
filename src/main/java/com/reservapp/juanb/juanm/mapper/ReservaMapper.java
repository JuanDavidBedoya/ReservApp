package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;

import com.reservapp.juanb.juanm.dto.ReservaRequestDTO;
import com.reservapp.juanb.juanm.dto.ReservaResponseDTO;
import com.reservapp.juanb.juanm.entities.Estado;
import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.entities.Usuario;

@Component
public class ReservaMapper {

    //Convierte entidad -> DTO de respuesta
    public ReservaResponseDTO toResponseDTO(Reserva reserva) {
        if (reserva == null) return null;

        return new ReservaResponseDTO(
                reserva.getIdReserva(),
                reserva.getFecha(),
                reserva.getHora(),
                reserva.getNumeroPersonas(),
                (reserva.getUsuario() != null) ? reserva.getUsuario().getCedula() : null,
                (reserva.getMesa() != null) ? reserva.getMesa().getIdMesa() : null,
                (reserva.getMesa() != null) ? reserva.getMesa().getEstado().getIdEstado() : null,
                (reserva.getEstado() != null) ? reserva.getEstado().getNombre() : null
        );
    }

    //Convierte DTO de creación/actualización -> entidad
    public Reserva fromRequestDTO(ReservaRequestDTO dto, Usuario usuario, Mesa mesa, Estado estado) {
        if (dto == null) return null;

        Reserva reserva = new Reserva();
        reserva.setFecha(dto.fecha());
        reserva.setHora(dto.hora());
        reserva.setNumeroPersonas(dto.numeroPersonas());
        reserva.setUsuario(usuario);
        reserva.setMesa(mesa);
        reserva.setEstado(estado);
        return reserva;
    }
}
