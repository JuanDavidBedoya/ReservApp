package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.repositories.PagoRepositorio;

@Service
public class PagoServicio {

    private PagoRepositorio pagoRepositorio;

    public PagoServicio(PagoRepositorio pagoRepositorio) {
        this.pagoRepositorio = pagoRepositorio;
    }

    public List<Pago> findAll() {
        return pagoRepositorio.findAll();
    }

    public Optional<Pago> findById(java.util.UUID uuid) {
        return pagoRepositorio.findById(uuid);
    }

    public Pago save(Pago pago) {
        return pagoRepositorio.save(pago);
    }

    public void delete(java.util.UUID uuid) {
        pagoRepositorio.deleteById(uuid);
    }

    public Pago update(java.util.UUID uuid, Pago pago) {
        pago.setIdPago(uuid);
        return pagoRepositorio.save(pago);
    }
}
