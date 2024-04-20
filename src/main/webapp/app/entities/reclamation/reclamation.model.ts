import dayjs from 'dayjs/esm';
import { IPieceJointe } from 'app/entities/piece-jointe/piece-jointe.model';
import { ICommentaire } from 'app/entities/commentaire/commentaire.model';
import { IPersonne } from 'app/entities/personne/personne.model';

export interface IReclamation {
  id: number;
  categorie?: string | null;
  email?: string | null;
  etat?: string | null;
  numero?: string | null;
  date?: dayjs.Dayjs | null;
  pieceJointe?: string | null;

  pieceJointes?: Pick<IPieceJointe, 'id'>[] | null;
  commentaires?: Pick<ICommentaire, 'id'>[] | null;
  personne?: Pick<IPersonne, 'id'> | null;

}

export type NewReclamation = Omit<IReclamation, 'id'> & { id: null };
