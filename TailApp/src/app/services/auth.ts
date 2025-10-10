import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequestDTO } from '../interfaces/loginResquestDTO';
import { AuthResponseDTO } from '../interfaces/authResponse';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private LOGIN_URL = 'http://localhost:8080/auth/login';
  private tokenKey = 'authToken';

  constructor(private httpClient: HttpClient, private router: Router) {}

  login(request: LoginRequestDTO): Observable<AuthResponseDTO> {
    return this.httpClient.post<AuthResponseDTO>(this.LOGIN_URL, request).pipe(
      tap(response => {
        if (response.token) {
          console.log('Token recibido:', response.token);
          this.setToken(response.token);
        }
      })
    );
  }

  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    if(typeof window != "undefined"){
      return localStorage.getItem(this.tokenKey);
    }else{
      return null
    }
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const exp = payload.exp * 1000;
      return Date.now() < exp;
    } catch (e) {
      console.error('Error decodificando el token', e);
      return false;
    }
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/login']);
  }
}