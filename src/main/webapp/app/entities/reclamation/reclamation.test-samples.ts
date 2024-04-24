import dayjs from 'dayjs/esm';

import { IReclamation, NewReclamation } from './reclamation.model';

export const sampleWithRequiredData: IReclamation = {
  id: 32696,
};

export const sampleWithPartialData: { date: dayjs.Dayjs; categorie: string; isDeveloping: string; id: number } = {
  id: 17026,
  categorie: 'étant donné que',
  isDeveloping: 'true',
  date: dayjs('2024-02-09'),
};

export const sampleWithFullData: { date: dayjs.Dayjs; categorie: string; numero: string; isDeveloping: string; id: number; etat: string } = {
  id: 7212,
  categorie: 'bien que bof',
  isDeveloping: 'true',
  etat: 'sitôt que concurrence',
  numero: 'près',
  date: dayjs('2024-02-09'),
};

export const sampleWithNewData: NewReclamation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
