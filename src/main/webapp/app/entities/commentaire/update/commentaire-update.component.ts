import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReclamation } from 'app/entities/reclamation/reclamation.model';
import { ReclamationService } from 'app/entities/reclamation/service/reclamation.service';
import { ICommentaire } from '../commentaire.model';
import { CommentaireService } from '../service/commentaire.service';
import { CommentaireFormService, CommentaireFormGroup } from './commentaire-form.service';

@Component({
  standalone: true,
  selector: 'reclamation-commentaire-update',
  templateUrl: './commentaire-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CommentaireUpdateComponent implements OnInit {
  isSaving = false;
  commentaire: ICommentaire | null = null;

  reclamationsSharedCollection: IReclamation[] = [];

  editForm: CommentaireFormGroup = this.commentaireFormService.createCommentaireFormGroup();

  constructor(
    protected commentaireService: CommentaireService,
    protected commentaireFormService: CommentaireFormService,
    protected reclamationService: ReclamationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareReclamation = (o1: IReclamation | null, o2: IReclamation | null): boolean => this.reclamationService.compareReclamation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentaire }) => {
      this.commentaire = commentaire;
      if (commentaire) {
        this.updateForm(commentaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commentaire = this.commentaireFormService.getCommentaire(this.editForm);
    if (commentaire.id !== null) {
      this.subscribeToSaveResponse(this.commentaireService.update(commentaire));
    } else {
      this.subscribeToSaveResponse(this.commentaireService.create(commentaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommentaire>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(commentaire: ICommentaire): void {
    this.commentaire = commentaire;
    this.commentaireFormService.resetForm(this.editForm, commentaire);

    this.reclamationsSharedCollection = this.reclamationService.addReclamationToCollectionIfMissing<IReclamation>(
      this.reclamationsSharedCollection,
      commentaire.reclamation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reclamationService
      .query()
      .pipe(map((res: HttpResponse<IReclamation[]>) => res.body ?? []))
      .pipe(
        map((reclamations: IReclamation[]) =>
          this.reclamationService.addReclamationToCollectionIfMissing<IReclamation>(reclamations, this.commentaire?.reclamation),
        ),
      )
      .subscribe((reclamations: IReclamation[]) => (this.reclamationsSharedCollection = reclamations));
  }
}
