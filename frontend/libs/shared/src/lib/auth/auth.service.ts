import { effect, inject, Injectable, signal } from '@angular/core'
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType, ReadyArgs, typeEventArgs } from 'keycloak-angular'
import { Principal } from './principal'
import Keycloak from 'keycloak-js'
import { ENVIRONMENT } from '../environment.token'

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  readonly principal = signal<Principal | undefined>(undefined)
  readonly authenticated = signal(false)

  private environment = inject(ENVIRONMENT)
  private readonly keycloak = inject(Keycloak)

  constructor() {
    const keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL)

    effect(() => {
      const keycloakEvent = keycloakSignal()

      if (keycloakEvent.type === KeycloakEventType.Ready) {
        this.authenticated.set(typeEventArgs<ReadyArgs>(keycloakEvent.args))
        const token = this.keycloak.tokenParsed
        if (token) {
          this.setPrincipal(token)
        } else {
          this.clearPrincipal()
        }
      }

      if (keycloakEvent.type === KeycloakEventType.AuthLogout) {
        this.authenticated.set(false)
        this.clearPrincipal()
      }
    })
  }

  public logout() {
    this.keycloak.logout({ redirectUri: this.environment.logoutUrl }).then()
  }

  public getPrincipal(): Principal | undefined {
    return this.principal()
  }

  hasRole(...roles: string[]): boolean {
    const principal = this.principal()
    if (!principal) return false
    return principal.roles.find((r) => roles.find((p) => r === p)) != null
  }

  private clearPrincipal() {
    console.log('Clear principal')
    this.principal.set(undefined)
  }

  private setPrincipal(token: any) {
    // console.info(JSON.stringify(token));
    const id = token['sub']
    const email = token['email']
    const username = token['preferred_username']
    const given_name = token['given_name']
    const family_name = token['family_name']
    const roles = token['realm_access']['roles']

    this.principal.set(new Principal(id, email, username, given_name, family_name, roles))
    // console.log('Set principal to ' + JSON.stringify(this.principal));
  }
}
