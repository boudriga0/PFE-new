import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PersonneComponent } from './list/personne.component';
import { PersonneDetailComponent } from './detail/personne-detail.component';
import { PersonneUpdateComponent } from './update/personne-update.component';
import PersonneResolve from './route/personne-routing-resolve.service';

const personneRoute: Routes = [
  {
    path: '',
    component: PersonneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonneDetailComponent,
    resolve: {
      personne: PersonneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonneUpdateComponent,
    resolve: {
      personne: PersonneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonneUpdateComponent,
    resolve: {
      personne: PersonneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default personneRoute;
