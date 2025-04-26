import {effect, inject, Injectable} from '@angular/core';
import {KEYCLOAK_EVENT_SIGNAL, KeycloakEventType, ReadyArgs, typeEventArgs} from "keycloak-angular";
import {Principal} from "./principal";
import {environment} from "../../../../../apps/open-event-app/src/environments/environment";
import Keycloak from "keycloak-js";

// @deprecated due to deprecated keycloak service
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private principal: Principal | undefined
  private authenticated = false

  constructor(private readonly keycloak: Keycloak) {
    const keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL)

    effect(() => {
      const keycloakEvent = keycloakSignal();

      if (keycloakEvent.type === KeycloakEventType.Ready) {
        this.authenticated = typeEventArgs<ReadyArgs>(keycloakEvent.args)
        let token = this.keycloak.tokenParsed
        if (token) {
          this.setPrincipal(token)
        } else {
          this.clearPrincipal()
        }
      }

      if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
        this.authenticated = false
        this.clearPrincipal()
      }
    });
  }


  public logout() {
    this.keycloak.logout({redirectUri: environment.logoutUrl}).then()
  }

  public getPrincipal(): Principal | undefined {
    return this.principal
  }

  hasRole(...roles: String[]): boolean {
    if (!this.principal) return false
    return this.principal.roles.find(r => roles.find(p => r === p)) != null
  }

  private clearPrincipal() {
    console.log('Clear principal');
    this.principal = undefined;
  }

  private setPrincipal(token: any) {
    // console.info(JSON.stringify(token));
    const id = token["sub"];
    const email = token["email"];
    const username = token["preferred_username"];
    const given_name = token["given_name"];
    const family_name = token["family_name"];
    const roles = token["realm_access"]["roles"];

    this.principal = new Principal(id, email, username, given_name, family_name, roles);
    // console.log('Set principal to ' + JSON.stringify(this.principal));
  }


  getRoles(): string[] {
    if (!this.principal) return []
    return this.principal.roles.sort();
  }
}
