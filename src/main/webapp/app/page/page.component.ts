import { Component } from '@angular/core';
import {RouterModule} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'reclamation-page',
  standalone: true,
  imports: [RouterModule, NgIf],
  templateUrl: './page.component.html',
  styleUrl: './page.component.scss'
})
export class PageComponent {
  isAuthenticated: boolean = false;

}
