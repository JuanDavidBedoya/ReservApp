package com.reservapp.juanb.juanm.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.entities.Reserva;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, UUID>{

    List<Reserva> findByMesaAndFecha(Mesa mesa, LocalDate fecha);

    @Query("SELECT r FROM Reserva r WHERE r.estado.nombre = 'Confirmada' AND " +
           "FUNCTION('CONCAT', r.fecha, 'T', r.hora) BETWEEN :inicio AND :fin")
    List<Reserva> findReservasActivasEnRango(LocalDateTime inicio, LocalDateTime fin);

    // Busca reservas que ya terminaron para enviar la encuesta
    @Query("SELECT r FROM Reserva r WHERE r.estado.nombre = 'Confirmada' AND " +
           "r.encuestaEnviada = false AND " +
           "FUNCTION('CONCAT', r.fecha, 'T', r.hora) < :momentoFinalizado")
    List<Reserva> findReservasFinalizadasParaEncuesta(LocalDateTime momentoFinalizado);
}

