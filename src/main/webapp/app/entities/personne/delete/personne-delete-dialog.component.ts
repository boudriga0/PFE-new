import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPersonne } from '../personne.model';
import { PersonneService } from '../service/personne.service';

@Component({
  standalone: true,
  templateUrl: './personne-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PersonneDeleteDialogComponent {
  personne?: IPersonne;

  constructor(
    protected personneService: PersonneService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personneService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
