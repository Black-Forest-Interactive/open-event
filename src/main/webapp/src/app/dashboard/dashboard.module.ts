import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {DashboardComponent} from './dashboard/dashboard.component';
import {MaterialModule} from "../material/material.module";
import {AppRoutingModule} from "../app-routing.module";
import {LoadingScreenComponent} from './loading-screen/loading-screen.component';
import {TranslateModule} from "@ngx-translate/core";
import {ConfirmLogoutDialogComponent} from './confirm-logout-dialog/confirm-logout-dialog.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {MainMenuComponent} from './main-menu/main-menu.component';
import {DashboardToolbarComponent} from './dashboard-toolbar/dashboard-toolbar.component';
import {AccountModule} from "../account/account.module";
import {GravatarModule} from "ngx-gravatar";
import {ActivityModule} from "../activity/activity.module";


@NgModule({
  declarations: [
    DashboardComponent,
    LoadingScreenComponent,
    ConfirmLogoutDialogComponent,
    PageNotFoundComponent,
    MainMenuComponent,
    DashboardToolbarComponent,
  ],
  exports: [
    DashboardComponent
  ],
    imports: [
        CommonModule,
        MaterialModule,
        AppRoutingModule,
        TranslateModule,
        AccountModule,
        GravatarModule,
        ActivityModule
    ]
})
export class DashboardModule {
}
