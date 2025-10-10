export interface UsuarioResponseDTO {
  cedula: string;
  nombre: string;
  correo: string;
  telefono: string;
}

export interface UsuarioUpdateDTO {
  nombre: string;
  correo: string;
  telefono: string;
}
