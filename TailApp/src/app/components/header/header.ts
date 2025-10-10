import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  imports: [RouterModule]
})
export class Header {

  constructor(private router: Router) {}
  
   isInicio(): boolean {
    return this.router.url === '/' || this.router.url === '/inicio';
  }
}
