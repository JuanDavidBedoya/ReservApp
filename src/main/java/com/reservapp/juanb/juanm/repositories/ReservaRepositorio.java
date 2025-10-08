package com.reservapp.juanb.juanm.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.entities.Reserva;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, UUID>{

    //Métods adicionales usados en el Service
    List<Reserva> findByMesaAndFecha(Mesa mesa, LocalDate fecha);

}

