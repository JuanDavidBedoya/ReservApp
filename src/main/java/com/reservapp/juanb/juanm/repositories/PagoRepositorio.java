package com.reservapp.juanb.juanm.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Pago;
import com.reservapp.juanb.juanm.entities.Reserva;

@Repository
public interface PagoRepositorio extends JpaRepository<Pago, UUID>{
    
    boolean existsByReserva(Reserva reserva);
    
}
