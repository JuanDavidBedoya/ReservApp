package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Comentario;
import com.reservapp.juanb.juanm.repositories.ComentarioRepositorio;

@Service
public class ComentarioServicio {

    private ComentarioRepositorio comentarioRepositorio;

    public ComentarioServicio(ComentarioRepositorio comentarioRepositorio) {
        this.comentarioRepositorio = comentarioRepositorio;
    }

    public List<Comentario> findAll() {
        return comentarioRepositorio.findAll();
    }

    public Optional<Comentario> findById(java.util.UUID uuid) {
        return comentarioRepositorio.findById(uuid);
    }

    public Comentario save(Comentario comentario) {
        return comentarioRepositorio.save(comentario);
    }

    public void delete(java.util.UUID uuid) {
        comentarioRepositorio.deleteById(uuid);
    }

    public Comentario update(java.util.UUID uuid, Comentario comentario) {
        comentario.setIdComentario(uuid);
        return comentarioRepositorio.save(comentario);
    }
}
