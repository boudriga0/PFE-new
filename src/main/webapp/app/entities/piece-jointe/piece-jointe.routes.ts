import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PieceJointeComponent } from './list/piece-jointe.component';
import { PieceJointeDetailComponent } from './detail/piece-jointe-detail.component';
import { PieceJointeUpdateComponent } from './update/piece-jointe-update.component';
import PieceJointeResolve from './route/piece-jointe-routing-resolve.service';

const pieceJointeRoute: Routes = [
  {
    path: '',
    component: PieceJointeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PieceJointeDetailComponent,
    resolve: {
      pieceJointe: PieceJointeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PieceJointeUpdateComponent,
    resolve: {
      pieceJointe: PieceJointeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PieceJointeUpdateComponent,
    resolve: {
      pieceJointe: PieceJointeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pieceJointeRoute;
