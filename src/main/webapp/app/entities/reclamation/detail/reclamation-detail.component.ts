import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReclamation } from '../reclamation.model';
import HasAnyAuthorityDirective from "../../../shared/auth/has-any-authority.directive";
import {findIconDefinition} from "@fortawesome/fontawesome-svg-core";
import {createPdf} from "pdfmake/build/pdfmake";
(pdfMake as any).vfs = pdfFonts.pdfMake.vfs;
@Component({
  standalone: true,
  selector: 'reclamation-reclamation-detail',
  templateUrl: './reclamation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe,HasAnyAuthorityDirective,],
})
export class ReclamationDetailComponent {
  @Input() reclamation: IReclamation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
  generatePDF(){
    let docDefinition = {
      content:[
        { text: 'Réclamation:', style: 'header' },
        { text: `ID: ${this.reclamation?.categorie}`, style: 'subheader' },
        { text: `Nom du projet: ${this.reclamation?.categorie}`, style: 'subheader' },
        { text: `Etat: ${this.reclamation?.etat}`, style: 'subheader' },
        { text: `Plainte: ${this.reclamation?.numero}`, style: 'subheader' },
      ],

    };
    pdfMake.createPdf(docDefinition).open();
  }



}
