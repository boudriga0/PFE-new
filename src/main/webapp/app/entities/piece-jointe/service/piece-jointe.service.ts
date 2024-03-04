import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPieceJointe, NewPieceJointe } from '../piece-jointe.model';

export type PartialUpdatePieceJointe = Partial<IPieceJointe> & Pick<IPieceJointe, 'id'>;

export type EntityResponseType = HttpResponse<IPieceJointe>;
export type EntityArrayResponseType = HttpResponse<IPieceJointe[]>;

@Injectable({ providedIn: 'root' })
export class PieceJointeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/piece-jointes');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(pieceJointe: NewPieceJointe): Observable<EntityResponseType> {
    return this.http.post<IPieceJointe>(this.resourceUrl, pieceJointe, { observe: 'response' });
  }

  update(pieceJointe: IPieceJointe): Observable<EntityResponseType> {
    return this.http.put<IPieceJointe>(`${this.resourceUrl}/${this.getPieceJointeIdentifier(pieceJointe)}`, pieceJointe, {
      observe: 'response',
    });
  }

  partialUpdate(pieceJointe: PartialUpdatePieceJointe): Observable<EntityResponseType> {
    return this.http.patch<IPieceJointe>(`${this.resourceUrl}/${this.getPieceJointeIdentifier(pieceJointe)}`, pieceJointe, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPieceJointe>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPieceJointe[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPieceJointeIdentifier(pieceJointe: Pick<IPieceJointe, 'id'>): number {
    return pieceJointe.id;
  }

  comparePieceJointe(o1: Pick<IPieceJointe, 'id'> | null, o2: Pick<IPieceJointe, 'id'> | null): boolean {
    return o1 && o2 ? this.getPieceJointeIdentifier(o1) === this.getPieceJointeIdentifier(o2) : o1 === o2;
  }

  addPieceJointeToCollectionIfMissing<Type extends Pick<IPieceJointe, 'id'>>(
    pieceJointeCollection: Type[],
    ...pieceJointesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pieceJointes: Type[] = pieceJointesToCheck.filter(isPresent);
    if (pieceJointes.length > 0) {
      const pieceJointeCollectionIdentifiers = pieceJointeCollection.map(
        pieceJointeItem => this.getPieceJointeIdentifier(pieceJointeItem)!,
      );
      const pieceJointesToAdd = pieceJointes.filter(pieceJointeItem => {
        const pieceJointeIdentifier = this.getPieceJointeIdentifier(pieceJointeItem);
        if (pieceJointeCollectionIdentifiers.includes(pieceJointeIdentifier)) {
          return false;
        }
        pieceJointeCollectionIdentifiers.push(pieceJointeIdentifier);
        return true;
      });
      return [...pieceJointesToAdd, ...pieceJointeCollection];
    }
    return pieceJointeCollection;
  }
}
