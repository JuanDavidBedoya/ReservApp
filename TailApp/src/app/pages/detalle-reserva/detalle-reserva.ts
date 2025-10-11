import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ReservaService } from '../../services/reserva-service'; 
import { ReservaResponseDTO } from '../../interfaces/reservaDTO';

@Component({
  selector: 'app-detalle-reserva',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './detalle-reserva.html',
})

export class DetalleReserva implements OnInit {

  reservaForm: FormGroup;
  cargando = true;
  reservaId: string | undefined;
  reserva: ReservaResponseDTO | undefined;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router,
    private reservaService: ReservaService
  ) {
    this.reservaForm = this.fb.group({
      fecha: ['', Validators.required],
      hora: ['', Validators.required],
      personas: ['', [Validators.required, Validators.min(1)]],
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.reservaId = params['id'];
      this.cargarReserva();
    });
  }

  cargarReserva() {
    if (!this.reservaId) {
      alert('No se encontró el ID de la reserva.');
      this.router.navigate(['/usuario-reserva']);
      return;
    }
    this.reservaService.getReservaPorId(this.reservaId).subscribe({
      next: (reserva: ReservaResponseDTO) => {

        this.reserva = reserva;

        this.reservaForm.patchValue({
          fecha: reserva.fecha,
          hora: reserva.hora,
          personas: reserva.numeroPersonas,
        });
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al cargar la reserva:', err);
        alert('No se pudo cargar la reserva.');
        this.router.navigate(['/usuario-reserva']);
      },
    });
  }

  guardarCambios() {
    if (this.reservaForm.invalid) {
      alert('Por favor, completa todos los campos correctamente.');
      return;
    }

    // Validar que la reserva no esté cancelada o pagada antes de permitir editar
    if (this.reserva && (this.reserva.nombreEstado === 'Cancelada' || this.reserva.nombreEstado === 'Pagada')) {
      alert(`No puedes modificar una reserva con estado: ${this.reserva.nombreEstado}`);
      return;
    }

    const datosActualizados: Partial<ReservaResponseDTO> = {
      fecha: this.reservaForm.value.fecha,
      hora: this.reservaForm.value.hora,
      numeroPersonas: this.reservaForm.value.personas,
      // si el backend requiere cédulaUsuario, puedes obtenerla del localStorage:
      cedulaUsuario: JSON.parse(localStorage.getItem('usuario') || '{}').cedula,
    };

    if (!this.reservaId) {
      alert('No se encontró el ID de la reserva.');
      return;
    }
    this.reservaService.actualizarReserva(this.reservaId, datosActualizados).subscribe({
      next: () => {
        alert('✅ Reserva actualizada correctamente');
        this.router.navigate(['/usuario-reserva']);
      },
      error: (err) => {
        console.error(err);
        alert('❌ No se pudo actualizar la reserva');
      },
    });
  }

  cancelarReserva() {
    if (this.reserva?.nombreEstado === 'Cancelada') {
      alert('Esta reserva ya está cancelada.');
      return;
    }

    if (this.reserva?.nombreEstado === 'Pagada') {
      alert('No puedes cancelar una reserva que ya ha sido pagada.');
      return;
    }

    const confirmar = confirm('¿Seguro que deseas cancelar esta reserva?');
    if (!confirmar) return;

    if (!this.reservaId) {
      alert('No se encontró el ID de la reserva.');
      return;
    }

    this.reservaService.cancelarReserva(this.reservaId).subscribe({
      next: () => {
        alert('❌ Reserva cancelada correctamente');
        this.router.navigate(['/usuario-reserva']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al cancelar la reserva');
      },
    });
  }

  // Método para redirigir a la página de pago
  irAPago() {
  
    if (this.reserva) {
      
      const monto = this.reserva.numeroPersonas * 20000;
      
      localStorage.setItem('reservaSeleccionada', JSON.stringify({
        id: this.reserva.idReserva,
        monto: monto
      }));

      localStorage.setItem('ultimaReservaId', this.reservaId ?? '');
      
      this.router.navigate(['/pago']);
    }
  }

  // Método para volver a la lista de reservas
  volver() {
    this.router.navigate(['/usuario-reserva']);
  }
}