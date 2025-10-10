import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ComentarioRequestDTO, ComentarioResponseDTO } from '../interfaces/comentarioDTO';

@Injectable({
  providedIn: 'root'
})
export class ComentarioService {
  private apiUrl = 'http://localhost:8080/comentarios';

  constructor(private http: HttpClient) {}

  /**
   * Envía un nuevo comentario al servidor.
   * @param comentario El objeto con los datos del comentario a crear.
   * @returns Un Observable con la respuesta del servidor.
   */
  crearComentario(comentario: ComentarioRequestDTO): Observable<ComentarioResponseDTO> {
    return this.http.post<ComentarioResponseDTO>(this.apiUrl, comentario);
  }
}
