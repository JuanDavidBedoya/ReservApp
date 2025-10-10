package com.reservapp.juanb.juanm.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.dto.ReservaResponseDTO;
import com.reservapp.juanb.juanm.entities.Mesa;
import com.reservapp.juanb.juanm.entities.Reserva;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, UUID>{

    //Métods adicionales usados en el Service
    List<Reserva> findByMesaAndFecha(Mesa mesa, LocalDate fecha);

    @Query("SELECT new com.reservapp.juanb.juanm.dto.ReservaResponseDTO(r.idReserva, r.fecha, r.hora, r.numeroPersonas, r.usuario.cedula, r.mesa.numeroMesa, r.estado.nombre) " +
           "FROM Reserva r WHERE r.usuario.cedula = :cedula")
    List<ReservaResponseDTO> findReservasByCedula(@Param("cedula") String cedula);
    
}

