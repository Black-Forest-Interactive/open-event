import {Component} from '@angular/core';
import {HotToastService} from "@ngxpert/hot-toast";
import {AuthService} from "../../auth/auth.service";
import {SearchService} from "../../search/model/search.service";

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
    private searchService: SearchService,
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
    this.searchService.setupAccounts().subscribe(_ => this.toastService.success("Account index builder has been started"))
  }

  buildCategoryIndex() {
    this.searchService.setupCategories().subscribe(_ => this.toastService.success("Category index builder has been started"))
  }

  buildEventIndex() {
    this.searchService.setupEvents().subscribe(_ => this.toastService.success("Event index builder has been started"))
  }
}
