import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IReclamation } from 'app/entities/reclamation/reclamation.model';
import { ReclamationService } from 'app/entities/reclamation/service/reclamation.service';
import { IPieceJointe } from '../piece-jointe.model';
import { PieceJointeService } from '../service/piece-jointe.service';
import { PieceJointeFormService, PieceJointeFormGroup } from './piece-jointe-form.service';

@Component({
  standalone: true,
  selector: 'reclamation-piece-jointe-update',
  templateUrl: './piece-jointe-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PieceJointeUpdateComponent implements OnInit {
  isSaving = false;
  pieceJointe: IPieceJointe | null = null;

  reclamationsSharedCollection: IReclamation[] = [];

  editForm: PieceJointeFormGroup = this.pieceJointeFormService.createPieceJointeFormGroup();

  constructor(
    protected pieceJointeService: PieceJointeService,
    protected pieceJointeFormService: PieceJointeFormService,
    protected reclamationService: ReclamationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareReclamation = (o1: IReclamation | null, o2: IReclamation | null): boolean => this.reclamationService.compareReclamation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pieceJointe }) => {
      this.pieceJointe = pieceJointe;
      if (pieceJointe) {
        this.updateForm(pieceJointe);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pieceJointe = this.pieceJointeFormService.getPieceJointe(this.editForm);
    if (pieceJointe.id !== null) {
      this.subscribeToSaveResponse(this.pieceJointeService.update(pieceJointe));
    } else {
      this.subscribeToSaveResponse(this.pieceJointeService.create(pieceJointe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPieceJointe>>): void {
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

  protected updateForm(pieceJointe: IPieceJointe): void {
    this.pieceJointe = pieceJointe;
    this.pieceJointeFormService.resetForm(this.editForm, pieceJointe);

    this.reclamationsSharedCollection = this.reclamationService.addReclamationToCollectionIfMissing<IReclamation>(
      this.reclamationsSharedCollection,
      pieceJointe.reclamation,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reclamationService
      .query()
      .pipe(map((res: HttpResponse<IReclamation[]>) => res.body ?? []))
      .pipe(
        map((reclamations: IReclamation[]) =>
          this.reclamationService.addReclamationToCollectionIfMissing<IReclamation>(reclamations, this.pieceJointe?.reclamation),
        ),
      )
      .subscribe((reclamations: IReclamation[]) => (this.reclamationsSharedCollection = reclamations));
  }
}
