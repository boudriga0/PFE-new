import { IReclamation } from 'app/entities/reclamation/reclamation.model';

export interface IPieceJointe {
  id: number;
  url?: string | null;
  type?: string | null;
  data?: string | null;
  reclamation?: Pick<IReclamation, 'id'> | null;
}

export type NewPieceJointe = Omit<IPieceJointe, 'id'> & { id: null };
