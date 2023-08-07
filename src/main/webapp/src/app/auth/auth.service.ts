import {Injectable} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {Principal} from "./principal";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

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
    const roles = token["resource_access"]["open-church-backend"]["roles"];

    this.principal = new Principal(id, email, username, given_name, family_name, roles);
    console.log('Set principal to ' + JSON.stringify(this.principal));
  }

}
