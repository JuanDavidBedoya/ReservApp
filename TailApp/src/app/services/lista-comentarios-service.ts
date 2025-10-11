// src/app/services/comentario.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComentarioResponseDTO, PromedioSemanalDTO } from '../interfaces/comentarioDTO';

@Injectable({
  providedIn: 'root'
})
export class ListarComentarioService {
  // La URL base de tu endpoint de comentarios en Spring Boot
  private apiUrl = 'http://localhost:8080/comentarios';

  constructor(private http: HttpClient) {}

  /**
   * Obtiene la lista de todos los comentarios desde el backend.
   * @returns Un Observable con un arreglo de comentarios.
   */
  listarComentarios(): Observable<ComentarioResponseDTO[]> {
    return this.http.get<ComentarioResponseDTO[]>(this.apiUrl);
  }

  obtenerPromedioSemanal(): Observable<PromedioSemanalDTO> {
  return this.http.get<PromedioSemanalDTO>(`${this.apiUrl}/promedio-semanal`);
}
}