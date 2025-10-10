import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { UsuarioResponseDTO, UsuarioUpdateDTO } from '../../interfaces/usuarioDTO';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
})
export class Perfil implements OnInit {
  
  private apiURL = 'http://localhost:8080/usuarios';
  user: UsuarioResponseDTO | null = null;

  formData = {
    nombre: '',
    correo: '',
    password: '',
    confirmarPassword: '',
    telefono: ''
  };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const userData = localStorage.getItem('usuario');
    if (userData) {
      const usuarioLocal = JSON.parse(userData);
      const cedula = usuarioLocal.cedula; // debe guardarse al loguearse
      this.obtenerUsuario(cedula);
    } else {
      alert('No hay usuario logueado');
    }
  }

  obtenerUsuario(cedula: string): void {
    this.http.get<UsuarioResponseDTO>(`${this.apiURL}/${cedula}`).subscribe({
      next: (data) => {
        this.user = data;
        this.formData.nombre = data.nombre;
        this.formData.correo = data.correo;
        this.formData.telefono = data.telefono;
      },
      error: (err) => {
        console.error('Error al obtener el usuario:', err);
        alert('No se pudo cargar el perfil');
      }
    });
  }

  actualizarDatos(): void {
    if (this.formData.password !== this.formData.confirmarPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    if (!this.user) {
      alert('No hay usuario cargado');
      return;
    }

    const updateData: UsuarioUpdateDTO = {
      nombre: this.formData.nombre,
      correo: this.formData.correo,
      telefono: this.formData.telefono,
    };

    this.http.put<UsuarioResponseDTO>(`${this.apiURL}/${this.user.cedula}`, updateData).subscribe({
      next: (data) => {
        this.user = data;
        localStorage.setItem('usuario', JSON.stringify(data));
        alert('Datos actualizados correctamente');
      },
      error: (err) => {
        console.error('Error al actualizar los datos', err);
        alert('No se pudo actualizar el perfil');
      }
    });
  }
}