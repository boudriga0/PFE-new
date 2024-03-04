import { Component } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import HasAnyAuthorityDirective from '../shared/auth/has-any-authority.directive';
import { NgbDropdown } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { IReclamation } from '../entities/reclamation/reclamation.model';
import { finalize } from 'rxjs/operators';
import { ReclamationService } from '../entities/reclamation/service/reclamation.service';
import { ReclamationFormGroup, ReclamationFormService } from '../entities/reclamation/update/reclamation-form.service';
import { PersonneService } from '../entities/personne/service/personne.service';

@Component({
  selector: 'reclamation-main-page',
  standalone: true,
  imports: [RouterModule, HasAnyAuthorityDirective, HasAnyAuthorityDirective, NgbDropdown, FormsModule, CommonModule],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.scss',
})
export class MainPageComponent {
  isSaving = false;

  editForm: ReclamationFormGroup = this.reclamationFormService.createReclamationFormGroup();

  constructor(
    protected reclamationService: ReclamationService,
    protected reclamationFormService: ReclamationFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  sendDev(): void {
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);
    reclamation.etat = 'isDeveloppment';
    console.log(reclamation);
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

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
  protected onSaveSuccess(): void {
    console.log('success');
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }
}
