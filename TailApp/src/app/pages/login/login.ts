import { Component } from '@angular/core';
import { AuthService } from '../../services/auth';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginRequestDTO } from '../../interfaces/loginResquestDTO';
import { AuthResponseDTO } from '../../interfaces/authResponse';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
})
export default class LoginComponent {
  cedula: string = '';
  contrasena: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {
    if (!this.cedula || !this.contrasena) {
      alert('Por favor, ingrese cédula y contraseña.');
      return;
    }

    const request: LoginRequestDTO = {
      cedula: this.cedula,
      contrasena: this.contrasena
    };

    console.log('📤 Enviando solicitud de login:', request);

    this.authService.login(request).subscribe({
      next: (response: AuthResponseDTO) => {
        console.log('✅ Login exitoso. Respuesta recibida:', response);

        // La lógica de guardar el token y el usuario ya se maneja en el servicio gracias al operador `tap`.
        // Ahora solo nos preocupamos de la redirección.

        const role = response.usuario.nombreRol;
        console.log('Rol detectado:', role);

        // Redirigimos al usuario según su rol.
        if (role === 'ADMINISTRADOR') {
          this.router.navigate(['/comentarios']);
        } else {
          this.router.navigate(['/home-private']);
        }
      },
      error: (err) => {
        console.error('❌ Error en el login:', err);
        // Manejo de errores mejorado para dar feedback más específico.
        const errorMessage = err.error?.message || 'Credenciales inválidas.';
        if (err.status === 400 || err.status === 401) {
          alert(errorMessage);
        } else if (err.status === 0) {
          alert('No se pudo conectar con el servidor. Verifique su conexión o el estado del backend.');
        } else {
          alert('Ocurrió un error inesperado. Por favor, intente más tarde.');
        }
      }
    });
  }
}
