import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReclamation } from '../reclamation.model';
import HasAnyAuthorityDirective from '../../../shared/auth/has-any-authority.directive';
import { findIconDefinition } from '@fortawesome/fontawesome-svg-core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Account } from '../../../core/auth/account.model';
import { TranslateModule } from '@ngx-translate/core';
import { AccountService } from '../../../core/auth/account.service';

(pdfMake as any).vfs = pdfFonts.pdfMake.vfs;
const initialAccount: Account = {} as Account;

@Component({
  standalone: true,
  selector: 'reclamation-reclamation-detail',
  templateUrl: './reclamation-detail.component.html',
  imports: [
    SharedModule,
    RouterModule,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    HasAnyAuthorityDirective,
    ReactiveFormsModule,
    TranslateModule,
    FormsModule,
  ],
})
export class ReclamationDetailComponent {
  @Input()
  reclamation: IReclamation | null = null;

  settingsForm = new FormGroup({
    email: new FormControl(initialAccount.email, {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email],
    }),
    login: new FormControl(initialAccount.login, { nonNullable: true }),
  });
  email: string = '';

  constructor(
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
  ) {}

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      if (account) {
        console.log(account);
        this.email = account.email;
        this.settingsForm.patchValue({ email: account.email });
        this.settingsForm.patchValue(account);
      }
    });
  }
  generatePDF() {
    const stateIcon = findIconDefinition({ prefix: 'fas', iconName: 'circle' });

    let docDefinition: any = {
      content: [
        { text: 'Réclamation:', style: 'header' },
        { text: '\n\n\n\n\n\n\n\n' },
        { text: `ID: ${this.reclamation?.id}`, style: 'subheader' },
        { text: '\n' },
        { text: `Email: ${this.reclamation?.email}`, style: 'subheader' },
        { text: '\n' },
        { text: `Date: ${this.reclamation?.date}`, style: 'subheader' },
        { text: '\n' },
        { text: `Etat: ${this.reclamation?.etat}`, style: 'subheader' },
        { text: '\n' },
        { text: `Nom du projet: ${this.reclamation?.categorie}`, style: 'subheader' },
        { text: '\n' },
        { text: `Plainte: ${this.reclamation?.numero}`, style: 'subheader' },
        { text: '\n' },
        { text: `Piece Jointe: ${this.reclamation?.jointpieceContentType}`, style: 'subheader' },
      ],
      footer: { text: 'GPRO Consulting © 2024 ', style: 'footer' },
      styles: {
        header: {
          fontSize: 20,
          bold: true,
          color: '#930327',
        },
        subheader: {
          fontSize: 15,
          bold: true,
          color: '#111c14',
        },
        footer: {
          fontSize: 10,
          bold: true,
          color: '#333',
          alignment: 'center',
        },
      },
    };

    pdfMake.createPdf(docDefinition).open();
  }
}
