// src/app/components/reserva/reserva.ts

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ReservaService } from '../../services/reserva-service';
import { ReservaCreateDTO } from '../../interfaces/reservaDTO'; // <<< CAMBIO: Se importa la interfaz correcta

@Component({
  selector: 'app-reserva',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reserva.html',
})
export class Reserva implements OnInit {

  constructor(
    private router: Router,
    private reservaService: ReservaService
  ) {}

  // <<< CAMBIO: Se usa la interfaz específica para la creación
  reservaData: ReservaCreateDTO = {
    cedulaUsuario: '',
    numeroPersonas: 1,
    fecha: '',
    hora: '',
  };

  formValid = false;

  ngOnInit() {
    // Cargar la cédula del usuario logueado (esto está bien)
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      this.reservaData.cedulaUsuario = user.cedula || '';
      this.onChange(); // Validar el formulario al cargar la cédula
    }
  }

  onChange() {
    const { cedulaUsuario, numeroPersonas, fecha, hora } = this.reservaData;
    this.formValid = cedulaUsuario.trim() !== '' && numeroPersonas > 0 && fecha.trim() !== '' && hora.trim() !== '';
  }

  realizarReserva() {
    if (!this.formValid) return;

    this.reservaService.crearReserva(this.reservaData).subscribe({
      next: (response) => {
        console.log('Reserva creada exitosamente:', response);
        alert('Reserva realizada exitosamente');
        this.router.navigate(['/']); // Redirigir a la página principal o a "mis reservas"
      },
      error: (err) => {
        console.error('Error al realizar la reserva:', err);
        // Mostrar un mensaje de error más específico si la API lo envía
        const errorMessage = err.error?.message || 'Error al realizar la reserva. Inténtalo nuevamente.';
        alert(errorMessage);
      }
    });
  }

  cancelar() {
    this.router.navigate(['/']);
  }
}