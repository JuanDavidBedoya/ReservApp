import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagoResponseDTO } from '../interfaces/pagoDTO';

@Injectable({ providedIn: 'root' })
export class PagoService {
  private apiURL = 'http://localhost:8080/pagos';

  constructor(private http: HttpClient) {}

  pagarReserva(idReserva: string, monto: number, idMetodo: string): Observable<PagoResponseDTO> {
    const params = new HttpParams()
      .set('idReserva', idReserva)
      .set('monto', monto)
      .set('idMetodo', idMetodo);
      
    return this.http.post<PagoResponseDTO>(`${this.apiURL}/pagar`, null, { params });
  }

  obtenerPago(idPago: string): Observable<PagoResponseDTO> {
    return this.http.get<PagoResponseDTO>(`${this.apiURL}/${idPago}`);
  }

  listarPagos(): Observable<PagoResponseDTO[]> {
    return this.http.get<PagoResponseDTO[]>(this.apiURL);
  }
}

