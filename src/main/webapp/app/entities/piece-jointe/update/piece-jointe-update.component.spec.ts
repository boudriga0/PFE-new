import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IReclamation } from 'app/entities/reclamation/reclamation.model';
import { ReclamationService } from 'app/entities/reclamation/service/reclamation.service';
import { PieceJointeService } from '../service/piece-jointe.service';
import { IPieceJointe } from '../piece-jointe.model';
import { PieceJointeFormService } from './piece-jointe-form.service';

import { PieceJointeUpdateComponent } from './piece-jointe-update.component';

describe('PieceJointe Management Update Component', () => {
  let comp: PieceJointeUpdateComponent;
  let fixture: ComponentFixture<PieceJointeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pieceJointeFormService: PieceJointeFormService;
  let pieceJointeService: PieceJointeService;
  let reclamationService: ReclamationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), PieceJointeUpdateComponent],
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
      .overrideTemplate(PieceJointeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PieceJointeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pieceJointeFormService = TestBed.inject(PieceJointeFormService);
    pieceJointeService = TestBed.inject(PieceJointeService);
    reclamationService = TestBed.inject(ReclamationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Reclamation query and add missing value', () => {
      const pieceJointe: IPieceJointe = { id: 456 };
      const reclamation: IReclamation = { id: 16281 };
      pieceJointe.reclamation = reclamation;

      const reclamationCollection: IReclamation[] = [{ id: 28467 }];
      jest.spyOn(reclamationService, 'query').mockReturnValue(of(new HttpResponse({ body: reclamationCollection })));
      const additionalReclamations = [reclamation];
      const expectedCollection: IReclamation[] = [...additionalReclamations, ...reclamationCollection];
      jest.spyOn(reclamationService, 'addReclamationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pieceJointe });
      comp.ngOnInit();

      expect(reclamationService.query).toHaveBeenCalled();
      expect(reclamationService.addReclamationToCollectionIfMissing).toHaveBeenCalledWith(
        reclamationCollection,
        ...additionalReclamations.map(expect.objectContaining),
      );
      expect(comp.reclamationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pieceJointe: IPieceJointe = { id: 456 };
      const reclamation: IReclamation = { id: 26501 };
      pieceJointe.reclamation = reclamation;

      activatedRoute.data = of({ pieceJointe });
      comp.ngOnInit();

      expect(comp.reclamationsSharedCollection).toContain(reclamation);
      expect(comp.pieceJointe).toEqual(pieceJointe);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPieceJointe>>();
      const pieceJointe = { id: 123 };
      jest.spyOn(pieceJointeFormService, 'getPieceJointe').mockReturnValue(pieceJointe);
      jest.spyOn(pieceJointeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pieceJointe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pieceJointe }));
      saveSubject.complete();

      // THEN
      expect(pieceJointeFormService.getPieceJointe).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pieceJointeService.update).toHaveBeenCalledWith(expect.objectContaining(pieceJointe));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPieceJointe>>();
      const pieceJointe = { id: 123 };
      jest.spyOn(pieceJointeFormService, 'getPieceJointe').mockReturnValue({ id: null });
      jest.spyOn(pieceJointeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pieceJointe: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pieceJointe }));
      saveSubject.complete();

      // THEN
      expect(pieceJointeFormService.getPieceJointe).toHaveBeenCalled();
      expect(pieceJointeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPieceJointe>>();
      const pieceJointe = { id: 123 };
      jest.spyOn(pieceJointeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pieceJointe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pieceJointeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReclamation', () => {
      it('Should forward to reclamationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(reclamationService, 'compareReclamation');
        comp.compareReclamation(entity, entity2);
        expect(reclamationService.compareReclamation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
