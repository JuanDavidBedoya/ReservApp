// Ubicación: com/reservapp/juanb/juanm/controllers/UsuarioControlador.java
package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        // Las validaciones de campos se delegan a @Valid
        
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
}