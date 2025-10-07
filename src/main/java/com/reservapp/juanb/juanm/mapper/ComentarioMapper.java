package com.reservapp.juanb.juanm.mapper;

import java.sql.Date;

import org.springframework.stereotype.Component;

import com.reservapp.juanb.juanm.dto.ComentarioRequestDTO;
import com.reservapp.juanb.juanm.dto.ComentarioResponseDTO;
import com.reservapp.juanb.juanm.entities.Comentario;
import com.reservapp.juanb.juanm.entities.Usuario;

@Component
public class ComentarioMapper {

    // Convierte entidad -> DTO de respuesta
    public ComentarioResponseDTO toResponseDTO(Comentario comentario) {
        if (comentario == null) return null;

        String nombreUsuario = (comentario.getUsuario() != null) ? comentario.getUsuario().getNombre() : null;

        return new ComentarioResponseDTO(
                comentario.getIdComentario(),
                comentario.getPuntuacion(),
                comentario.getMensaje(),
                comentario.getFechaComentario(),
                nombreUsuario
        );
    }

    // Convierte DTO de creación/actualización -> entidad
    public Comentario fromRequestDTO(ComentarioRequestDTO dto, Usuario usuario) {
        if (dto == null) return null;

        Comentario comentario = new Comentario();
        comentario.setPuntuacion(dto.puntuacion());
        comentario.setMensaje(dto.mensaje());
        comentario.setFechaComentario(new Date(0)); // se asigna fecha actual
        comentario.setUsuario(usuario);
        return comentario;
    }
}
