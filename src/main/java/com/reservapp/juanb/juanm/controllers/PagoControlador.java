package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.PagoServicio;

import java.util.List;
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
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(list); // 200 OK
    }
    
    @GetMapping("/{uuid}")
    public ResponseEntity<Pago> getById(@PathVariable("uuid") UUID uuid){
        Pago pago = pagoServicio.findById(uuid)
            .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + uuid));
        return ResponseEntity.ok(pago); // 200 OK
    }

    @PostMapping
    public ResponseEntity<Pago> save(@RequestBody Pago pago) {
        // Validaciones básicas
        if (pago.getMonto() <= 0) {
            throw new BadRequestException("El monto debe ser mayor a 0");
        }
        if (pago.getFechaPago() == null) {
            throw new BadRequestException("La fecha de pago no puede estar vacía");
        }
        if (pago.getMetodo() == null) {
            throw new BadRequestException("El pago debe tener un método asignado");
        }
        if (pago.getEstado() == null) {
            throw new BadRequestException("El pago debe tener un estado asignado");
        }
        
        Pago nuevoPago = pagoServicio.save(pago);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago); // 201 Created
    }
    
    @PutMapping("/{uuid}")
    public ResponseEntity<Pago> update(@PathVariable("uuid") UUID uuid, @RequestBody Pago pago) {
        // Verificar que existe
        if (!pagoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + uuid);
        }
        
        // Validaciones básicas
        if (pago.getMonto() <= 0) {
            throw new BadRequestException("El monto debe ser mayor a 0");
        }
        if (pago.getFechaPago() == null) {
            throw new BadRequestException("La fecha de pago no puede estar vacía");
        }
        if (pago.getMetodo() == null) {
            throw new BadRequestException("El pago debe tener un método asignado");
        }
        if (pago.getEstado() == null) {
            throw new BadRequestException("El pago debe tener un estado asignado");
        }
        
        Pago actualizarPago = pagoServicio.update(uuid, pago);
        return ResponseEntity.ok(actualizarPago); // 200 OK
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> delete(@PathVariable("uuid") UUID uuid) {
        // Verificar que existe
        if (!pagoServicio.findById(uuid).isPresent()) {
            throw new ResourceNotFoundException("Pago no encontrado con ID: " + uuid);
        }
        
        pagoServicio.delete(uuid);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}