import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-private',
  imports: [],
  templateUrl: './home-private.html',
})
export class HomePrivate {

  constructor(private router: Router) {}

  irA(ruta: string) {
    this.router.navigate([ruta]);
  }

}
