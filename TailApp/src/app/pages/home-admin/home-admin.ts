import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common'; // Importante para usar *ngIf en la plantilla

@Component({
  selector: 'app-home-admin',
  standalone: true,
  // Asegúrate de importar CommonModule para que las directivas como *ngIf funcionen
  imports: [CommonModule], 
  templateUrl: './home-admin.html',
})
export default class HomeAdmin implements OnInit {
  
  // Propiedad para almacenar el nombre del administrador logueado
  nombreUsuario: string = '';

  constructor(private router: Router) {}

  /**
   * Al iniciar el componente, obtenemos los datos del usuario
   * desde el localStorage para mostrar su nombre.
   */
  ngOnInit(): void {
    const userData = localStorage.getItem('usuario');
    if (userData) {
      const usuario = JSON.parse(userData);
      this.nombreUsuario = usuario.nombre || 'Administrador';
    }
  }

  /**
   * Función genérica para navegar a diferentes rutas.
   * @param ruta La ruta a la que se desea navegar.
   */
  irA(ruta: string): void {
    this.router.navigate([ruta]);
  }
}
