import {Component, Input} from '@angular/core';
import {MainNavItem} from "../dashboard/main-nav-item";

@Component({
  selector: 'app-main-nav-entry',
  templateUrl: './main-nav-entry.component.html',
  styleUrls: ['./main-nav-entry.component.scss']
})
export class MainNavEntryComponent {

  @Input() collapsed: boolean = false

  @Input() item: MainNavItem | undefined

  constructor() {
  }


}
