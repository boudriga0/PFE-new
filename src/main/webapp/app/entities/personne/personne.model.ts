import dayjs from 'dayjs/esm';
import { IReclamation } from 'app/entities/reclamation/reclamation.model';

export interface IPersonne {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  cIN?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  phone?: string | null;
  rib?: string | null;
  email?: string | null;
  sex?: string | null;
  reclamations?: Pick<IReclamation, 'id'>[] | null;
}

export type NewPersonne = Omit<IPersonne, 'id'> & { id: null };
