import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagoRequestDTO, PagoResponseDTO } from '../interfaces/pagoDTO';

@Injectable({ providedIn: 'root' })
export class PagoService {
  
  private apiURL = 'http://localhost:8080/pagos';

  constructor(private http: HttpClient) {}

  pagarReserva(pagoData: PagoRequestDTO): Observable<PagoResponseDTO> {
    return this.http.post<PagoResponseDTO>(`${this.apiURL}`, pagoData);
  }

  obtenerPago(idPago: string): Observable<PagoResponseDTO> {
    return this.http.get<PagoResponseDTO>(`${this.apiURL}/${idPago}`);
  }

  listarPagos(): Observable<PagoResponseDTO[]> {
    return this.http.get<PagoResponseDTO[]>(this.apiURL);
  }
}

