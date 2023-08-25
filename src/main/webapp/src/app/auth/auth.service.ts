import {Injectable} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {Principal} from "./principal";

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  public static SETTINGS_READ = "openevent.settings.read"
  public static SETTINGS_WRITE = "openevent.settings.write"

  public static CATEGORY_READ = "openevent.category.read"
  public static CATEGORY_WRITE = "openevent.category.write"

  public static REGISTRATION_READ = "openevent.registration.read"
  public static REGISTRATION_WRITE = "openevent.registration.write"
  public static REGISTRATION_MANAGE = "openevent.registration.manage"
  public static REGISTRATION_ADMIN = "openevent.registration.admin"

  public static CACHE_READ = "openevent.cache.read"
  public static CACHE_WRITE = "openevent.cache.write"

  public static EVENT_READ = "openevent.event.read"
  public static EVENT_WRITE = "openevent.event.write"
  public static EVENT_ADMIN = "openevent.event.admin"


  private principal: Principal | undefined;

  constructor(private keycloak: KeycloakService) {
    try {
      let token = this.keycloak.getKeycloakInstance().tokenParsed;
      if (token == null) {
        this.clearPrincipal();
      } else {
        this.setPrincipal(token);
      }
    } catch (e) {
      console.log('Failed to load user details', e);
      this.clearPrincipal();
    }
  }


  public logout() {
    this.keycloak.logout("/").then()
  }

  public getPrincipal(): Principal | undefined {
    return this.principal
  }

  private clearPrincipal() {
    console.log('Clear principal');
    this.principal = undefined;
  }

  private setPrincipal(token: any) {
    console.info(JSON.stringify(token));
    const id = token["sub"];
    const email = token["email"];
    const username = token["preferred_username"];
    const given_name = token["given_name"];
    const family_name = token["family_name"];
    const roles = token["realm_access"]["roles"];

    this.principal = new Principal(id, email, username, given_name, family_name, roles);
    console.log('Set principal to ' + JSON.stringify(this.principal));
  }

  hasRole(...roles: String[]): boolean {
    if (!this.principal) return false
    return this.principal.roles.find(r => roles.find(p => r === p)) != null
  }


}
