package com.reservapp.juanb.juanm.services;

import com.reservapp.juanb.juanm.dto.AuthResponseDTO;
import com.reservapp.juanb.juanm.dto.LoginRequestDTO;
import com.reservapp.juanb.juanm.dto.UsuarioResponseDTO;
import com.reservapp.juanb.juanm.entities.Usuario;
import com.reservapp.juanb.juanm.repositories.UsuarioRepositorio;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // --- Inyecciones necesarias ---
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepositorio usuarioRepository;
    private final JwtService jwtService; // Asumo que tienes un servicio para generar JWTs

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepositorio usuarioRepository,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    /**
     * Procesa la solicitud de login.
     * 1. Valida las credenciales del usuario usando AuthenticationManager.
     * 2. Busca los datos completos del usuario en la base de datos usando UsuarioRepository.
     * 3. Genera un token JWT.
     * 4. Empaqueta el token y los datos del usuario en el AuthResponseDTO.
     *
     * @param request DTO con la cédula y contraseña.
     * @return AuthResponseDTO con el token y el DTO del usuario.
     */
    public AuthResponseDTO login(LoginRequestDTO request) {
        // 1. Autenticar con Spring Security. Esto valida la cédula y contraseña.
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.cedula(), request.contrasena())
        );

        // *** CAMBIO CLAVE ***
        // El objeto 'principal' dentro de 'Authentication' es el UserDetails que necesitamos.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 2. Si la autenticación es exitosa, obtener la entidad Usuario completa.
        // Usamos el username del UserDetails para asegurar consistencia.
        Usuario usuario = usuarioRepository.findByCedula(userDetails.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con cédula: " + userDetails.getUsername()));

        // 3. Generar el token JWT pasando el objeto UserDetails que espera el método.
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. Mapear la entidad `Usuario` al DTO `UsuarioResponseDTO`.
        UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO(
            usuario.getCedula(),
            usuario.getNombre(),
            usuario.getCorreo(),
            usuario.getTelefono(),
            usuario.getRol().getNombre() // Asumiendo que la entidad Usuario tiene una relación getRol()
        );

        // 5. Crear y devolver el DTO de respuesta final con el token y los datos del usuario.
        return new AuthResponseDTO(jwtToken, usuarioDTO);
    }
}

