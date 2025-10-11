import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaService } from '../../services/reserva-service';
import { ReservaResponseDTO } from '../../interfaces/reservaDTO';

@Component({
  selector: 'app-usuario-reserva',
  imports: [CommonModule],
  templateUrl: './usuario-reserva.html',
})
export class UsuarioReservas implements OnInit {
  reservas: ReservaResponseDTO[] = [];
  loading = true;
  error = false;

  constructor(private router: Router, private reservaService: ReservaService) {}

  ngOnInit() {
    const userData = localStorage.getItem('usuario');
    if (!userData) {
      this.error = true;
      this.loading = false;
      return;
    }

    const usuario = JSON.parse(userData);
    const cedula = usuario.cedula || usuario.cedulaUsuario;

    this.reservaService.getReservasPorUsuario(cedula).subscribe({
      next: (data) => {
        this.reservas = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error al obtener reservas', err);
        this.error = true;
        this.loading = false;
      },
    });
  }

  verReserva(id: string) {
  // Primero obtener los datos de la reserva para verificar el estado
  this.reservaService.getReservaPorId(id).subscribe({
    next: (reserva: ReservaResponseDTO) => {
      if (reserva.nombreEstado === 'Cancelada') {
        alert('No puedes ver los detalles de una reserva cancelada.');
        return;
      }
      // Si no está cancelada, navegar a la página de detalle
      this.router.navigate(['/detalle-reserva', id]);
    },
    error: (err) => {
      console.error('Error al verificar el estado de la reserva:', err);
      alert('No se pudo verificar el estado de la reserva.');
    }
  });
}
}

