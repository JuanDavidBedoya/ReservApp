import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PagoService } from '../../services/pago-service';
import { PagoRequestDTO } from '../../interfaces/pagoDTO';

@Component({
  selector: 'app-pago',
  imports: [ReactiveFormsModule, CommonModule, RouterModule],
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
    const reserva = JSON.parse(localStorage.getItem('reservaSeleccionada') || '{}');

    this.paymentForm = this.fb.group({
      idReserva: [{ value: reserva.id || '', disabled: true }],
      monto: [{ value: reserva.monto || 0, disabled: true }],
      metodoPago: ['', Validators.required],
      // Campos de tarjeta (siempre requeridos)
      nombreTitular: ['', [Validators.required, Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/)]],
      numeroTarjeta: ['', [Validators.required]],
      fechaExpiracion: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/[0-9]{2}$/)]],
      cvv: ['', [Validators.required, Validators.pattern(/^[0-9]{3,4}$/)]]
    });
  }

  // Validación personalizada para número de tarjeta
  validarNumeroTarjeta(control: AbstractControl) {
    if (!control.value) {
      return null;
    }
    
    // Remover espacios para la validación
    const numeroLimpio = control.value.replace(/\s/g, '');
    
    // Validar longitud
    if (numeroLimpio.length < 13 || numeroLimpio.length > 19) {
      return { 'longitudInvalida': true };
    }
    
    // Validar que solo contenga números
    if (!/^\d+$/.test(numeroLimpio)) {
      return { 'soloNumeros': true };
    }
    
    return null;
  }

  formatearNumeroTarjeta(event: any) {
    let input = event.target;
    let value = input.value.replace(/\s+/g, '').replace(/[^0-9]/gi, '');
    
    if (value.length > 16) {
      value = value.substring(0, 16);
    }
    
    const matches = value.match(/\d{4,16}/g);
    const match = matches && matches[0] || '';
    const parts = [];
    
    for (let i = 0; i < match.length; i += 4) {
      parts.push(match.substring(i, i + 4));
    }
    
    if (parts.length) {
      input.value = parts.join(' ');
    } else {
      input.value = value;
    }
    
    // Actualizar el form control con el valor formateado
    this.paymentForm.get('numeroTarjeta')?.setValue(input.value, { emitEvent: true });
    
    // Validar después de formatear
    const numeroLimpio = input.value.replace(/\s/g, '');
    const control = this.paymentForm.get('numeroTarjeta');
    
    if (numeroLimpio.length < 13 || numeroLimpio.length > 19) {
      control?.setErrors({ 'longitudInvalida': true });
    } else if (!/^\d+$/.test(numeroLimpio)) {
      control?.setErrors({ 'soloNumeros': true });
    } else {
      // Limpiar errores si es válido
      if (control?.errors?.['longitudInvalida'] || control?.errors?.['soloNumeros']) {
        control.setErrors(null);
      }
    }
  }

  validarFechaExpiracion() {
    const fechaControl = this.paymentForm.get('fechaExpiracion');
    if (fechaControl?.valid && fechaControl.value) {
      const [mes, año] = fechaControl.value.split('/');
      const ahora = new Date();
      const añoActual = ahora.getFullYear() % 100;
      const mesActual = ahora.getMonth() + 1;
      
      if (parseInt(año) < añoActual || (parseInt(año) === añoActual && parseInt(mes) < mesActual)) {
        fechaControl.setErrors({ 'fechaExpirada': true });
      } else {
        // Limpiar errores si la fecha es válida
        if (fechaControl.errors?.['fechaExpirada']) {
          delete fechaControl.errors['fechaExpirada'];
          fechaControl.updateValueAndValidity();
        }
      }
    }
  }

  procederPago() {
    // Marcar todos los campos como tocados para mostrar errores
    this.marcarControlesComoTocados();

    if (this.paymentForm.invalid) {
      console.log('Formulario inválido. Detalles:');
      Object.keys(this.paymentForm.controls).forEach(key => {
        const control = this.paymentForm.get(key);
        if (control?.invalid) {
          console.log(`Campo ${key} es inválido:`, control.errors);
        }
      });
      
      alert('Por favor completa todos los campos correctamente.');
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

    this.cargando = true;

    // Siempre pago con tarjeta
    const pagoData: PagoRequestDTO = {
      idReserva: idReserva,
      monto: monto,
      idMetodo: idMetodo,
      nombreTitular: this.paymentForm.get('nombreTitular')?.value,
      numeroTarjeta: this.paymentForm.get('numeroTarjeta')?.value.replace(/\s/g, ''),
      fechaExpiracion: this.paymentForm.get('fechaExpiracion')?.value,
      cvv: this.paymentForm.get('cvv')?.value
    };

    console.log('Enviando pago:', pagoData);
    
    this.pagoService.pagarReserva(pagoData).subscribe({
      next: (pago) => {
        this.cargando = false;
        alert('✅ Pago realizado con éxito. Se ha enviado la factura a tu correo.');
        this.router.navigate(['/usuario-reserva']);
      },
      error: (err) => {
        this.cargando = false;
        console.error('Error en pago:', err);
        const mensaje = err.status === 400 ? 
          'Error en los datos de la tarjeta. Verifica la información.' : 
          '❌ Error al procesar el pago: ' + (err.error?.message || err.message);
        alert(mensaje);
      },
    });
  }

  private marcarControlesComoTocados() {
    Object.keys(this.paymentForm.controls).forEach(key => {
      const control = this.paymentForm.get(key);
      control?.markAsTouched();
    });
  }

  obtenerIdMetodo(nombre: string): string {
    const metodos: Record<string, string> = {
      'Visa': '11111111-1111-1111-1111-111111111111',
      'Mastercard': '22222222-2222-2222-2222-222222222222',
      'American Express': '55555555-5555-5555-5555-555555555555'
    };
    
    const id = metodos[nombre];
    console.log(`Método: ${nombre} -> ID: ${id}`);
    return id || '';
  }

  cancelar() {
    this.router.navigate(['/usuario-reserva']);
  }

  // Getters para facilitar el acceso en el template
  get metodoPago() { return this.paymentForm.get('metodoPago'); }
  get nombreTitular() { return this.paymentForm.get('nombreTitular'); }
  get numeroTarjeta() { return this.paymentForm.get('numeroTarjeta'); }
  get fechaExpiracion() { return this.paymentForm.get('fechaExpiracion'); }
  get cvv() { return this.paymentForm.get('cvv'); }
}