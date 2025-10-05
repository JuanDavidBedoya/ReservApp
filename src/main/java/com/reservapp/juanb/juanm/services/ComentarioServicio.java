package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.entities.Comentario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
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

    public Optional<Comentario> findById(UUID uuid) {
        return comentarioRepositorio.findById(uuid);
    }

    public Comentario save(Comentario comentario) {
        try {
            return comentarioRepositorio.save(comentario);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el comentario: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        try {
            comentarioRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el comentario: " + e.getMessage());
        }
    }

    public Comentario update(UUID uuid, Comentario comentario) {
        // Verificar que existe
        if (!comentarioRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + uuid);
        }
        
        comentario.setIdComentario(uuid);
        return save(comentario);
    }
}