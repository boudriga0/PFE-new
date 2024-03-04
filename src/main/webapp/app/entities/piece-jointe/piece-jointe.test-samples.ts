import { IPieceJointe, NewPieceJointe } from './piece-jointe.model';

export const sampleWithRequiredData: IPieceJointe = {
  id: 6275,
};

export const sampleWithPartialData: IPieceJointe = {
  id: 26483,
  type: 'bientôt',
};

export const sampleWithFullData: IPieceJointe = {
  id: 2288,
  url: 'https://acre-population-du-quebec.eu/',
  type: 'aussitôt que orange horrible',
  data: 'membre de l’équipe pff chier',
};

export const sampleWithNewData: NewPieceJointe = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
