import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home-public.html'
})
export class HomePublic {

  constructor(private router: Router) {}

  hacerReserva() {
    this.router.navigate(['/reserva']);
  }
}