import { UsuarioResponseDTO } from "./usuarioDTO";

export interface AuthResponseDTO {
  token: string;
  usuario: UsuarioResponseDTO;
}