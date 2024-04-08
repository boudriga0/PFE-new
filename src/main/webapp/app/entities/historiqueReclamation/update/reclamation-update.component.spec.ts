import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { ReclamationService } from '../service/reclamation.service';
import { IReclamation } from '../reclamation.model';
import { ReclamationFormService } from './reclamation-form.service';

import { ReclamationUpdateComponent } from './reclamation-update.component';

describe('Reclamation Management Update Component', () => {
  let comp: ReclamationUpdateComponent;
  let fixture: ComponentFixture<ReclamationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reclamationFormService: ReclamationFormService;
  let reclamationService: ReclamationService;
  let personneService: PersonneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ReclamationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReclamationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReclamationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reclamationFormService = TestBed.inject(ReclamationFormService);
    reclamationService = TestBed.inject(ReclamationService);
    personneService = TestBed.inject(PersonneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personne query and add missing value', () => {
      const reclamation: IReclamation = { id: 456 };
      const personne: IPersonne = { id: 31929 };
      reclamation.personne = personne;

      const personneCollection: IPersonne[] = [{ id: 15421 }];
      jest.spyOn(personneService, 'query').mockReturnValue(of(new HttpResponse({ body: personneCollection })));
      const additionalPersonnes = [personne];
      const expectedCollection: IPersonne[] = [...additionalPersonnes, ...personneCollection];
      jest.spyOn(personneService, 'addPersonneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      expect(personneService.query).toHaveBeenCalled();
      expect(personneService.addPersonneToCollectionIfMissing).toHaveBeenCalledWith(
        personneCollection,
        ...additionalPersonnes.map(expect.objectContaining),
      );
      expect(comp.personnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reclamation: IReclamation = { id: 456 };
      const personne: IPersonne = { id: 20994 };
      reclamation.personne = personne;

      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      expect(comp.personnesSharedCollection).toContain(personne);
      expect(comp.reclamation).toEqual(reclamation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReclamation>>();
      const reclamation = { id: 123 };
      jest.spyOn(reclamationFormService, 'getReclamation').mockReturnValue(reclamation);
      jest.spyOn(reclamationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reclamation }));
      saveSubject.complete();

      // THEN
      expect(reclamationFormService.getReclamation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reclamationService.update).toHaveBeenCalledWith(expect.objectContaining(reclamation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReclamation>>();
      const reclamation = { id: 123 };
      jest.spyOn(reclamationFormService, 'getReclamation').mockReturnValue({ id: null });
      jest.spyOn(reclamationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reclamation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reclamation }));
      saveSubject.complete();

      // THEN
      expect(reclamationFormService.getReclamation).toHaveBeenCalled();
      expect(reclamationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReclamation>>();
      const reclamation = { id: 123 };
      jest.spyOn(reclamationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reclamation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reclamationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePersonne', () => {
      it('Should forward to personneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personneService, 'comparePersonne');
        comp.comparePersonne(entity, entity2);
        expect(personneService.comparePersonne).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
