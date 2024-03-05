import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IReclamation } from '../reclamation.model';
import { ReclamationService } from '../service/reclamation.service';
import { ReclamationFormService, ReclamationFormGroup } from './reclamation-form.service';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";


@Component({
  standalone: true,
  selector: 'reclamation-reclamation-update',
  templateUrl: './reclamation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgbModule ],
})
export class ReclamationUpdateComponent implements OnInit {
  isSaving = false;
  reclamation: IReclamation | null = null;

  personnesSharedCollection: IPersonne[] = [];

  editForm: ReclamationFormGroup = this.reclamationFormService.createReclamationFormGroup();

  constructor(
    protected reclamationService: ReclamationService,
    protected reclamationFormService: ReclamationFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reclamation }) => {
      this.reclamation = reclamation;
      if (reclamation) {
        this.updateForm(reclamation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);

    reclamation.etat = "notVerified";
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReclamation>>): void {
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

  protected updateForm(reclamation: IReclamation): void {
    this.reclamation = reclamation;
    this.reclamationFormService.resetForm(this.editForm, reclamation);

    this.personnesSharedCollection = this.personneService.addPersonneToCollectionIfMissing<IPersonne>(
      this.personnesSharedCollection,
      reclamation.personne,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personneService
      .query()
      .pipe(map((res: HttpResponse<IPersonne[]>) => res.body ?? []))
      .pipe(
        map((personnes: IPersonne[]) =>
          this.personneService.addPersonneToCollectionIfMissing<IPersonne>(personnes, this.reclamation?.personne),
        ),
      )
      .subscribe((personnes: IPersonne[]) => (this.personnesSharedCollection = personnes));
  }
}
