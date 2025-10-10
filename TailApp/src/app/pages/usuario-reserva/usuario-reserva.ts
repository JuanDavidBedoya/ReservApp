import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReservaService } from '../../services/reserva-service';
import { ReservaDTO } from '../../interfaces/reservaDTO';

@Component({
  selector: 'app-usuario-reserva',
  imports: [CommonModule],
  templateUrl: './usuario-reserva.html',
})
export class UsuarioReservas implements OnInit {
  reservas: ReservaDTO[] = [];
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
    this.router.navigate(['/detalle-reserva', id]);
  }
}

