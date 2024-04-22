import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReclamation, NewReclamation } from '../reclamation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReclamation for edit and NewReclamationFormGroupInput for create.
 */
type ReclamationFormGroupInput = IReclamation | PartialWithRequiredKeyOf<NewReclamation>;

type ReclamationFormDefaults = Pick<NewReclamation, 'id'>;

type ReclamationFormGroupContent = {
  id: FormControl<IReclamation['id'] | NewReclamation['id']>;
  categorie: FormControl<IReclamation['categorie']>;
  piece: FormControl<IReclamation['piece']>;
  email: FormControl<IReclamation['email']>;
  etat: FormControl<IReclamation['etat']>;
  numero: FormControl<IReclamation['numero']>;
  date: FormControl<IReclamation['date']>;

  pieceJointe: FormControl<IReclamation['pieceJointe']>;
  personne: FormControl<IReclamation['personne']>;
};

export type ReclamationFormGroup = FormGroup<ReclamationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReclamationFormService {
  createReclamationFormGroup(reclamation: ReclamationFormGroupInput = { id: null }): ReclamationFormGroup {
    const reclamationRawValue = {
      ...this.getFormDefaults(),
      ...reclamation,
    };
    return new FormGroup<ReclamationFormGroupContent>({
      id: new FormControl(
        { value: reclamationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      categorie: new FormControl(reclamationRawValue.categorie),
      piece: new FormControl(reclamationRawValue.piece),
      email: new FormControl(reclamationRawValue.email),
      etat: new FormControl(reclamationRawValue.etat),
      numero: new FormControl(reclamationRawValue.numero),
      date: new FormControl(reclamationRawValue.date),
      pieceJointe: new FormControl(reclamationRawValue.pieceJointe),
      personne: new FormControl(reclamationRawValue.personne),
    });
  }

  getReclamation(form: ReclamationFormGroup): IReclamation | NewReclamation {
    return form.getRawValue() as IReclamation | NewReclamation;
  }

  resetForm(form: ReclamationFormGroup, reclamation: ReclamationFormGroupInput): void {
    const reclamationRawValue = { ...this.getFormDefaults(), ...reclamation };
    form.reset(
      {
        ...reclamationRawValue,
        id: { value: reclamationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReclamationFormDefaults {
    return {
      id: null,
    };
  }
}
