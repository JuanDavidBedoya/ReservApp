// src/app/services/reserva.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservaCreateDTO, ReservaResponseDTO } from '../interfaces/reservaDTO';

@Injectable({ providedIn: 'root' })
export class ReservaService {

  private apiUrl = 'http://localhost:8080/reservas';

  constructor(private http: HttpClient) {}

  crearReserva(reserva: ReservaCreateDTO): Observable<ReservaResponseDTO> {
    return this.http.post<ReservaResponseDTO>(this.apiUrl, reserva);
  }

  obtenerReservas(): Observable<ReservaResponseDTO[]> {
    return this.http.get<ReservaResponseDTO[]>(this.apiUrl);
  }

  getReservasPorUsuario(cedula: string): Observable<ReservaResponseDTO[]> {
    return this.http.get<ReservaResponseDTO[]>(`${this.apiUrl}/usuario/${cedula}`);
  }

  getReservaPorId(id: string): Observable<ReservaResponseDTO> {
    return this.http.get<ReservaResponseDTO>(`${this.apiUrl}/${id}`);
  }

  actualizarReserva(id: string, reserva: Partial<ReservaCreateDTO>): Observable<ReservaResponseDTO> {
    return this.http.put<ReservaResponseDTO>(`${this.apiUrl}/${id}`, reserva);
  }

  cancelarReserva(id: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/cancelar`, {});
  }
}