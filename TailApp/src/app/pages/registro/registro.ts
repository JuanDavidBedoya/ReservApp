import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router'; // Importa RouterLink para la navegación

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink // Añade RouterLink a los imports
  ],
  templateUrl: './registro.html',
})
export class Registro {

  constructor() { }

}
