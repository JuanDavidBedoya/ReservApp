package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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

@Service
public class PagoServicio {

    private PagoRepositorio pagoRepositorio;
    private MetodoRepositorio metodoRepositorio;
    private EstadoRepositorio estadoRepositorio;
    private ReservaRepositorio reservaRepositorio;
    private PagoMapper pagoMapper;

    public PagoServicio(PagoRepositorio pagoRepositorio, MetodoRepositorio metodoRepositorio, EstadoRepositorio estadoRepositorio, ReservaRepositorio reservaRepositorio, PagoMapper pagoMapper) {
        this.pagoRepositorio = pagoRepositorio;
        this.metodoRepositorio = metodoRepositorio;
        this.estadoRepositorio = estadoRepositorio;
        this.reservaRepositorio = reservaRepositorio;
        this.pagoMapper = pagoMapper;
    }

    //GET ALL
    public List<PagoResponseDTO> findAll() {
        return pagoRepositorio.findAll()
                .stream()
                .map(pagoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    //GET BY ID
    public PagoResponseDTO findById(UUID uuid) {
        Pago pago = pagoRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + uuid));
        return pagoMapper.toResponseDTO(pago);
    }

    //POST
    public PagoResponseDTO save(PagoRequestDTO dto) {
        Metodo metodo = metodoRepositorio.findById(dto.idMetodo())
                .orElseThrow(() -> new ResourceNotFoundException("Método no encontrado"));
        Estado estado = estadoRepositorio.findById(dto.idEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));
        Reserva reserva = reservaRepositorio.findById(dto.idReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        //Validar que la reserva no tenga ya un pago
        if (pagoRepositorio.existsByReserva(reserva)) {
            throw new BadRequestException("Esta reserva ya tiene un pago asociado");
        }

        Pago pago = pagoMapper.fromRequestDTO(dto, metodo, estado, reserva);

        try {
            Pago guardado = pagoRepositorio.save(pago);
            return pagoMapper.toResponseDTO(guardado);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el pago: " + e.getMessage());
        }
    }

    //PUT
    public PagoResponseDTO update(UUID uuid, PagoRequestDTO dto) {
        if (!pagoRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + uuid);
        }

        Metodo metodo = metodoRepositorio.findById(dto.idMetodo())
                .orElseThrow(() -> new ResourceNotFoundException("Método no encontrado"));
        Estado estado = estadoRepositorio.findById(dto.idEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado no encontrado"));
        Reserva reserva = reservaRepositorio.findById(dto.idReserva())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        Pago pago = pagoMapper.fromRequestDTO(dto, metodo, estado, reserva);
        pago.setIdPago(uuid);

        Pago actualizado = pagoRepositorio.save(pago);
        return pagoMapper.toResponseDTO(actualizado);
    }

    //DELETE
    public void delete(UUID uuid) {
        if (!pagoRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + uuid);
        }
        try {
            pagoRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el pago: " + e.getMessage());
        }
    }
}