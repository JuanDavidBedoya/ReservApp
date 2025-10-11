import { Component, OnInit } from '@angular/core'; // <<< Se importa OnInit
import { Router } from '@angular/router';
import { CommonModule, NgClass } from '@angular/common';
import { ReservaService } from '../../services/reserva-service'; // <<< Se importa el servicio
import { ReservaResponseDTO } from '../../interfaces/reservaDTO'; // <<< Se importa la interfaz

@Component({
  selector: 'app-lista-reservas',
  standalone: true,
  imports: [CommonModule, NgClass],
  templateUrl: './lista-reservas.html',
})
export default class ListaReservas implements OnInit { // <<< Se implementa OnInit

  // El arreglo se inicializa vacío y se llenará con datos de la API
  public reservas: ReservaResponseDTO[] = [];

  constructor(
    private router: Router,
    private reservaService: ReservaService // <<< Se inyecta el servicio de reservas
  ) {}

  /**
   * Este método se ejecuta automáticamente cuando el componente se inicializa.
   * Es el lugar perfecto para cargar los datos desde el servidor.
   */
  ngOnInit(): void {
    this.cargarReservas();
  }

  /**
   * Llama al servicio para obtener la lista de todas las reservas y las
   * asigna a la variable local 'reservas'.
   */
  cargarReservas(): void {
    this.reservaService.obtenerReservas().subscribe({
      next: (data) => {
        this.reservas = data;
        console.log('Reservas cargadas exitosamente:', this.reservas);
      },
      error: (err) => {
        console.error('Error al cargar las reservas:', err);
        alert('No se pudieron cargar las reservas. Por favor, intente más tarde.');
      }
    });
  }

  /**
   * Navega de vuelta al panel principal de administración.
   */
  volver(): void {
    this.router.navigate(['/home-admin']);
  }

  /**
   * Devuelve clases de CSS para colorear el estado de la reserva.
   * (Esta función se mantiene igual, está perfecta).
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