import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPieceJointe } from '../piece-jointe.model';
import { PieceJointeService } from '../service/piece-jointe.service';

export const pieceJointeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPieceJointe> => {
  const id = route.params['id'];
  if (id) {
    return inject(PieceJointeService)
      .find(id)
      .pipe(
        mergeMap((pieceJointe: HttpResponse<IPieceJointe>) => {
          if (pieceJointe.body) {
            return of(pieceJointe.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default pieceJointeResolve;
