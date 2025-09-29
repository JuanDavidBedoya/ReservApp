package com.reservapp.juanb.juanm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{

}
