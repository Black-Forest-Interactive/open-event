import {AuthService} from "../../auth/auth.service";

export class MainNavItem {
  constructor(
    public url: string,
    public text: string,
    public icon: string,
    public permissions: string[] = []
  ) {
  }

  isAccessible(service: AuthService): boolean {
    if (this.permissions.length <= 0) return true
    let principal = service.getPrincipal()
    if (!principal) return false
    return principal.roles.find(r => this.permissions.find(p => p === r) != null) != null
  }
}
