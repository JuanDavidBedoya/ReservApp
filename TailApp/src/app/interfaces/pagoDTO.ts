export interface PagoResponseDTO {
  idPago: string;
  monto: number;
  fechaPago: string;
  nombreMetodo: string;
  nombreEstado: string;
  idReserva: string;
}

export interface PagoRequestDTO {
  idReserva: string;
  monto: number;
  idMetodo: string;
}