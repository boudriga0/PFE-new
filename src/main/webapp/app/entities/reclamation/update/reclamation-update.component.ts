import { Component, OnInit } from '@angular/core';
import { HttpEventType, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLinkActive } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

import { IPersonne } from 'app/entities/personne/personne.model';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { IReclamation } from '../reclamation.model';
import { ReclamationService } from '../service/reclamation.service';
import { ReclamationFormService, ReclamationFormGroup } from './reclamation-form.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import HasAnyAuthorityDirective from '../../../shared/auth/has-any-authority.directive';
import { AccountService } from '../../../core/auth/account.service';
import { Account } from '../../../core/auth/account.model';
import { UploadFileService } from '../../../services/upload-file.service';
import { AsyncPipe } from '@angular/common';
import { DataUtils, FileLoadError } from '../../../core/util/data-util.service';
const initialAccount: Account = {} as Account;

@Component({
  standalone: true,
  selector: 'reclamation-reclamation-update',
  templateUrl: './reclamation-update.component.html',
  imports: [SharedModule, NgbModule, FormsModule, ReactiveFormsModule, NgbModule, AsyncPipe, HasAnyAuthorityDirective, RouterLinkActive],
})
export class ReclamationUpdateComponent implements OnInit {
  selectedFile: File | null = null;
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
  j: number = 0;
  selectedFiles: FileList | null = null;
  currentFile: File | null = null;
  progress = 0;
  message = '';
  fileInfos: Observable<any> | null = null;
  email: string = '';
  mail = {
    from: 'boudrigaahmed7@gmail.com',
    subject: 'Une correction est disponible' + '\n titre:Une correction pour la réclamation: non-respect des termes du contrat de service',
    name: '',
    to: ['boudrigaahmed7@gmail.com'],
    data: 'cc',
    title: '',
  };
  constructor(
    protected dataUtils: DataUtils,
    protected reclamationService: ReclamationService,
    protected reclamationFormService: ReclamationFormService,
    protected personneService: PersonneService,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    private uploadService: UploadFileService,
  ) {}
  comparePersonne = (o1: IPersonne | null, o2: IPersonne | null): boolean => this.personneService.comparePersonne(o1, o2);

  ngOnInit(): void {
    this.fileInfos = this.uploadService.getFiles();
    this.activatedRoute.data.subscribe(({ reclamation }) => {
      this.reclamation = reclamation;
      if (reclamation) {
        this.updateForm(reclamation);
      }
      this.loadRelationshipsOptions();
    });
    this.accountService.identity().subscribe(account => {
      if (account) {
        console.log(account);
        this.email = account.email;
        this.settingsForm.patchValue(account);
        this.settingsForm.patchValue({ email: account.email });
      }
    });
  }

  selectFile(event: Event) {
    this.selectedFiles = (event.target as HTMLInputElement).files;
  }

  upload() {
    this.progress = 0;

    if (this.currentFile !== null) {
      this.uploadService.upload(this.currentFile).subscribe(
        event => {
          if (event.type === HttpEventType.UploadProgress && event.total) {
            this.progress = Math.round((100 * event.loaded) / event.total);
          } else if (event instanceof HttpResponse) {
            this.message = event.body.message;
            this.fileInfos = this.uploadService.getFiles();
          }
        },
        err => {
          this.progress = 0;
          this.message = 'Could not upload the file!';
          this.currentFile = null;
        },
      );
    }

    this.selectedFiles = null;
  }

  previousState(): void {
    window.history.back();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        /*this.eventManager.broadcast(new EventWithContent<AlertError>('pfeApp.error', { ...err, key: 'error.file.' + err.key })),*/
        console.log(err),
    });
  }

  saveNotVerified(): void {
    this.reclamationService.sendmail(this.mail).subscribe(
      (dataRes: any) => {
        console.log(dataRes);
      },
      (error: any) => {
        if (error.status !== 200) {
          console.log(error.error.errorKey, 'Email');
        } else {
          console.log('e-mail a été envoyé avec succès', 'Email');
        }
      },
    );
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);
    console.log(reclamation);
    reclamation.etat = 'notVerified';
    reclamation.email = this.email;
    console.log(this.email);
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }

  saveVerified(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);
    reclamation.etat = 'Verified';
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }

  saveDeveloped(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);
    reclamation.etat = 'isDeveloped';
    if (reclamation.id !== null) {
      this.subscribeToSaveResponse(this.reclamationService.update(reclamation));
    } else {
      this.subscribeToSaveResponse(this.reclamationService.create(reclamation));
    }
  }

  saveValid(): void {
    this.isSaving = true;
    const reclamation = this.reclamationFormService.getReclamation(this.editForm);
    reclamation.etat = 'isValid';
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
