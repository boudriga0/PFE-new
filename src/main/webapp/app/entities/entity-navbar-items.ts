import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Personne',
    route: '/personne',
    translationKey: 'global.menu.entities.personne',
  },
  {
    name: 'Reclamation',
    route: '/reclamation',
    translationKey: 'global.menu.entities.reclamation',
  },
  {
    name: 'Commentaire',
    route: '/commentaire',
    translationKey: 'global.menu.entities.commentaire',
  },
  {
    name: 'PieceJointe',
    route: '/piece-jointe',
    translationKey: 'global.menu.entities.pieceJointe',
  },
];
