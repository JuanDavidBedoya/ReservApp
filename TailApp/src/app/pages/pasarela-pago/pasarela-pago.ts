import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-pasarela-pago',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './pasarela-pago.html',
})
export class PasarelaPago implements OnInit {
  paymentForm!: FormGroup;
  metodoPago: string = '';

  constructor(private fb: FormBuilder, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.metodoPago = this.route.snapshot.queryParams['metodo'] || 'Tarjeta';

    this.paymentForm = this.fb.group({
      nombre: ['', Validators.required],
      numeroTarjeta: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
      expiracion: ['', [Validators.required, Validators.pattern('(0[1-9]|1[0-2])/[0-9]{2}')]],
      cvv: ['', [Validators.required, Validators.pattern('^[0-9]{3,4}$')]],
    });
  }

  // Algoritmo Luhn para validación del número de tarjeta
  validarTarjeta(num: string): boolean {
    const arr = num.split('').reverse().map((n) => parseInt(n, 10));
    const suma = arr.reduce((acc, val, i) => {
      if (i % 2 !== 0) {
        val *= 2;
        if (val > 9) val -= 9;
      }
      return acc + val;
    }, 0);
    return suma % 10 === 0;
  }

  pagar() {
    if (this.paymentForm.invalid) {
      alert('Por favor completa todos los campos correctamente.');
      return;
    }

    const numero = this.paymentForm.get('numeroTarjeta')?.value;
    if (!this.validarTarjeta(numero)) {
      alert('Número de tarjeta inválido.');
      return;
    }

    alert('Pago realizado con éxito ✅');
    this.router.navigate(['/']);
  }

  cancelar() {
    this.router.navigate(['/pago']);
  }
}
