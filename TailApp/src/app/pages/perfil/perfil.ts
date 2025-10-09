import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-perfil',
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
})
export class Perfil {

  // Datos del usuario (ejemplo)
  user = {
    nombre: 'Juan Pérez',
    correo: 'juanperez@email.com',
    telefono: '300 123 4567'
  };

  // Datos del formulario
  formData = {
    nombre: '',
    correo: '',
    password: '',
    confirmarPassword: '',
    telefono: ''
  };

  actualizarDatos() {
    if (this.formData.password !== this.formData.confirmarPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    console.log('Datos actualizados:', this.formData);
    alert('Datos actualizados correctamente');
  }
}
