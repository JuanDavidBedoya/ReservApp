import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-comentarios',
  templateUrl: './comentario.html',
  imports: [FormsModule, NgClass]
})
export class Comentarios {
  rating = 0;
  comentario = '';
  stars = Array(5).fill(0);

  constructor(private router: Router) {}

  // Asignar puntuación seleccionada
  setRating(value: number) {
    this.rating = value;
  }

  // Enviar comentario con validaciones
  enviarComentario() {
    if (!this.rating) {
      alert('Por favor selecciona una puntuación.');
      return;
    }

    if (!this.comentario.trim()) {
      alert('Por favor escribe un comentario antes de enviar.');
      return;
    }

    alert(`¡Gracias por tu comentario!\nPuntuación: ${this.rating} ⭐\n"${this.comentario}"`);
    this.router.navigate(['/inicio']);
  }

  // Cancelar y volver al inicio
  cancelar() {
    this.router.navigate(['/inicio']);
  }
}
