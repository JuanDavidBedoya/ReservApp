// Ubicación: com/reservapp/juanb/juanm/mapper/UsuarioMapper.java
package com.reservapp.juanb.juanm.mapper;

import org.springframework.stereotype.Component;
import com.reservapp.juanb.juanm.dto.UsuarioResponseDTO;
import com.reservapp.juanb.juanm.entities.Usuario;

@Component
public class UsuarioMapper {

    //Convierte una entidad Usuario a un DTO de respuesta.
    
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        String nombreRol = (usuario.getRol() != null) ? usuario.getRol().getNombre() : null;
        return new UsuarioResponseDTO(
                usuario.getCedula(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                nombreRol
        );
    }
}