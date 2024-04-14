import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

import NavbarComponent from './layouts/navbar/navbar.component';
import LoginComponent from './login/login.component';

import {SidebarComponent} from "./layouts/sidebar/sidebar.component";
import {FooterComponent} from "./layouts/footer/footer.component";
import {PageComponent} from "./page/page.component";


const routes: Routes = [

  {
    path: '',
    component: NavbarComponent,
    outlet: 'navbar',
  },
  {
    path: '',
    component: SidebarComponent,
    outlet: 'sidebar',
  },
  {
    path: '',
    component: FooterComponent,
    outlet: 'footer',
  },
  {
    path: '',
    component: PageComponent,

  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  ...errorRoute,
];

export default routes;
