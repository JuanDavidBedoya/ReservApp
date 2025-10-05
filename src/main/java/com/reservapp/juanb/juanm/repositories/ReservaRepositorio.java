package com.reservapp.juanb.juanm.repositories;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Reserva;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, UUID>{

    // Método para contar reservas en una fecha específica (para validar aforo)
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.fecha = :fecha")
    int countByFecha(@Param("fecha") Date fecha);
    
    // Método para contar reservas de una mesa en una fecha específica
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.mesa.idMesa = :mesaId AND r.fecha = :fecha")
    int countByMesaAndFecha(@Param("mesaId") UUID mesaId, @Param("fecha") Date fecha);
}

