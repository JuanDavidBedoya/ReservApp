package com.reservapp.juanb.juanm.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.reservapp.juanb.juanm.entities.Estado;

@Repository
public interface EstadoRepositorio extends JpaRepository<Estado, UUID> {

    Optional<Estado> findByNombre(String nombre);
}