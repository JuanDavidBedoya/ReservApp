import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ComentarioResponseDTO, PromedioSemanalDTO } from '../../interfaces/comentarioDTO';
import { ListarComentarioService } from '../../services/lista-comentarios-service';

@Component({
  selector: 'app-lista-comentarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-comentarios.html',
})
export default class ListaComentarios implements OnInit {

  public comentarios: ComentarioResponseDTO[] = [];
  public promedioSemanal: PromedioSemanalDTO | null = null;
  public cargandoPromedio: boolean = false;
  public today: Date = new Date(); // Para mostrar la última actualización

  constructor(
    private router: Router,
    private comentarioService: ListarComentarioService
  ) {}

  ngOnInit(): void {
    this.cargarComentarios();
    this.cargarPromedioSemanal();
  }

  cargarComentarios(): void {
    this.comentarioService.listarComentarios().subscribe({
      next: (data) => {
        this.comentarios = data;
        this.today = new Date(); // Actualizar timestamp
        console.log('Comentarios cargados exitosamente:', data);
      },
      error: (err) => {
        console.error('Error al cargar los comentarios:', err);
        alert('No se pudieron cargar los comentarios. Inténtelo más tarde.');
      }
    });
  }

  cargarPromedioSemanal(): void {
    this.cargandoPromedio = true;
    this.comentarioService.obtenerPromedioSemanal().subscribe({
      next: (data) => {
        this.promedioSemanal = data;
        this.cargandoPromedio = false;
        console.log('Promedio semanal cargado:', data);
      },
      error: (err) => {
        console.error('Error al cargar el promedio semanal:', err);
        this.cargandoPromedio = false;
        alert('No se pudo cargar el promedio semanal.');
      }
    });
  }

  mostrarEstrellas(puntuacion: number): string {
    const estrellasLlenas = '★'.repeat(puntuacion);
    const estrellasVacias = '☆'.repeat(5 - puntuacion);
    return estrellasLlenas + estrellasVacias;
  }

  mostrarEstrellasPromedio(promedio: number): string {
    const estrellasLlenas = '★'.repeat(Math.round(promedio));
    const estrellasVacias = '☆'.repeat(5 - Math.round(promedio));
    return estrellasLlenas + estrellasVacias;
  }

  volver(): void {
    this.router.navigate(['/home-admin']);
  }
}