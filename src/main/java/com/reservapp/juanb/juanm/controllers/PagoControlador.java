package com.reservapp.juanb.juanm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.dto.PagoResponseDTO;
import com.reservapp.juanb.juanm.services.PagoServicio;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/pagos")
public class PagoControlador {

    private final PagoServicio pagoServicio;
    
    public PagoControlador(PagoServicio pagoServicio) {
        this.pagoServicio = pagoServicio;
    }

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> getAll() {
        List<PagoResponseDTO> pagos = pagoServicio.findAll();
        if (pagos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PagoResponseDTO> getById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(pagoServicio.findById(uuid));
    }

    @PostMapping("/pagar")
    public ResponseEntity<PagoResponseDTO> pagarReserva(
            @RequestParam UUID idReserva,
            @RequestParam double monto,
            @RequestParam UUID idMetodo) {
        return ResponseEntity.ok(pagoServicio.pagarReserva(idReserva, monto, idMetodo));
    }
}