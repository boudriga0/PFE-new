import { Component } from '@angular/core';
import {RouterLink, RouterLinkActive} from "@angular/router";
import HasAnyAuthorityDirective from "../../shared/auth/has-any-authority.directive";

@Component({
  selector: 'reclamation-sidebar',
  standalone: true,
  imports: [
    RouterLinkActive,
    RouterLink,
    HasAnyAuthorityDirective
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {

}
