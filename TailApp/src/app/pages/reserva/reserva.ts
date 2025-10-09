import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reserva',
  imports: [CommonModule, FormsModule],
  templateUrl: './reserva.html',
})
export class Reserva {
  constructor(private router: Router) {}

  reservaData = {
    cedula: '',
    personas: 1,
    fecha: '',
    hora: ''
  };

  formValid = false;

  onChange() {
    const { cedula, personas, fecha, hora } = this.reservaData;
    this.formValid = cedula.trim() !== '' && personas > 0 && fecha.trim() !== '' && hora.trim() !== '';
  }

  realizarReserva() {
    if (!this.formValid) return;

    console.log('Reserva realizada:', this.reservaData);
    alert('Reserva realizada exitosamente');
    this.router.navigate(['/']);
  }

  cancelar() {
    this.router.navigate(['/']);
  }
}
