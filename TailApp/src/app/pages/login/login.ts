
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginRequestDTO } from '../../interfaces/loginResquestDTO';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
})
export default class Login {
  cedula: string = '';
  contrasena: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {
    if (!this.cedula || !this.contrasena) {
      alert('Por favor, ingrese usuario y contraseña.');
      return;
    }

    const request: LoginRequestDTO = {
      cedula: this.cedula,
      contrasena: this.contrasena
    };

    console.log('📤 Enviando login:', request);

    this.authService.login(request).subscribe({
      next: (response) => {
        console.log('✅ Login exitoso:', response);

        if (response.token) {
          // Guardar token usando el método del AuthService
          localStorage.setItem('authToken', response.token);

          // Intentar leer el rol del token (si existe)
          try {
            const payload = JSON.parse(atob(response.token.split('.')[1]));
            const role = payload.role || payload.rol || payload.authorities?.[0];

            console.log('Rol detectado:', role);

            if (role === 'Administrador') {
              this.router.navigate(['/comentarios']);
            } else {
              this.router.navigate(['/home-private']);
            }
          } catch (error) {
            console.warn('No se pudo decodificar el token JWT:', error);
            this.router.navigate(['/home-private']);
          }
        } else {
          alert('No se recibió un token válido desde el servidor.');
        }
      },
      error: (err) => {
        console.error('❌ Error en login:', err);
        if (err.status === 400) {
          alert('Credenciales inválidas.');
        } else if (err.status === 0) {
          alert('No se pudo conectar con el servidor.');
        } else {
          alert('Ocurrió un error inesperado. Intente más tarde.');
        }
      }
    });
  }

}