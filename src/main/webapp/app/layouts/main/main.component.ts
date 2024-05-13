import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { FooterComponent } from '../footer/footer.component';
import { CommonModule, registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import dayjs from 'dayjs';

registerLocaleData(localeFr);
@Component({
  selector: 'reclamation-main',
  standalone: true,
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [RouterOutlet, FooterComponent, PageRibbonComponent, CommonModule],
})
export default class MainComponent implements OnInit {
  private renderer: Renderer2;

  constructor(
    private router: Router,
    private appPageTitleStrategy: AppPageTitleStrategy,
    public accountService: AccountService,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe();

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }
  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }
  title = 'Angular Upload Files';
}
