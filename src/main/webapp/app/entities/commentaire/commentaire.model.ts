import { IReclamation } from 'app/entities/reclamation/reclamation.model';

export interface ICommentaire {
  id: number;
  contenu?: string | null;
  reclamation?: Pick<IReclamation, 'id'> | null;
}

export type NewCommentaire = Omit<ICommentaire, 'id'> & { id: null };
