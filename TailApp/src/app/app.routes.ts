import { Routes } from '@angular/router';
import { HomePublic } from './pages/home-public/home-public';
import { Perfil } from './pages/perfil/perfil';
import { Reserva } from './pages/reserva/reserva';
import { Comentarios } from './pages/comentario/comentario';
import { Login } from './pages/login/login';
import { Pago } from './pages/pago/pago';
import { PasarelaPago } from './pages/pasarela-pago/pasarela-pago';
import { DetalleReserva } from './pages/detalle-reserva/detalle-reserva';
import { Registro } from './pages/registro/registro';
import { UsuarioReservas } from './pages/usuario-reserva/usuario-reserva';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'inicio', component: HomePublic },
  { path: 'perfil', component: Perfil },
  { path: 'reserva', component: Reserva },
  { path: 'pago', component: Pago },
  { path: 'pasarela-pago', component: PasarelaPago },
  { path: 'comentarios', component: Comentarios},
  { path: 'login', component: Login},
  { path: 'usuario-reserva', component: UsuarioReservas },
  { path: 'detalle-reserva/:id', component: DetalleReserva },
  { path: 'registro', component: Registro },
  { path: '**', redirectTo: 'inicio' }

];