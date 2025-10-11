package com.reservapp.juanb.juanm.repositories;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reservapp.juanb.juanm.entities.Comentario;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, UUID>{

    @Query("SELECT AVG(c.puntuacion) FROM Comentario c WHERE c.fechaComentario BETWEEN :fechaInicio AND :fechaFin")
    Double findPromedioSemanal(@Param("fechaInicio") Date fechaInicio, 
                              @Param("fechaFin") Date fechaFin);
    
    @Query("SELECT COUNT(c) FROM Comentario c WHERE c.fechaComentario BETWEEN :fechaInicio AND :fechaFin")
    Integer countComentariosSemana(@Param("fechaInicio") Date fechaInicio, 
                                  @Param("fechaFin") Date fechaFin);

}
