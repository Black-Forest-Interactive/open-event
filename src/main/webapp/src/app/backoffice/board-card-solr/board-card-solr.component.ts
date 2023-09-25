import {Component} from '@angular/core';
import {AccountService} from "../../account/model/account.service";
import {CategoryService} from "../../category/model/category.service";
import {EventService} from "../../event/model/event.service";
import {HotToastService} from "@ngneat/hot-toast";
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: 'app-board-card-solr',
  templateUrl: './board-card-solr.component.html',
  styleUrls: ['./board-card-solr.component.scss']
})
export class BoardCardSolrComponent {

  isAccountAdmin: boolean = false
  isCategoryAdmin: boolean = false
  isEventAdmin: boolean = false


  constructor(
    private accountService: AccountService,
    private categoryService: CategoryService,
    private eventService: EventService,
    private toastService: HotToastService,
    private authService: AuthService
  ) {
  }

  ngOnInit() {
    this.isAccountAdmin = this.authService.hasRole(AuthService.ACCOUNT_ADMIN)
    this.isCategoryAdmin = this.authService.hasRole(AuthService.CATEGORY_ADMIN)
    this.isEventAdmin = this.authService.hasRole(AuthService.EVENT_ADMIN)
  }

  buildAccountIndex() {
    this.accountService.buildIndex().subscribe(_ => this.toastService.success("Account index builder has been started"))
  }

  buildCategoryIndex() {
    this.categoryService.buildIndex().subscribe(_ => this.toastService.success("Category index builder has been started"))
  }

  buildEventIndex() {
    this.eventService.buildIndex().subscribe(_ => this.toastService.success("Event index builder has been started"))
  }
}
