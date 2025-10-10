import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { PagoService } from '../../services/pago-service';
import { PagoResponseDTO } from '../../interfaces/pagoDTO';

@Component({
  selector: 'app-pasarela-pago',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './pasarela-pago.html',
})
export class PasarelaPago implements OnInit {
  paymentForm!: FormGroup;
  metodoPago: string = '';
  idReserva: string = '';
  monto: number = 0;
  metodoIds: { [key: string]: string } = {
    Visa: 'd4c7b5e6-3b2a-4c7f-8c90-9a1f7d2b4567',
    Mastercard: 'e8a1d7f0-91a8-4f6b-8e3b-2c4a6b8f9034',
    Davivienda: 'f3b6e2a9-5c4d-41e9-9c75-8f0d1a2c8b4f',
    Bancolombia: 'a6e9b7f3-2d8c-4e1a-bf56-9d3a7e0c5d21'
  };

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private pagoService: PagoService
  ) {}

  ngOnInit(): void {
    this.metodoPago = this.route.snapshot.queryParams['metodo'] || 'Tarjeta';
    this.idReserva = this.route.snapshot.queryParams['idReserva'];
    this.monto = parseFloat(this.route.snapshot.queryParams['monto']) || 0;

    this.paymentForm = this.fb.group({
      nombre: ['', Validators.required],
      numeroTarjeta: ['', [Validators.required, Validators.pattern('^[0-9]{16}$')]],
      expiracion: ['', [Validators.required, Validators.pattern('(0[1-9]|1[0-2])/[0-9]{2}')]],
      cvv: ['', [Validators.required, Validators.pattern('^[0-9]{3,4}$')]],
    });
  }

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

  pagar(): void {
    if (this.paymentForm.invalid) {
      alert('Por favor completa todos los campos correctamente.');
      return;
    }

    const numero = this.paymentForm.get('numeroTarjeta')?.value;
    if (!this.validarTarjeta(numero)) {
      alert('Número de tarjeta inválido.');
      return;
    }

    const idMetodo = this.metodoIds[this.metodoPago as keyof typeof this.metodoIds];
    if (!idMetodo) {
      alert('Método de pago no reconocido.');
      return;
    }

    this.pagoService.pagarReserva(this.idReserva, this.monto, idMetodo).subscribe({
      next: (respuesta: PagoResponseDTO) => {
        alert(`✅ Pago realizado con éxito. Estado: ${respuesta.nombreEstado}`);
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error(err);
        alert('❌ Error al procesar el pago. Intenta nuevamente.');
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/pago']);
  }
}
