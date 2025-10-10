export interface ComentarioRequestDTO {
  puntuacion: number;
  mensaje: string;
  idUsuario: string; // Se envía la cédula del usuario
}

export interface ComentarioResponseDTO {
  idComentario: string;
  puntuacion: number;
  mensaje: string;
  fechaComentario: string; // Las fechas suelen manejarse como string en formato ISO
  nombreUsuario: string;
}