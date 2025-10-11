import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  // --- AÑADE TODA ESTA CONFIGURACIÓN ---
  selector: 'app-reset-password',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  templateUrl: './reset-password.html',
  // ------------------------------------
})
export class ResetPassword implements OnInit {
  token: string | null = null;
  nuevaContrasena = '';
  confirmarContrasena = '';
  mensaje = '';
  esError = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
  }

  onSubmit(): void {
    if (this.nuevaContrasena !== this.confirmarContrasena) {
      this.esError = true;
      this.mensaje = 'Las contraseñas no coinciden.';
      return;
    }

    if (this.token) {
      this.authService.resetPassword(this.token, this.nuevaContrasena).subscribe({
        next: () => {
          alert('¡Contraseña restablecida con éxito! Serás redirigido al login.');
          this.router.navigate(['/login']);
        },
        error: (err) => {
          this.esError = true;
          this.mensaje = err.error?.message || 'El token es inválido o ha expirado.';
        }
      });
    } else {
        this.esError = true;
        this.mensaje = 'No se encontró un token de restablecimiento.';
    }
  }
}