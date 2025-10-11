package com.reservapp.juanb.juanm.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class UsuarioServicio implements UserDetailsService {

    private UsuarioRepositorio usuarioRepositorio;
    private RolRepositorio rolRepositorio;
    private UsuarioMapper usuarioMapper;
    private EmailServicio emailServicio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, RolRepositorio rolRepositorio, UsuarioMapper usuarioMapper, EmailServicio emailServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.rolRepositorio = rolRepositorio;
        this.usuarioMapper = usuarioMapper;
        this.emailServicio = emailServicio;
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
                .orElseThrow(
                        () -> new ResourceNotFoundException("El rol 'Cliente' no se encuentra en la base de datos."));

        Usuario usuario = new Usuario(
                usuarioDTO.cedula(),
                usuarioDTO.nombre(),
                usuarioDTO.correo(),
                usuarioDTO.contrasena(),
                usuarioDTO.telefono(),
                rolCliente // De momento, se asigna el Rol de Cliente por defecto
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
            throw new ResourceAlreadyExistsException(
                    "El correo " + usuarioDTO.correo() + " ya está en uso por otro usuario.");
        }

        usuarioExistente.setNombre(usuarioDTO.nombre());
        usuarioExistente.setCorreo(usuarioDTO.correo());
        usuarioExistente.setTelefono(usuarioDTO.telefono());

        if (usuarioDTO.contrasena() != null && !usuarioDTO.contrasena().trim().isEmpty()) {
            usuarioExistente.setContrasena(usuarioDTO.contrasena());
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

    // Método de la implementación del Loggin
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("--- INTENTO DE LOGIN: Buscando usuario con cédula: " + username + " ---");

        Usuario usuario = usuarioRepositorio.findByCedula(username)
                .orElseThrow(() -> {
                    System.err.println("--- LOGIN FALLIDO: Usuario no encontrado en la base de datos. ---");
                    return new UsernameNotFoundException("Usuario no encontrado con cédula: " + username);
                });

        System.out.println("--- LOGIN EXITOSO: Usuario encontrado: " + usuario.getNombre() + " con rol: "
                + usuario.getRol().getNombre() + " ---");
        return usuario;
    }

    // --- NUEVOS MÉTODOS PARA RECUPERACIÓN DE CONTRASEÑA ---

    /**
     * Inicia el proceso de restablecimiento de contraseña.
     * Genera un token y envía un correo al usuario.
     */
    public void forgotPassword(String correo) {
        // Buscamos al usuario por su correo.
        usuarioRepositorio.findByCorreo(correo).ifPresent(usuario -> {
            // Generamos un token único.
            String token = UUID.randomUUID().toString();
            usuario.setResetPasswordToken(token);
            // El token será válido por 1 hora.
            usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1));
            usuarioRepositorio.save(usuario);

            // Usamos el EmailServicio para enviar el correo.
            emailServicio.enviarCorreoRestablecimiento(usuario.getCorreo(), token);
        });
        // IMPORTANTE: No se lanza error si no se encuentra el correo.
        // Esto es una medida de seguridad para evitar que atacantes adivinen qué
        // correos están registrados.
    }

    /**
     * Restablece la contraseña del usuario usando el token.
     */
    public void resetPassword(String token, String nuevaContrasena) {
        Usuario usuario = usuarioRepositorio.findByResetPasswordToken(token)
                .orElseThrow(() -> new BadRequestException("Token inválido o expirado."));

        // Verificamos que el token no haya expirado.
        if (usuario.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("El token ha expirado.");
        }

        // Establecemos la nueva contraseña (sin encriptar).
        usuario.setContrasena(nuevaContrasena);

        // Limpiamos el token para que no pueda ser reutilizado.
        usuario.setResetPasswordToken(null);
        usuario.setResetPasswordTokenExpiry(null);

        usuarioRepositorio.save(usuario);
    }

}