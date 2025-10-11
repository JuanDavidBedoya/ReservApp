package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reservapp.juanb.juanm.dto.ForgotPasswordRequestDTO;
import com.reservapp.juanb.juanm.dto.ResetPasswordRequestDTO;
import com.reservapp.juanb.juanm.dto.UsuarioCreateDTO;
import com.reservapp.juanb.juanm.dto.UsuarioResponseDTO;
import com.reservapp.juanb.juanm.dto.UsuarioUpdateDTO;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.services.UsuarioServicio;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<UsuarioResponseDTO> list = usuarioServicio.findAll();
        return list.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(list);
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable("cedula") String cedula) {
        return ResponseEntity.ok(usuarioServicio.findById(cedula));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> save(@Valid @RequestBody UsuarioCreateDTO usuarioDTO) {
        
        // Validaciones de negocio
        if (usuarioServicio.existsById(usuarioDTO.cedula())) {
            throw new ResourceAlreadyExistsException("La cédula " + usuarioDTO.cedula() + " ya está registrada");
        }
        if (usuarioServicio.existsByCorreo(usuarioDTO.correo())) {
            throw new ResourceAlreadyExistsException("El correo " + usuarioDTO.correo() + " ya está registrado");
        }
        
        UsuarioResponseDTO nuevoUsuario = usuarioServicio.save(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    
    @PutMapping("/{cedula}")
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable("cedula") String cedula, @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) {
        UsuarioResponseDTO usuarioActualizado = usuarioServicio.update(cedula, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        usuarioServicio.delete(cedula);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        usuarioServicio.forgotPassword(request.correo());
        return ResponseEntity.ok(Map.of("message", "Si el correo está registrado, se ha enviado un enlace de restablecimiento."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        usuarioServicio.resetPassword(request.token(), request.nuevaContrasena());
        return ResponseEntity.ok(Map.of("message", "Contraseña restablecida con éxito."));
    }
}