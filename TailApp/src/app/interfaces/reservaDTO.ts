// src/app/interfaces/reserva.dto.ts

/**
 * Interface para enviar datos al crear una reserva.
 * Coincide con ReservaRequestDTO en Spring Boot.
 */
export interface ReservaCreateDTO {
  fecha: string;
  hora: string;
  numeroPersonas: number;
  cedulaUsuario: string;
}

/**
 * Interface para recibir datos de una reserva desde la API.
 * Coincide con ReservaResponseDTO en Spring Boot.
 */
export interface ReservaResponseDTO {
  idReserva: string;
  fecha: string;
  hora: string;
  numeroPersonas: number;
  cedulaUsuario: string;
  numeroMesa: number;
  nombreEstado: string;
}