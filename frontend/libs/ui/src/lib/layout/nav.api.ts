import { Principal } from '@open-event/shared'

export class NavItem {
  constructor(
    public url: string,
    public text: string,
    public icon: string,
    public permissions: string[] = []
  ) {}

  isAccessible(principal: Principal | undefined): boolean {
    if (this.permissions.length <= 0) return true
    if (!principal) return false
    return principal.roles.find((r) => this.permissions.find((p) => p === r) != null) != null
  }
}

export interface NavGroup {
  title?: string
  items: NavItem[]
}
