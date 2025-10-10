import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { LoginRequestDTO } from '../interfaces/loginResquestDTO';
import { AuthResponseDTO } from '../interfaces/authResponse';
import { UsuarioResponseDTO } from '../interfaces/usuarioDTO';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private LOGIN_URL = 'http://localhost:8080/auth/login';
  private tokenKey = 'authToken'; // Clave para guardar el token
  private userKey = 'usuario';   // Clave para guardar los datos del usuario

  constructor(private httpClient: HttpClient, private router: Router) {}

  /**
   * Realiza la petición de login.
   * Utiliza `tap` para centralizar el almacenamiento del token y del usuario,
   * manteniendo el componente más limpio.
   */
  login(request: LoginRequestDTO): Observable<AuthResponseDTO> {
    return this.httpClient.post<AuthResponseDTO>(this.LOGIN_URL, request).pipe(
      tap((response: AuthResponseDTO) => {
        // Guardamos el token y el objeto de usuario en localStorage
        this.saveAuthData(response.token, response.usuario);
      })
    );
  }

  /**
   * Guarda la información de autenticación en el localStorage.
   */
  private saveAuthData(token: string, user: UsuarioResponseDTO): void {
    if (typeof window !== "undefined") {
      localStorage.setItem(this.tokenKey, token);
      localStorage.setItem(this.userKey, JSON.stringify(user));
    }
  }

  /**
   * Cierra la sesión del usuario.
   * Elimina tanto el token como el objeto de usuario.
   */
  logout(): void {
    if (typeof window !== "undefined") {
      localStorage.removeItem(this.tokenKey);
      localStorage.removeItem(this.userKey);
    }
    this.router.navigate(['/login']);
  }

  /**
   * Verifica si el usuario está autenticado basado en la existencia del token.
   */
  isAuthenticated(): boolean {
    if (typeof window !== "undefined") {
      return localStorage.getItem(this.tokenKey) !== null;
    }
    return false;
  }

  /**
   * Obtiene el token de autenticación del localStorage.
   * Útil para añadirlo a las cabeceras en un interceptor.
   */
  getToken(): string | null {
    if (typeof window !== "undefined") {
      return localStorage.getItem(this.tokenKey);
    }
    return null;
  }

  /**
   * Obtiene los datos del usuario autenticado del localStorage.
   * Permite que otros componentes y servicios accedan fácilmente a la información.
   */
  getUser(): UsuarioResponseDTO | null {
    if (typeof window !== "undefined") {
      const user = localStorage.getItem(this.userKey);
      return user ? JSON.parse(user) : null;
    }
    return null;
  }
}
