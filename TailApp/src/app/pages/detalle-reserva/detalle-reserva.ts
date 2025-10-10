import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ReservaService } from '../../services/reserva-service'; 
import { ReservaDTO } from '../../interfaces/reservaDTO'; 

@Component({
  selector: 'app-detalle-reserva',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './detalle-reserva.html',
})
export class DetalleReserva implements OnInit {
  reservaForm!: FormGroup;
  reservaId!: string;
  cargando = true;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private router: Router,
    private reservaService: ReservaService
  ) {}

  ngOnInit() {
    this.reservaId = this.route.snapshot.paramMap.get('id') || '';
    this.cargarReserva();
  }

  cargarReserva() {
    this.reservaService.getReservaPorId(this.reservaId).subscribe({
      next: (reserva: ReservaDTO) => {
        this.reservaForm = this.fb.group({
          fecha: [reserva.fecha, Validators.required],
          hora: [reserva.hora, Validators.required],
          personas: [reserva.numeroPersonas, [Validators.required, Validators.min(1)]],
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

    const datosActualizados: Partial<ReservaDTO> = {
      fecha: this.reservaForm.value.fecha,
      hora: this.reservaForm.value.hora,
      numeroPersonas: this.reservaForm.value.personas,
      // si el backend requiere cédulaUsuario, puedes obtenerla del localStorage:
      cedulaUsuario: JSON.parse(localStorage.getItem('usuario') || '{}').cedula,
    };

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
    const confirmar = confirm('¿Seguro que deseas cancelar esta reserva?');
    if (!confirmar) return;

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
}


