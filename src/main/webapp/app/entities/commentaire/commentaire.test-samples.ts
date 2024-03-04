import { ICommentaire, NewCommentaire } from './commentaire.model';

export const sampleWithRequiredData: ICommentaire = {
  id: 31891,
};

export const sampleWithPartialData: ICommentaire = {
  id: 8514,
};

export const sampleWithFullData: ICommentaire = {
  id: 24057,
  contenu: 'commis',
};

export const sampleWithNewData: NewCommentaire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
