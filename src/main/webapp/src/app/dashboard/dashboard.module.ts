import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard/dashboard.component';
import {MaterialModule} from "../material/material.module";
import {AppRoutingModule} from "../app-routing.module";
import {LoadingScreenComponent} from './loading-screen/loading-screen.component';
import {TranslateModule} from "@ngx-translate/core";
import {ConfirmLogoutDialogComponent} from './confirm-logout-dialog/confirm-logout-dialog.component';
import {MainNavEntryComponent} from './main-nav-entry/main-nav-entry.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';


@NgModule({
  declarations: [
    DashboardComponent,
    LoadingScreenComponent,
    ConfirmLogoutDialogComponent,
    MainNavEntryComponent,
    PageNotFoundComponent
  ],
  exports: [
    DashboardComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    AppRoutingModule,
    TranslateModule
  ]
})
export class DashboardModule {
}
