package com.reservapp.juanb.juanm.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Tipo;

@Repository
public interface TipoRepositorio extends JpaRepository<Tipo, UUID>{

    boolean existsByNombre(String nombre);
    Optional<Tipo> findByNombre(String nombre);

}
