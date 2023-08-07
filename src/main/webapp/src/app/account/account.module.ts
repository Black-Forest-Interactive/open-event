import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AccountComponent} from './account/account.component';
import {AccountDisplayNamePipe} from './account-display-name.pipe';
import {GravatarModule} from "ngx-gravatar";


@NgModule({
  declarations: [
    AccountComponent,
    AccountDisplayNamePipe
  ],
  exports: [
    AccountComponent,
    AccountDisplayNamePipe
  ],
  imports: [
    CommonModule,
    GravatarModule
  ]
})
export class AccountModule { }
