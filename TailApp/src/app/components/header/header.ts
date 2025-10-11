// src/app/components/header/header.ts

import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common'; // <<< IMPORTANTE: Necesario para @if

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  standalone: true, // <<< Recomendado: Hacer el componente standalone
  imports: [RouterModule, CommonModule] // <<< Añadir CommonModule
})
export class Header {
  constructor(private authService: AuthService, private router: Router) { }

  // Hacemos públicos los métodos del servicio para usarlos fácilmente en el HTML
  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  get isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  logout(): void {
    this.authService.logout();
  }
}