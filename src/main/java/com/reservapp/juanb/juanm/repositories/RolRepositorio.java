package com.reservapp.juanb.juanm.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Rol;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, UUID>{

    boolean existsByNombre(String nombre);
    
    // Verificar si existe otro rol con el mismo nombre (excluyendo el actual)
    @Query("SELECT COUNT(r) > 0 FROM Rol r WHERE r.nombre = :nombre AND r.idRol != :id")
    boolean existsByNombreAndIdRolNot(@Param("nombre") String nombre, @Param("id") UUID id);

}
