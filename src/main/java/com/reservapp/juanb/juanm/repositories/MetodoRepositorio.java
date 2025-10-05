package com.reservapp.juanb.juanm.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Metodo;

@Repository
public interface MetodoRepositorio extends JpaRepository<Metodo, UUID>{

    boolean existsByNombre(String nombre);
    
    @Query("SELECT COUNT(m) > 0 FROM Metodo m WHERE m.nombre = :nombre AND m.idMetodo != :id")
    boolean existsByNombreAndIdMetodoNot(@Param("nombre") String nombre, @Param("id") UUID id);
}
