import {Component, Renderer2, RendererFactory2} from '@angular/core';
import {Router, RouterModule} from "@angular/router";
import {NgIf} from "@angular/common";
import {AppPageTitleStrategy} from "../app-page-title-strategy";
import {AccountService} from "../core/auth/account.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'reclamation-page',
  standalone: true,
  imports: [RouterModule, NgIf],
  templateUrl: './page.component.html',
  styleUrl: './page.component.scss'
})
export class PageComponent {
  isAuthenticated: boolean = false;
  constructor(
    public accountService: AccountService,


  ){}
  isLoggedIn(): boolean {
    return this.accountService.isAuthenticated();
  }
}
