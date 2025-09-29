package com.reservapp.juanb.juanm.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Rol;

@Repository
public interface RolRepositorio extends JpaRepository<Rol, UUID>{

}
