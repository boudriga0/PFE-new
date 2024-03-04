import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonne } from '../personne.model';
import { PersonneService } from '../service/personne.service';

export const personneResolve = (route: ActivatedRouteSnapshot): Observable<null | IPersonne> => {
  const id = route.params['id'];
  if (id) {
    return inject(PersonneService)
      .find(id)
      .pipe(
        mergeMap((personne: HttpResponse<IPersonne>) => {
          if (personne.body) {
            return of(personne.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default personneResolve;
