import { Routes } from '@angular/router';
import { HomePublic } from './pages/home-public/home-public/home-public';

export const routes: Routes = [
  { path: '', component: HomePublic },
  { path: '**', redirectTo: '' },
];
