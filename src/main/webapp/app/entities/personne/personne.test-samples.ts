import dayjs from 'dayjs/esm';

import { IPersonne, NewPersonne } from './personne.model';

export const sampleWithRequiredData: IPersonne = {
  id: 28108,
};

export const sampleWithPartialData: IPersonne = {
  id: 30513,
  prenom: 'disputer tellement sauf à',
  cIN: 'repousser mince',
  rib: 'avertir triste drelin',
  email: 'Ansbert64@yahoo.fr',
};

export const sampleWithFullData: IPersonne = {
  id: 28447,
  nom: 'recouvrir rédaction',
  prenom: 'tant que émerger de manière à ce que',
  cIN: 'hé vouloir',
  dateNaissance: dayjs('2024-02-09'),
  phone: '+33 414779237',
  rib: 'smack au-dessus pourvu que',
  email: 'Calixte28@hotmail.fr',
  sex: 'vide population du Québec taire',
};

export const sampleWithNewData: NewPersonne = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
