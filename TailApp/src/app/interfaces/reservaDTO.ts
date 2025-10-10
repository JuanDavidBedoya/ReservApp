export interface ReservaDTO {
  idReserva?: string;
  fecha: string;          
  hora: string;           
  numeroPersonas: number;
  cedulaUsuario: string;
  numeroMesa?: number;
  nombreEstado?: string;
}