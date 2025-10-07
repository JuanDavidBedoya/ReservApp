package com.reservapp.juanb.juanm.services;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.reservapp.juanb.juanm.dto.ComentarioRequestDTO;
import com.reservapp.juanb.juanm.dto.ComentarioResponseDTO;
import com.reservapp.juanb.juanm.entities.Comentario;
import com.reservapp.juanb.juanm.entities.Usuario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.ComentarioMapper;
import com.reservapp.juanb.juanm.repositories.ComentarioRepositorio;
import com.reservapp.juanb.juanm.repositories.UsuarioRepositorio;

@Service
public class ComentarioServicio {

    private ComentarioRepositorio comentarioRepositorio;
    private UsuarioRepositorio usuarioRepositorio;
    private ComentarioMapper comentarioMapper;

    public ComentarioServicio(ComentarioRepositorio comentarioRepositorio, UsuarioRepositorio usuarioRepositorio, ComentarioMapper comentarioMapper) {
        this.comentarioRepositorio = comentarioRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.comentarioMapper = comentarioMapper;
    }

    public List<ComentarioResponseDTO> findAll() {
        return comentarioRepositorio.findAll()
                .stream()
                .map(comentarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ComentarioResponseDTO findById(UUID uuid) {
        Comentario comentario = comentarioRepositorio.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario no encontrado con ID: " + uuid));
        return comentarioMapper.toResponseDTO(comentario);
    }

    public ComentarioResponseDTO save(ComentarioRequestDTO dto) {
        Usuario usuario = usuarioRepositorio.findById(dto.idUsuario())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado con cédula: " + dto.idUsuario()));

        if (dto.puntuacion() < 1 || dto.puntuacion() > 5) {
            throw new BadRequestException("La puntuación debe estar entre 1 y 5");
        }

        Comentario comentario = comentarioMapper.fromRequestDTO(dto, usuario);
        comentario.setFechaComentario(new Date(System.currentTimeMillis()));

        try {
            Comentario saved = comentarioRepositorio.save(comentario);
            return comentarioMapper.toResponseDTO(saved);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el comentario: " + e.getMessage());
        }
    }

    public ComentarioResponseDTO update(UUID uuid, ComentarioRequestDTO dto) {
        if (!comentarioRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + uuid);
        }

        Usuario usuario = usuarioRepositorio.findById(dto.idUsuario())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado con cédula: " + dto.idUsuario()));

        Comentario comentario = comentarioMapper.fromRequestDTO(dto, usuario);
        comentario.setIdComentario(uuid);

        try {
            Comentario updated = comentarioRepositorio.save(comentario);
            return comentarioMapper.toResponseDTO(updated);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al actualizar el comentario: " + e.getMessage());
        }
    }

    public void delete(UUID uuid) {
        if (!comentarioRepositorio.existsById(uuid)) {
            throw new ResourceNotFoundException("Comentario no encontrado con ID: " + uuid);
        }
        try {
            comentarioRepositorio.deleteById(uuid);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al eliminar el comentario: " + e.getMessage());
        }
    }
}