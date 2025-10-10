import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detalle-reserva',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './detalle-reserva.html',
})
export class DetalleReserva implements OnInit {
  reservaForm!: FormGroup;
  reservaId!: string;

  constructor(private route: ActivatedRoute, private fb: FormBuilder, private router: Router) {}

  ngOnInit() {
    this.reservaId = this.route.snapshot.paramMap.get('id') || '';
    // Simular datos de la reserva (reemplazar con tu servicio real)
    const reserva = {
      id: this.reservaId,
      fecha: '2025-10-15',
      hora: '19:00',
      personas: 4,
    };

    this.reservaForm = this.fb.group({
      fecha: [reserva.fecha, Validators.required],
      hora: [reserva.hora, Validators.required],
      personas: [reserva.personas, [Validators.required, Validators.min(1)]],
    });
  }

  guardarCambios() {
    if (this.reservaForm.valid) {
      console.log('Reserva modificada:', this.reservaForm.value);
      alert('Reserva actualizada correctamente ✅');
      this.router.navigate(['/usuario-reserva']);
    } else {
      alert('Por favor, completa todos los campos correctamente.');
    }
  }

  cancelarReserva() {
    const confirmar = confirm('¿Seguro que deseas cancelar esta reserva?');
    if (confirmar) {
      alert('Reserva cancelada ❌');
      this.router.navigate(['/usuario-reserva']);
    }
  }
}

