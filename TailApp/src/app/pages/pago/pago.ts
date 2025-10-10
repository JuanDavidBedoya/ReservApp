import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pago',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './pago.html',
})
export class Pago implements OnInit {
  paymentForm!: FormGroup;

  constructor(private fb: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    // Autocompletar los valores
    const reservaEjemplo = {
      idReserva: 'R-45821',
      monto: 150000,
    };

    this.paymentForm = this.fb.group({
      idReserva: [{ value: reservaEjemplo.idReserva, disabled: true }],
      monto: [{ value: reservaEjemplo.monto, disabled: true }],
      metodoPago: ['Visa', Validators.required],
    });
  }

  procederPago() {
    if (this.paymentForm.invalid) {
      alert('Por favor selecciona un método de pago válido.');
      return;
    }

    const metodo = this.paymentForm.get('metodoPago')?.value;
    this.router.navigate(['/pasarela-pago'], {
      queryParams: { metodo },
    });
  }

  cancelar() {
    this.router.navigate(['/']);
  }
}


