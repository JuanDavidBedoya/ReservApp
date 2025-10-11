import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule, NgClass } from '@angular/common';

// Interfaz local para definir la estructura de los datos de prueba.
interface ReservaDePrueba {
  idReserva: string;
  fecha: string; // LocalDate se representa como string 'YYYY-MM-DD'
  hora: string;  // LocalTime se representa como string 'HH:MM'
  numeroPersonas: number;
  cedulaUsuario: string;
  numeroMesa: number;
  nombreEstado: 'Confirmada' | 'Pendiente' | 'Cancelada';
}

@Component({
  selector: 'app-lista-reservas',
  standalone: true,
  imports: [CommonModule, NgClass], // NgClass es para aplicar estilos condicionales
  templateUrl: './lista-reservas.html',
})
export default class ListaReservas {

  // Datos quemados para la prueba de la interfaz
  reservas: ReservaDePrueba[] = [
    { idReserva: 'e1f2a3b4-c5d6-7890-1234-567890abcdef', fecha: '2025-11-15', hora: '20:00', numeroPersonas: 4, cedulaUsuario: '12345678', numeroMesa: 5, nombreEstado: 'Confirmada' },
    { idReserva: 'f2a3b4c5-d6e7-8901-2345-67890abcdef1', fecha: '2025-11-16', hora: '19:30', numeroPersonas: 2, cedulaUsuario: '87654321', numeroMesa: 12, nombreEstado: 'Pendiente' },
    { idReserva: 'a3b4c5d6-e7f8-9012-3456-7890abcdef12', fecha: '2025-11-18', hora: '21:00', numeroPersonas: 6, cedulaUsuario: '11223344', numeroMesa: 8, nombreEstado: 'Cancelada' },
  ];

  constructor(private router: Router) {}

  /**
   * Navega de vuelta al panel principal de administración.
   */
  volver(): void {
    this.router.navigate(['/home-admin']);
  }

  /**
   * Devuelve clases de CSS para colorear el estado de la reserva.
   * @param estado El estado actual de la reserva.
   * @returns Un string con las clases de Tailwind CSS a aplicar.
   */
  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'Confirmada':
        return 'bg-green-500/20 text-green-400';
      case 'Pendiente':
        return 'bg-yellow-500/20 text-yellow-400';
      case 'Cancelada':
        return 'bg-red-500/20 text-red-400';
      default:
        return 'bg-gray-500/20 text-gray-400';
    }
  }
}

