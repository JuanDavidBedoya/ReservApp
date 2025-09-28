package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.services.PagoServicio;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/pagos")
public class PagoControlador {

    private PagoServicio pagoServicio;

    public PagoControlador(PagoServicio pagoServicio) {
        this.pagoServicio = pagoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> getAll(){
        List<Pago> list = pagoServicio.findAll();
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/{uuid}")
    public Optional<Pago> getById(@PathVariable("uuid") UUID uuid){
        return pagoServicio.findById(uuid);
    }

    @PostMapping
    public ResponseEntity<Pago> save(@RequestBody Pago pago) {
        Pago nuevoPago = pagoServicio.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
    }
    
    @PutMapping("/{uuid}")
    public ResponseEntity<Pago> update(@PathVariable("uuid") UUID uuid, @RequestBody Pago pago) {
        Pago actualizarPago = pagoServicio.update(uuid, pago);
        return ResponseEntity.ok(actualizarPago);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        pagoServicio.delete(uuid);
        return ResponseEntity.noContent().build();
    }
}
