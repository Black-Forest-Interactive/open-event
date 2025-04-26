import {inject} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {AuthGuardData, createAuthGuard} from "keycloak-angular";


const isAccessAllowed = async (
  route: ActivatedRouteSnapshot,
  _: RouterStateSnapshot,
  authData: AuthGuardData
): Promise<boolean | UrlTree> => {
  const {authenticated, grantedRoles} = authData;
  const requiredRoles = route.data['roles'];
  if (authenticated && (!Array.isArray(requiredRoles) || requiredRoles.length === 0)) {
    return true;
  }

  const hasRequiredRole = (role: string): boolean =>
    Object.values(grantedRoles.realmRoles).some((roles) => roles.includes(role));

  if (authenticated && hasRequiredRole(requiredRoles)) {
    return true;
  }

  const router = inject(Router);
  return router.parseUrl('/forbidden');
};

export const canActivateAuthRole = createAuthGuard<CanActivateFn>(isAccessAllowed);

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthGuard extends KeycloakAuthGuard {
//
//   constructor(
//     protected override readonly router: Router,
//     protected readonly keycloak: KeycloakService
//   ) {
//     super(router, keycloak);
//   }
//
//   public async isAccessAllowed(
//     route: ActivatedRouteSnapshot,
//     state: RouterStateSnapshot
//   ) {
//     // Force the user to log in if currently unauthenticated.
//     if (!this.authenticated) {
//       await this.keycloak.login({
//         redirectUri: window.location.origin + state.url
//       });
//     }
//
//     // Get the roles required from the route.
//     const requiredRoles = route.data['roles'];
//
//     // Allow the user to proceed if no additional roles are required to access the route.
//     if (!Array.isArray(requiredRoles) || requiredRoles.length === 0) {
//       return true;
//     }
//
//     // Allow the user to proceed if all the required roles are present.
//     return requiredRoles.every((role) => this.roles.includes(role));
//   }
//
// }
