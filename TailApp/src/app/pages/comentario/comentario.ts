import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
// NgClass se importa a través de CommonModule, por lo que no es necesario importarlo explícitamente aquí.
import { CommonModule } from '@angular/common';
import { ComentarioService } from '../../services/comentario-service';
import { ComentarioRequestDTO } from '../../interfaces/comentarioDTO';

@Component({
  selector: 'app-comentarios',
  standalone: true,
  templateUrl: './comentario.html',
  // Se simplifica el array de imports. NgClass ya está disponible a través de CommonModule.
  imports: [CommonModule, FormsModule]
})
export class Comentarios implements OnInit {
  rating = 0;
  comentario = '';
  stars = Array(5).fill(0);

  constructor(
    private router: Router,
    private comentarioService: ComentarioService // Inyectar el servicio
  ) {}
  
  ngOnInit(): void {
    // Lógica de inicialización si es necesaria
  }

  // Asignar puntuación seleccionada
  setRating(value: number) {
    this.rating = value;
  }

  // Enviar comentario con validaciones y llamada al servicio
  enviarComentario() {
    if (!this.rating) {
      alert('Por favor selecciona una puntuación.');
      return;
    }

    if (!this.comentario.trim()) {
      alert('Por favor escribe un comentario antes de enviar.');
      return;
    }

    // Obtener el usuario del localStorage para enviar su cédula
    const userData = localStorage.getItem('usuario');
    if (!userData) {
      alert('No se ha encontrado un usuario logueado. Por favor, inicia sesión de nuevo.');
      this.router.navigate(['/login']);
      return;
    }

    const usuario = JSON.parse(userData);
    const idUsuario = usuario.cedula; // Asumimos que la cédula es el identificador

    // Construir el objeto DTO para la petición
    const nuevoComentario: ComentarioRequestDTO = {
      puntuacion: this.rating,
      mensaje: this.comentario,
      idUsuario: idUsuario
    };

    // Llamar al servicio para crear el comentario
    this.comentarioService.crearComentario(nuevoComentario).subscribe({
      next: (response) => {
        console.log('Comentario enviado con éxito:', response);
        alert('¡Gracias por tu comentario!');
        this.router.navigate(['/inicio']); // Redirigir al inicio o a donde prefieras
      },
      error: (err) => {
        console.error('Error al enviar el comentario:', err);
        alert('Hubo un error al enviar tu comentario. Por favor, inténtalo de nuevo.');
      }
    });
  }

  // Cancelar y volver al inicio
  cancelar() {
    this.router.navigate(['/inicio']);
  }
}

