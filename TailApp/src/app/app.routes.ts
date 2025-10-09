import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { InicioSesion } from './pages/inicio-sesion/inicio-sesion';

export const routes: Routes = [
    {path: '', component:Home},
    {path: 'inicio-sesion', component:InicioSesion},
    {path: '**', redirectTo: ''}
];
