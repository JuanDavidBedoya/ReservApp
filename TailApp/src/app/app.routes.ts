import { Routes } from '@angular/router';
import { HomePublic } from './pages/home-public/home-public';
import { Perfil } from './pages/perfil/perfil';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'inicio', component: HomePublic },
  { path: 'perfil', component: Perfil }
];