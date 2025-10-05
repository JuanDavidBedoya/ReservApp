package com.reservapp.juanb.juanm.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Mesa;

@Repository
public interface MesaRepositorio extends JpaRepository<Mesa, UUID>{

    // Verificar si existe una mesa con el mismo número
    boolean existsByNumeroMesa(int numeroMesa);
    
    // Verificar si existe otra mesa con el mismo número (excluyendo la actual)
    @Query("SELECT COUNT(m) > 0 FROM Mesa m WHERE m.numeroMesa = :numeroMesa AND m.idMesa != :id")
    boolean existsByNumeroMesaAndIdMesaNot(@Param("numeroMesa") int numeroMesa, @Param("id") UUID id);
    
}
