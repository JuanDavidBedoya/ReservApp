import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  
  // Inyectamos el Router para poder redirigir si es necesario
  const router = inject(Router);

  // 1. Verificamos si estamos en el navegador para acceder a localStorage
  if (typeof window !== 'undefined') {
    const userData = localStorage.getItem('usuario');

    // 2. Si hay datos de usuario, los analizamos
    if (userData) {
      const usuario = JSON.parse(userData);
      
      // 3. ¡La lógica clave! Verificamos si el rol es 'ADMINISTRADOR'
      if (usuario && usuario.nombreRol === 'Administrador') {
        return true; // Si es Admin, permitimos el acceso
      }
    }
  }
  
  // 4. Si no es Admin o no hay sesión, lo redirigimos a la página de login
  router.navigate(['/login']);
  return false; // Bloqueamos el acceso
};