package com.reservapp.juanb.juanm.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Estado;

@Repository
public interface EstadoRepositorio extends JpaRepository<Estado, UUID>{

    boolean existsByNombre(String nombre);
    
    @Query("SELECT COUNT(e) > 0 FROM Estado e WHERE e.nombre = :nombre AND e.idEstado != :id")
    boolean existsByNombreAndIdEstadoNot(@Param("nombre") String nombre, @Param("id") UUID id);

}
