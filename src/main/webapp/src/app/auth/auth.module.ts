import {APP_INITIALIZER, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {KeycloakAngularModule, KeycloakService} from "keycloak-angular";
import {AuthService} from "./auth.service";
import {initializer} from "./app-init";
import {AuthGuard} from "./auth.guard";


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    KeycloakAngularModule
  ],
  providers: [
    AuthService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializer,
      multi: true,
      deps: [KeycloakService]
    },
    AuthGuard
  ]
})
export class AuthModule {
}
