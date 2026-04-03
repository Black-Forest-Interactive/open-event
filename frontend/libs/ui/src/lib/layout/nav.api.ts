export interface NavItem {
  url: string
  text: string
  icon: string
  permissions?: string[]
}

export interface NavGroup {
  title?: string
  items: NavItem[]
}
