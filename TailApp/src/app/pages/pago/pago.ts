import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PagoService } from '../../services/pago-service';

@Component({
  selector: 'app-pago',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './pago.html',
})
export class Pago implements OnInit {
  paymentForm!: FormGroup;
  cargando = false;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private pagoService: PagoService
  ) {}

  ngOnInit(): void {
    // Ejemplo: obtener reserva desde localStorage o route params
    const reserva = JSON.parse(localStorage.getItem('reservaSeleccionada') || '{}');

    this.paymentForm = this.fb.group({
      idReserva: [{ value: reserva.id || '', disabled: true }],
      monto: [{ value: reserva.monto || 0, disabled: true }],
      metodoPago: ['', Validators.required],
    });
  }

  procederPago() {
  if (this.paymentForm.invalid) {
    alert('Por favor selecciona un método de pago válido.');
    return;
  }

  const reservaSeleccionada = JSON.parse(localStorage.getItem('reservaSeleccionada') || '{}');
  const idReserva = reservaSeleccionada.id;
  const monto = reservaSeleccionada.monto;
  const metodo = this.paymentForm.get('metodoPago')?.value;
  const idMetodo = this.obtenerIdMetodo(metodo);

  if (!idReserva || !monto || !idMetodo) {
    alert('Faltan datos para procesar el pago.');
    return;
  }

  // Si el método requiere pasar por pasarela (ej: tarjeta)
  const requierePasarela = ['Visa', 'Mastercard'].includes(metodo);

  if (requierePasarela) {
    // Redirigimos al componente de pasarela
    this.router.navigate(['/pasarela-pago'], {
      queryParams: { metodo, idReserva, monto },
    });
  } else {
    // Procesamos el pago directamente con el backend
    this.cargando = true;
    this.pagoService.pagarReserva(idReserva, monto, idMetodo).subscribe({
      next: (pago) => {
        this.cargando = false;
        alert('✅ Pago realizado con éxito');
        console.log('Pago:', pago);
        this.router.navigate(['/usuario-reserva']);
      },
      error: (err) => {
        this.cargando = false;
        alert('❌ Error al procesar el pago: ' + (err.error?.message || err.message));
      },
    });
  }
}

  obtenerIdMetodo(nombre: string): string {
    const metodos: Record<string, string> = {
      Visa: '11111111-1111-1111-1111-111111111111',
      Mastercard: '22222222-2222-2222-2222-222222222222',
      Davivienda: '33333333-3333-3333-3333-333333333333',
      Bancolombia: '44444444-4444-4444-4444-444444444444',
    };

    return metodos[nombre] || '';
  }

  cancelar() {
    this.router.navigate(['/usuario-reserva']);
  }
}



