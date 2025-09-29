package com.reservapp.juanb.juanm.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reservapp.juanb.juanm.entities.Usuario;
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
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{cedula}")
    public Optional<Usuario> getById(@PathVariable("cedula") String cedula){
        return usuarioServicio.findById(cedula);
    }
    
    @PostMapping
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioServicio.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/{cedula}")
    public ResponseEntity<Usuario> update(@PathVariable("cedula") String cedula, @RequestBody Usuario usuario) {
        Usuario actualizarUsuario = usuarioServicio.update(cedula, usuario);
        return ResponseEntity.ok(actualizarUsuario);
    }

    @DeleteMapping("/{cedula}")
    public ResponseEntity<Void> delete(@PathVariable("ceddula") String cedula) {
        usuarioServicio.delete(cedula);
        return ResponseEntity.noContent().build();
    }   
}