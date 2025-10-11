import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagoRequestDTO, PagoResponseDTO } from '../interfaces/pagoDTO';

@Injectable({ providedIn: 'root' })
export class PagoService {
  // Asegúrate que esta URL sea correcta (el endpoint de tu backend)
  private apiURL = 'http://localhost:8080/pagos';

  constructor(private http: HttpClient) {}

  /**
   * Envía la información de pago al backend usando el cuerpo de la petición (body).
   */
  pagarReserva(idReserva: string, monto: number, idMetodo: string): Observable<PagoResponseDTO> {
    // Objeto con los datos que se enviarán en el cuerpo (body) de la solicitud POST
    const pagoData: PagoRequestDTO = {
      idReserva: idReserva,
      monto: monto,
      idMetodo: idMetodo
    };

    // La llamada POST que envía el objeto pagoData en el body
    return this.http.post<PagoResponseDTO>(`this.apiURL${'/pagar-reserva'}`, pagoData);
  }

  obtenerPago(idPago: string): Observable<PagoResponseDTO> {
    return this.http.get<PagoResponseDTO>(`${this.apiURL}/${idPago}`);
  }

  listarPagos(): Observable<PagoResponseDTO[]> {
    return this.http.get<PagoResponseDTO[]>(this.apiURL);
  }
}

