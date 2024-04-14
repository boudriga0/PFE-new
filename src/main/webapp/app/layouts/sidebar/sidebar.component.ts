import { Component } from '@angular/core';
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import HasAnyAuthorityDirective from "../../shared/auth/has-any-authority.directive";
import {VERSION} from "../../app.constants";
import {LoginService} from "../../login/login.service";

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
  version = '';
  constructor(
    private loginService: LoginService,
    private router: Router,

  ){
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }
  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }
}
