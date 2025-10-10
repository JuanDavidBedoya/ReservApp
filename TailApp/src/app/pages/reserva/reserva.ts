import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ReservaService } from '../../services/reserva-service';
import { ReservaDTO } from '../../interfaces/reservaDTO';

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

  // Datos del formulario
  reservaData: ReservaDTO = {
    cedulaUsuario: '',
    numeroPersonas: 1,
    fecha: '',
    hora: '',
  };

  formValid = false;

  ngOnInit() {
    // Cargar la cédula del usuario logueado
    const userData = localStorage.getItem('user');
    if (userData) {
      const user = JSON.parse(userData);
      this.reservaData.cedulaUsuario = user.cedula || '';
    }
  }

  onChange() {
    const { cedulaUsuario, numeroPersonas, fecha, hora } = this.reservaData;
    this.formValid = cedulaUsuario.trim() !== '' && numeroPersonas > 0 && fecha.trim() !== '' && hora.trim() !== '';
  }

  realizarReserva() {
    if (!this.formValid) return;

    this.reservaService.crearReserva(this.reservaData).subscribe({
      next: () => {
        alert('Reserva realizada exitosamente');
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Error al realizar la reserva:', err);
        alert('Error al realizar la reserva. Inténtalo nuevamente.');
      }
    });
  }

  cancelar() {
    this.router.navigate(['/']);
  }
}