import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PersonneDetailComponent } from './personne-detail.component';

describe('Personne Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PersonneDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PersonneDetailComponent,
              resolve: { personne: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PersonneDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load personne on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PersonneDetailComponent);

      // THEN
      expect(instance.personne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
