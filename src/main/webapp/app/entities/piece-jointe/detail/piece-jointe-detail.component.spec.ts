import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PieceJointeDetailComponent } from './piece-jointe-detail.component';

describe('PieceJointe Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PieceJointeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PieceJointeDetailComponent,
              resolve: { pieceJointe: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PieceJointeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load pieceJointe on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PieceJointeDetailComponent);

      // THEN
      expect(instance.pieceJointe).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
