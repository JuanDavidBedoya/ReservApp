export interface UsuarioResponseDTO {
  cedula: string;
  nombre: string;
  correo: string;
  telefono: string;
  nombreRol: string; 
}

export interface UsuarioCreateDTO {
  cedula: string;
  nombre: string;
  correo: string;
  contrasena: string;
  telefono: string;
}