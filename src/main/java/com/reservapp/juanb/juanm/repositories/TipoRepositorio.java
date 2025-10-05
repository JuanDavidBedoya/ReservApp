package com.reservapp.juanb.juanm.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Tipo;

@Repository
public interface TipoRepositorio extends JpaRepository<Tipo, UUID>{

    boolean existsByNombre(String nombre);
    
    // Verificar si existe otro tipo con el mismo nombre (excluyendo el actual)
    @Query("SELECT COUNT(t) > 0 FROM Tipo t WHERE t.nombre = :nombre AND t.idTipo != :id")
    boolean existsByNombreAndIdTipoNot(@Param("nombre") String nombre, @Param("id") UUID id);

}
