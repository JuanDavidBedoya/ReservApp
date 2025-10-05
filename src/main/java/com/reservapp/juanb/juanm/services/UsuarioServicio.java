package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Usuario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.repositories.UsuarioRepositorio;

@Service
public class UsuarioServicio {

    private UsuarioRepositorio usuarioRepositorio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }

    public Optional<Usuario> findById(String cedula) {
        return usuarioRepositorio.findById(cedula);
    }

    public Usuario save(Usuario usuario) {
        try {
            return usuarioRepositorio.save(usuario);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public void delete(String cedula) {
        try {
            usuarioRepositorio.deleteById(cedula);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    public Usuario update(String cedula, Usuario usuario) {
        // Verificar que existe
        if (!usuarioRepositorio.existsById(cedula)) {
            throw new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula);
        }
        
        usuario.setCedula(cedula);
        return save(usuario);
    }

    // Métodos adicionales para validaciones
    public boolean existsByCorreo(String correo) {
        return usuarioRepositorio.existsByCorreo(correo);
    }

    public boolean existsByCorreoAndCedulaNot(String correo, String cedula) {
        return usuarioRepositorio.existsByCorreoAndCedulaNot(correo, cedula);
    }
}