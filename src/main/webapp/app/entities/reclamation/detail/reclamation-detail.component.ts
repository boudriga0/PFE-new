import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReclamation } from '../reclamation.model';
import HasAnyAuthorityDirective from "../../../shared/auth/has-any-authority.directive";
import { findIconDefinition } from "@fortawesome/fontawesome-svg-core";
import { createPdf } from "pdfmake/build/pdfmake";

(pdfMake as any).vfs = pdfFonts.pdfMake.vfs;

@Component({
  standalone: true,
  selector: 'reclamation-reclamation-detail',
  templateUrl: './reclamation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, HasAnyAuthorityDirective],
})
export class ReclamationDetailComponent {
  @Input() reclamation: IReclamation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }

  generatePDF() {
    const stateIcon = findIconDefinition({ prefix: 'fas', iconName: 'circle' });

    let docDefinition: any = {
      content: [
        { text: 'Réclamation:', style: 'header' },
        { text: '\n\n\n\n\n\n\n\n' }, // Ajout de 5 lignes vides
        { text: `ID: ${this.reclamation?.id}`, style: 'subheader' },
        { text: '\n' }, // Ajout de 5 lignes vides
        { text: `Nom du projet: ${this.reclamation?.categorie}`, style: 'subheader' },
        { text: '\n' }, // Ajout de 5 lignes vides
        { text: `Etat: ${this.reclamation?.etat}`, style: 'subheader' },
        { text: '\n' }, // Ajout de 5 lignes vides
        { text: `Plainte: ${this.reclamation?.numero}`, style: 'subheader' },
      ],
      footer: { text: 'GPRO Consulting © 2024 ', style: 'footer' },
      styles: {
        header: {
          fontSize: 25,
          bold: true,
          color: '#930327'
        },
        subheader: {
          fontSize: 15,
          bold: true,
          color: '#111c14'
        },
        footer: {
          fontSize: 10,
          bold: true,
          color: '#333',
          alignment: 'center'
        }
      }
    };

    pdfMake.createPdf(docDefinition).open();
  }

}
