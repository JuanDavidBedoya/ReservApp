// Ubicación: com/reservapp/juanb/juanm/services/UsuarioServicio.java
package com.reservapp.juanb.juanm.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.reservapp.juanb.juanm.dto.UsuarioCreateDTO;
import com.reservapp.juanb.juanm.dto.UsuarioResponseDTO;
import com.reservapp.juanb.juanm.dto.UsuarioUpdateDTO;
import com.reservapp.juanb.juanm.entities.Rol;
import com.reservapp.juanb.juanm.entities.Usuario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.mapper.UsuarioMapper;
import com.reservapp.juanb.juanm.repositories.RolRepositorio;
import com.reservapp.juanb.juanm.repositories.UsuarioRepositorio;

@Service
public class UsuarioServicio {

    private UsuarioRepositorio usuarioRepositorio;
    private RolRepositorio rolRepositorio;
    private UsuarioMapper usuarioMapper;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, RolRepositorio rolRepositorio, UsuarioMapper usuarioMapper) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.rolRepositorio = rolRepositorio;
        this.usuarioMapper = usuarioMapper;
    }

    public List<UsuarioResponseDTO> findAll() {
        return usuarioRepositorio.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO findById(String cedula) {
        Usuario usuario = usuarioRepositorio.findById(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula));
        return usuarioMapper.toResponseDTO(usuario);
    }

    public UsuarioResponseDTO save(UsuarioCreateDTO usuarioDTO) {
        Rol rolCliente = rolRepositorio.findByNombre("Cliente")
                .orElseThrow(() -> new ResourceNotFoundException("El rol 'Cliente' no se encuentra en la base de datos."));
        
        Usuario usuario = new Usuario(
                usuarioDTO.cedula(),
                usuarioDTO.nombre(),
                usuarioDTO.correo(),
                usuarioDTO.contrasena(), 
                usuarioDTO.telefono(),
                rolCliente // Asignamos el rol por defecto
        );

        try {
            Usuario nuevoUsuario = usuarioRepositorio.save(usuario);
            return usuarioMapper.toResponseDTO(nuevoUsuario);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    public UsuarioResponseDTO update(String cedula, UsuarioUpdateDTO usuarioDTO) {
  
        Usuario usuarioExistente = usuarioRepositorio.findById(cedula)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula));

        if (usuarioRepositorio.existsByCorreoAndCedulaNot(usuarioDTO.correo(), cedula)) {
            throw new ResourceAlreadyExistsException("El correo " + usuarioDTO.correo() + " ya está en uso por otro usuario.");
        }

        usuarioExistente.setNombre(usuarioDTO.nombre());
        usuarioExistente.setCorreo(usuarioDTO.correo());
        usuarioExistente.setTelefono(usuarioDTO.telefono());

        if (!usuarioExistente.getRol().getIdRol().equals(usuarioDTO.idRol())) {
            Rol nuevoRol = rolRepositorio.findById(usuarioDTO.idRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + usuarioDTO.idRol()));
            usuarioExistente.setRol(nuevoRol);
        }

        Usuario usuarioActualizado = usuarioRepositorio.save(usuarioExistente);
        return usuarioMapper.toResponseDTO(usuarioActualizado);
    }   

    public void delete(String cedula) {
        if (!usuarioRepositorio.existsById(cedula)) {
             throw new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula);
        }
        usuarioRepositorio.deleteById(cedula);
    }

    // Métodos para validaciones que se usan en el controlador
    public boolean existsById(String cedula) {
        return usuarioRepositorio.existsById(cedula);
    }
    
    public boolean existsByCorreo(String correo) {
        return usuarioRepositorio.existsByCorreo(correo);
    }

    public boolean existsByCorreoAndCedulaNot(String correo, String cedula) {
        return usuarioRepositorio.existsByCorreoAndCedulaNot(correo, cedula);
    }
}