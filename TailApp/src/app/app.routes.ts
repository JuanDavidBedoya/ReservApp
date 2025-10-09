import { Routes } from '@angular/router';
import { HomePublic } from './pages/home-public/home-public';
import { Perfil } from './pages/perfil/perfil';
import { Reserva } from './pages/reserva/reserva';
import { Comentarios } from './pages/comentario/comentario';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'inicio', component: HomePublic },
  { path: 'perfil', component: Perfil },
  { path: 'reserva', component: Reserva },
  { path: 'comentarios', component: Comentarios},
  { path: '**', redirectTo: 'inicio' }
];