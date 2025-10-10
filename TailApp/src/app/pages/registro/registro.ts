import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Necesario para ngModel
import { UsuarioService } from '../../services/usuario-service';
import { UsuarioCreateDTO } from '../../interfaces/registroDTO';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FormsModule // Importar FormsModule
  ],
  templateUrl: './registro.html',
})
export class Registro implements OnInit {

  // Objeto para enlazar con los campos del formulario
  formData = {
    cedula: '',
    nombre: '',
    correo: '',
    contrasena: '',
    telefono: ''
  };

  constructor(
    private usuarioService: UsuarioService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Este método se ejecuta al inicializar el componente.
    // Se puede usar para cargar datos iniciales si fuera necesario.
  }

  /**
   * Método para manejar el envío del formulario de registro.
   */
  registrarUsuario(): void {
    // Construir el DTO con los datos del formulario
    const nuevoUsuario: UsuarioCreateDTO = {
      cedula: this.formData.cedula,
      nombre: this.formData.nombre,
      correo: this.formData.correo,
      contrasena: this.formData.contrasena,
      telefono: this.formData.telefono
    };

    // Llamar al servicio para registrar el usuario
    this.usuarioService.registrarUsuario(nuevoUsuario).subscribe({
      next: (response) => {
        console.log('Usuario registrado con éxito:', response);
        alert('¡Registro exitoso! Ahora puedes iniciar sesión.');
        this.router.navigate(['/login']); // Redirigir al login
      },
      error: (err) => {
        console.error('Error al registrar el usuario:', err);
        // Aquí podrías manejar diferentes tipos de errores (ej: cédula ya existe)
        alert('Hubo un error en el registro. Por favor, verifica tus datos.');
      }
    });
  }
}

