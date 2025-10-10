import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReservaDTO } from '../interfaces/reservaDTO';

@Injectable({ providedIn: 'root' })
export class ReservaService {

  private apiUrl = 'http://localhost:8080/reservas'; 

  constructor(private http: HttpClient) {}

  crearReserva(reserva: ReservaDTO): Observable<any> {
    return this.http.post(this.apiUrl, reserva);
  }

  obtenerReservas(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getReservasPorUsuario(cedula: string): Observable<ReservaDTO[]> {
    return this.http.get<ReservaDTO[]>(`${this.apiUrl}/usuario/${cedula}`);
  }

  getReservaPorId(id: string): Observable<ReservaDTO> {
    return this.http.get<ReservaDTO>(`${this.apiUrl}/${id}`);
  }

  actualizarReserva(id: string, reserva: Partial<ReservaDTO>): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, reserva);
  }

  cancelarReserva(id: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/cancelar`, {});
  }
}