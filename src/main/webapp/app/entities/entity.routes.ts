import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'personne',
    data: { pageTitle: 'pfeApp.personne.home.title' },
    loadChildren: () => import('./personne/personne.routes'),
  },
  {
    path: 'reclamation',
    data: { pageTitle: 'pfeApp.reclamation.home.title' },
    loadChildren: () => import('./reclamation/reclamation.routes'),
  },
  {
    path: 'commentaire',
    data: { pageTitle: 'pfeApp.commentaire.home.title' },
    loadChildren: () => import('./commentaire/commentaire.routes'),
  },
  {
    path: 'piece-jointe',
    data: { pageTitle: 'pfeApp.pieceJointe.home.title' },
    loadChildren: () => import('./piece-jointe/piece-jointe.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
