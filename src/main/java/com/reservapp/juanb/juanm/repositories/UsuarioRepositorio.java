package com.reservapp.juanb.juanm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Rol;
import com.reservapp.juanb.juanm.entities.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{

    //Métods adicionales usados en el Service

    boolean existsByCorreo(String correo);
    
    // Verificar si existe otro usuario con el mismo correo (excluyendo el actual)
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.correo = :correo AND u.cedula != :cedula")
    boolean existsByCorreoAndCedulaNot(@Param("correo") String correo, @Param("cedula") String cedula);

    Optional<Rol> findByNombre(String nombre);

    Optional<Usuario> findByCedula(String cedula);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByResetPasswordToken(String token);

}
