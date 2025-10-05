package com.reservapp.juanb.juanm.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reservapp.juanb.juanm.entities.Usuario;
import com.reservapp.juanb.juanm.exceptions.BadRequestException;
import com.reservapp.juanb.juanm.exceptions.ResourceAlreadyExistsException;
import com.reservapp.juanb.juanm.exceptions.ResourceNotFoundException;
import com.reservapp.juanb.juanm.services.UsuarioServicio;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    private UsuarioServicio usuarioServicio;

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAll(){
        List<Usuario> list = usuarioServicio.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(list); // 200
    }

    @GetMapping("/{cedula}")
    public ResponseEntity<Usuario> getById(@PathVariable("cedula") String cedula){
        Usuario usuario = usuarioServicio.findById(cedula)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula));
        return ResponseEntity.ok(usuario); // 200
    }
    
    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        // Validaciones básicas
        if (usuario.getCedula() == null || usuario.getCedula().trim().isEmpty()) {
            throw new BadRequestException("La cédula no puede estar vacía");
        }
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre no puede estar vacío");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new BadRequestException("El correo no puede estar vacío");
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().trim().isEmpty()) {
            throw new BadRequestException("La contraseña no puede estar vacía");
        }
        if (usuario.getRol() == null) {
            throw new BadRequestException("El usuario debe tener un rol asignado");
        }
        
        // RF2: Validar correo único
        if (usuarioServicio.existsByCorreo(usuario.getCorreo())) {
            throw new ResourceAlreadyExistsException("El correo " + usuario.getCorreo() + " ya está registrado");
        }
        
        // Validar si ya existe la cédula
        if (usuarioServicio.findById(usuario.getCedula()).isPresent()) {
            throw new ResourceAlreadyExistsException("La cédula " + usuario.getCedula() + " ya está registrada");
        }
        
        Usuario nuevoUsuario = usuarioServicio.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario); // 201
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<Usuario> update(@PathVariable("cedula") String cedula, @RequestBody Usuario usuario) {
        // Verificar que existe
        if (!usuarioServicio.findById(cedula).isPresent()) {
            throw new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula);
        }
        
        // Validaciones básicas para actualización
        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new BadRequestException("El nombre no puede estar vacío");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            throw new BadRequestException("El correo no puede estar vacío");
        }
        if (usuario.getRol() == null) {
            throw new BadRequestException("El usuario debe tener un rol asignado");
        }
        
        // Validar que no exista otro usuario con el mismo correo (excluyendo el actual)
        if (usuarioServicio.existsByCorreoAndCedulaNot(usuario.getCorreo(), cedula)) {
            throw new ResourceAlreadyExistsException("Ya existe otro usuario con el correo: " + usuario.getCorreo());
        }
        
        Usuario actualizarUsuario = usuarioServicio.update(cedula, usuario);
        return ResponseEntity.ok(actualizarUsuario); // 200
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("cedula") String cedula) {
        // Verificar que existe
        if (!usuarioServicio.findById(cedula).isPresent()) {
            throw new ResourceNotFoundException("Usuario no encontrado con cédula: " + cedula);
        }
        
        usuarioServicio.delete(cedula);
        return ResponseEntity.noContent().build(); // 204
    }   
}