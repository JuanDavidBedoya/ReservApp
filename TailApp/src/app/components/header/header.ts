import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-header',
  templateUrl: './header.html',
  imports: [RouterModule]
})
export class Header {
  constructor(private authService: AuthService, private router: Router) { }

  logout(): void {
    this.authService.logout();
  }

  isInicio(): boolean {
    return this.router.url === '/' || this.router.url === '/inicio';
  }
}
