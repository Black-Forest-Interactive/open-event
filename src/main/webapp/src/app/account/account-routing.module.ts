import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AccountBoardComponent} from "./account-board/account-board.component";
import {AccountProfileComponent} from "./account-profile/account-profile.component";
import {AccountPreferencesComponent} from "./account-preferences/account-preferences.component";
import {AccountAddressComponent} from "./account-address/account-address.component";
import {AccountActivityComponent} from "./account-activity/account-activity.component";

const routes: Routes = [
  {
    path: '',
    component: AccountBoardComponent,
    children: [
      {path: '', redirectTo: 'profile', pathMatch: "full"},
      {path: 'profile', component: AccountProfileComponent},
      {path: 'address', component: AccountAddressComponent},
      {path: 'preferences', component: AccountPreferencesComponent},
      {path: 'activity', component: AccountActivityComponent},
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AccountRoutingModule {
}
