import { Component } from '@angular/core';
import { CommonModule, NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  // --- AÑADE TODA ESTA CONFIGURACIÓN ---
  selector: 'app-forgot-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    NgClass
  ],
  templateUrl: './forgot-password.html',
  // ------------------------------------
})
export class ForgotPassword {
  correo = '';
  mensaje = '';
  esError = false;

  constructor(private authService: AuthService) {}

  onSubmit(): void {
    this.mensaje = '';
    this.authService.forgotPassword(this.correo).subscribe({
      next: () => {
        this.esError = false;
        this.mensaje = 'Si existe una cuenta con ese correo, se ha enviado un enlace de restablecimiento.';
      },
      error: (err) => {
        this.esError = true;
        this.mensaje = 'Ocurrió un error. Por favor, inténtalo de nuevo.';
      }
    });
  }
}