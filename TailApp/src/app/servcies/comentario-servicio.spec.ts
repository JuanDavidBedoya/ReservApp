import { TestBed } from '@angular/core/testing';

import { ComentarioServicio } from './comentario-servicio';

describe('ComentarioServicio', () => {
  let service: ComentarioServicio;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComentarioServicio);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
