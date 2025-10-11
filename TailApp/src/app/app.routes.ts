import { Routes } from '@angular/router';
import { HomePublic } from './pages/home-public/home-public';
import { Perfil } from './pages/perfil/perfil';
import { Reserva } from './pages/reserva/reserva';
import { Comentarios } from './pages/comentario/comentario';
import { Pago } from './pages/pago/pago';
import { PasarelaPago } from './pages/pasarela-pago/pasarela-pago';
import { DetalleReserva } from './pages/detalle-reserva/detalle-reserva';
import { Registro } from './pages/registro/registro';
import { UsuarioReservas } from './pages/usuario-reserva/usuario-reserva';
import Login from './pages/login/login';
import { HomePrivate } from './pages/home-private/home-private';
import { AuthGuard } from './guards/auth-guard';
import { AuthenticatedGuard } from './guards/authenticated-guard';
import HomeAdmin from './pages/home-admin/home-admin';
import { adminGuard } from './guards/admin-guard';
import ListaComentarios from './pages/lista-comentarios/lista-comentarios';
import ListaReservas from './pages/lista-reservas/lista-reservas';
import { ForgotPassword } from './pages/forgot-password/forgot-password';
import { ResetPassword } from './pages/reset-password/reset-password';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'inicio', component: HomePublic, canActivate: [AuthenticatedGuard]},
  { path: 'home-private', component: HomePrivate, canActivate: [AuthGuard] },
  { path: 'perfil', component: Perfil, canActivate: [AuthGuard]},
  { path: 'reserva', component: Reserva, canActivate: [AuthGuard] },
  { path: 'pago', component: Pago, canActivate: [AuthGuard] },
  { path: 'pasarela-pago', component: PasarelaPago , canActivate: [AuthGuard]},
  { path: 'comentarios', component: Comentarios, canActivate: [AuthGuard]},
  { path: 'login', component: Login, canActivate: [AuthenticatedGuard]},
  { path: 'usuario-reserva', component: UsuarioReservas, canActivate: [AuthGuard] },
  { path: 'detalle-reserva/:id', component: DetalleReserva, canActivate: [AuthGuard] },
  { path: 'registro', component: Registro, canActivate: [AuthenticatedGuard] },
  { path: 'home-admin', component: HomeAdmin, canActivate: [adminGuard] },
  { path: 'lista-comentarios', component: ListaComentarios, canActivate: [adminGuard] },
  { path: 'lista-reservas', component: ListaReservas, canActivate: [adminGuard] },
  { path: 'forgot-password', component: ForgotPassword, canActivate: [AuthenticatedGuard] },
  { path: 'reset-password', component: ResetPassword, canActivate: [AuthenticatedGuard] },
  { path: '**', redirectTo: 'inicio' }
];