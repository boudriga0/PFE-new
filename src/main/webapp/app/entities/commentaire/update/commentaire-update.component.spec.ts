import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IReclamation } from 'app/entities/reclamation/reclamation.model';
import { ReclamationService } from 'app/entities/reclamation/service/reclamation.service';
import { CommentaireService } from '../service/commentaire.service';
import { ICommentaire } from '../commentaire.model';
import { CommentaireFormService } from './commentaire-form.service';

import { CommentaireUpdateComponent } from './commentaire-update.component';

describe('Commentaire Management Update Component', () => {
  let comp: CommentaireUpdateComponent;
  let fixture: ComponentFixture<CommentaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commentaireFormService: CommentaireFormService;
  let commentaireService: CommentaireService;
  let reclamationService: ReclamationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CommentaireUpdateComponent],
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
      .overrideTemplate(CommentaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommentaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commentaireFormService = TestBed.inject(CommentaireFormService);
    commentaireService = TestBed.inject(CommentaireService);
    reclamationService = TestBed.inject(ReclamationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Reclamation query and add missing value', () => {
      const commentaire: ICommentaire = { id: 456 };
      const reclamation: IReclamation = { id: 4351 };
      commentaire.reclamation = reclamation;

      const reclamationCollection: IReclamation[] = [{ id: 26713 }];
      jest.spyOn(reclamationService, 'query').mockReturnValue(of(new HttpResponse({ body: reclamationCollection })));
      const additionalReclamations = [reclamation];
      const expectedCollection: IReclamation[] = [...additionalReclamations, ...reclamationCollection];
      jest.spyOn(reclamationService, 'addReclamationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commentaire });
      comp.ngOnInit();

      expect(reclamationService.query).toHaveBeenCalled();
      expect(reclamationService.addReclamationToCollectionIfMissing).toHaveBeenCalledWith(
        reclamationCollection,
        ...additionalReclamations.map(expect.objectContaining),
      );
      expect(comp.reclamationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commentaire: ICommentaire = { id: 456 };
      const reclamation: IReclamation = { id: 9915 };
      commentaire.reclamation = reclamation;

      activatedRoute.data = of({ commentaire });
      comp.ngOnInit();

      expect(comp.reclamationsSharedCollection).toContain(reclamation);
      expect(comp.commentaire).toEqual(commentaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentaire>>();
      const commentaire = { id: 123 };
      jest.spyOn(commentaireFormService, 'getCommentaire').mockReturnValue(commentaire);
      jest.spyOn(commentaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentaire }));
      saveSubject.complete();

      // THEN
      expect(commentaireFormService.getCommentaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(commentaireService.update).toHaveBeenCalledWith(expect.objectContaining(commentaire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentaire>>();
      const commentaire = { id: 123 };
      jest.spyOn(commentaireFormService, 'getCommentaire').mockReturnValue({ id: null });
      jest.spyOn(commentaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commentaire }));
      saveSubject.complete();

      // THEN
      expect(commentaireFormService.getCommentaire).toHaveBeenCalled();
      expect(commentaireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommentaire>>();
      const commentaire = { id: 123 };
      jest.spyOn(commentaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commentaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commentaireService.update).toHaveBeenCalled();
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
