package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Reserva;
import com.reservapp.juanb.juanm.repositories.ReservaRepositorio;

@Service
public class ReservaServicio {

    private ReservaRepositorio reservaRepositorio;

    public ReservaServicio(ReservaRepositorio reservaRepositorio) {
        this.reservaRepositorio = reservaRepositorio;
    }

    public List<Reserva> findAll() {
        return reservaRepositorio.findAll();
    }

    public Optional<Reserva> findById(java.util.UUID uuid) {
        return reservaRepositorio.findById(uuid);
    }

    public Reserva save(Reserva reserva) {
        return reservaRepositorio.save(reserva);
    }

    public void delete(java.util.UUID uuid) {
        reservaRepositorio.deleteById(uuid);
    }

    public Reserva update(java.util.UUID uuid, Reserva reserva) {
        reserva.setIdReserva(uuid);
        return reservaRepositorio.save(reserva);
    }
}
