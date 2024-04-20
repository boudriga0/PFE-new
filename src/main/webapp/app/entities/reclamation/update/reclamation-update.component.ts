import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IReclamation } from '../reclamation.model';
import { ReclamationService } from '../service/reclamation.service';
import { ReclamationFormService, ReclamationFormGroup } from './reclamation-form.service';
import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import HasAnyAuthorityDirective from "../../../shared/auth/has-any-authority.directive";
import {AccountService} from "../../../core/auth/account.service";
import {Account} from "../../../core/auth/account.model";
const initialAccount: Account = {} as Account;

@Component({
  standalone: true,
  selector: 'reclamation-reclamation-update',
  templateUrl: './reclamation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, NgbModule, HasAnyAuthorityDirective, HasAnyAuthorityDirective, HasAnyAuthorityDirective, HasAnyAuthorityDirective, HasAnyAuthorityDirective],
})
export class ReclamationUpdateComponent implements OnInit {

  isSaving = false;
  reclamation: IReclamation | null = null;

  personnesSharedCollection: IPersonne[] = [];

  editForm: ReclamationFormGroup = this.reclamationFormService.createReclamationFormGroup();
  settingsForm = new FormGroup({
    email: new FormControl(initialAccount.email, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    login: new FormControl(initialAccount.login, { nonNullable: true }),
  });
  email: string = "";
  constructor(
    protected reclamationService: ReclamationService,
    protected reclamationFormService: ReclamationFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
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
    this.accountService.identity().subscribe(account => {
      if (account) {
        console.log(account)
        this.email = account.email;
        this.settingsForm.patchValue(account)
        this.settingsForm.patchValue({ email: account.email });
      }
    });

  }

  previousState(): void {
    window.history.back();
  }


  saveNotVerified(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);
    console.log(reclamation.pieceJointe)
    reclamation.etat = "notVerified";
    reclamation.email = this.email;
    console.log(this.email)
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }
  saveVerified(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);

    reclamation.etat = "Verified";
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }
  saveDeveloped(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);

    reclamation.etat = "isDeveloped";
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }
  saveValid(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);

    reclamation.etat = "isValid";
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
