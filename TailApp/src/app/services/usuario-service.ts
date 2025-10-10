import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UsuarioResponseDTO } from '../interfaces/usuarioDTO';
import { UsuarioCreateDTO } from '../interfaces/registroDTO';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = 'http://localhost:8080/usuarios';

  constructor(private http: HttpClient) {}

  // Obtener usuario por cédula
  getUsuario(cedula: string): Observable<UsuarioResponseDTO> {
    return this.http.get<UsuarioResponseDTO>(`${this.apiUrl}/${cedula}`);
  }

  // Actualizar datos del usuario
  actualizarUsuario(cedula: string, usuario: Partial<UsuarioResponseDTO>): Observable<UsuarioResponseDTO> {
    return this.http.put<UsuarioResponseDTO>(`${this.apiUrl}/${cedula}`, usuario);
  }

  //Crear Usuario
  registrarUsuario(usuario: UsuarioCreateDTO): Observable<UsuarioResponseDTO> {
    return this.http.post<UsuarioResponseDTO>(this.apiUrl, usuario);
  }
}
