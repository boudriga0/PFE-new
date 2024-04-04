import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReclamation } from '../reclamation.model';
import HasAnyAuthorityDirective from "../../../shared/auth/has-any-authority.directive";

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

}
