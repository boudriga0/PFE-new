import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPieceJointe, NewPieceJointe } from '../piece-jointe.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPieceJointe for edit and NewPieceJointeFormGroupInput for create.
 */
type PieceJointeFormGroupInput = IPieceJointe | PartialWithRequiredKeyOf<NewPieceJointe>;

type PieceJointeFormDefaults = Pick<NewPieceJointe, 'id'>;

type PieceJointeFormGroupContent = {
  id: FormControl<IPieceJointe['id'] | NewPieceJointe['id']>;
  url: FormControl<IPieceJointe['url']>;
  type: FormControl<IPieceJointe['type']>;
  data: FormControl<IPieceJointe['data']>;
  reclamation: FormControl<IPieceJointe['reclamation']>;
};

export type PieceJointeFormGroup = FormGroup<PieceJointeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PieceJointeFormService {
  createPieceJointeFormGroup(pieceJointe: PieceJointeFormGroupInput = { id: null }): PieceJointeFormGroup {
    const pieceJointeRawValue = {
      ...this.getFormDefaults(),
      ...pieceJointe,
    };
    return new FormGroup<PieceJointeFormGroupContent>({
      id: new FormControl(
        { value: pieceJointeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      url: new FormControl(pieceJointeRawValue.url),
      type: new FormControl(pieceJointeRawValue.type),
      data: new FormControl(pieceJointeRawValue.data),
      reclamation: new FormControl(pieceJointeRawValue.reclamation),
    });
  }

  getPieceJointe(form: PieceJointeFormGroup): IPieceJointe | NewPieceJointe {
    return form.getRawValue() as IPieceJointe | NewPieceJointe;
  }

  resetForm(form: PieceJointeFormGroup, pieceJointe: PieceJointeFormGroupInput): void {
    const pieceJointeRawValue = { ...this.getFormDefaults(), ...pieceJointe };
    form.reset(
      {
        ...pieceJointeRawValue,
        id: { value: pieceJointeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PieceJointeFormDefaults {
    return {
      id: null,
    };
  }
}
