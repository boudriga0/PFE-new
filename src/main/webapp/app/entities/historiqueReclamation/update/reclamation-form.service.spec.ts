import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../reclamation.test-samples';

import { ReclamationFormService } from './reclamation-form.service';

describe('Reclamation Form Service', () => {
  let service: ReclamationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReclamationFormService);
  });

  describe('Service methods', () => {
    describe('createReclamationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createReclamationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categorie: expect.any(Object),
            etat: expect.any(Object),
            numero: expect.any(Object),
            date: expect.any(Object),
            isDeveloping: expect.any(Object),
            personne: expect.any(Object),
          }),
        );
      });

      it('passing IReclamation should create a new form with FormGroup', () => {
        const formGroup = service.createReclamationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categorie: expect.any(Object),
            etat: expect.any(Object),
            numero: expect.any(Object),
            date: expect.any(Object),
            isDeveloping: expect.any(Object),
            personne: expect.any(Object),
          }),
        );
      });
    });

    describe('getReclamation', () => {
      it('should return NewReclamation for default Reclamation initial value', () => {
        const formGroup = service.createReclamationFormGroup(sampleWithNewData);

        const reclamation = service.getReclamation(formGroup) as any;

        expect(reclamation).toMatchObject(sampleWithNewData);
      });

      it('should return NewReclamation for empty Reclamation initial value', () => {
        const formGroup = service.createReclamationFormGroup();

        const reclamation = service.getReclamation(formGroup) as any;

        expect(reclamation).toMatchObject({});
      });

      it('should return IReclamation', () => {
        const formGroup = service.createReclamationFormGroup(sampleWithRequiredData);

        const reclamation = service.getReclamation(formGroup) as any;

        expect(reclamation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IReclamation should not enable id FormControl', () => {
        const formGroup = service.createReclamationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewReclamation should disable id FormControl', () => {
        const formGroup = service.createReclamationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
