import { Component } from '@angular/core';
import { AuthService } from '../../services/auth';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LoginRequestDTO } from '../../interfaces/loginResquestDTO';
import { AuthResponseDTO } from '../../interfaces/authResponse';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './login.html',
})
export default class Login {
  // Objeto para enlazar con los campos del formulario
  formData = {
    cedula: '',
    contrasena: ''
  };

  // Propiedad para almacenar y mostrar errores del servidor
  errorMessage: string | null = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(): void {
    // La validación de campos vacíos ahora la maneja el formulario en el HTML.
    // Limpiamos cualquier mensaje de error anterior antes de un nuevo intento.
    this.errorMessage = null;

    const request: LoginRequestDTO = {
      cedula: this.formData.cedula,
      contrasena: this.formData.contrasena
    };

    console.log('📤 Enviando solicitud de login:', request);

    this.authService.login(request).subscribe({
      next: (response: AuthResponseDTO) => {
        console.log('✅ Login exitoso. Respuesta recibida:', response);
        const role = response.usuario.nombreRol;
        console.log('Rol detectado:', role);

        // Redirigimos al usuario según su rol.
        if (role === 'Administrador') {
          this.router.navigate(['/home-admin']);
        } else {
          this.router.navigate(['/home-private']);
        }
      },
      error: (err) => {
        console.error('❌ Error en el login:', err);
        // Establecemos el mensaje de error para mostrarlo en la UI.
        if (err.status === 400 || err.status === 401) {
          this.errorMessage = 'La cédula o la contraseña son incorrectas.';
          
        } else if (err.status === 0) {
          this.errorMessage = 'La cédula o la contraseña son incorrectas.';
        } else {
          this.errorMessage = 'La cédula o la contraseña son incorrectas.';
        }
      }
    });
  }
}
