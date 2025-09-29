package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Usuario;
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
        return usuarioRepositorio.save(usuario);
    }

    public void delete(String cedula) {
        usuarioRepositorio.deleteById(cedula);
    }

    public Usuario update(String cedula, Usuario usuario) {
        usuario.setCedula(cedula);
        return usuarioRepositorio.save(usuario);
    }
}
