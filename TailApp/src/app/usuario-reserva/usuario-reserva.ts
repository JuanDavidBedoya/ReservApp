import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface Reserva {
  id: string;
  fecha: string;
  hora: string;
  personas: number;
  estado: 'Confirmada' | 'Pendiente' | 'Pagada';
}

@Component({
  selector: 'app-usuario-reserva',
  imports: [CommonModule],
  templateUrl: './usuario-reserva.html',
})
export class UsuarioReservas implements OnInit {
  reservas: Reserva[] = [];

  constructor(private router: Router) {}

  ngOnInit() {
    this.reservas = [
      { id: 'R001', fecha: '2025-10-15', hora: '19:00', personas: 4, estado: 'Confirmada' },
      { id: 'R002', fecha: '2025-10-20', hora: '20:30', personas: 2, estado: 'Pendiente' },
      { id: 'R003', fecha: '2025-10-25', hora: '18:00', personas: 5, estado: 'Pagada' },
    ];
  }

  verReserva(id: string) {
    this.router.navigate(['/detalle-reserva', id]);
  }
}
