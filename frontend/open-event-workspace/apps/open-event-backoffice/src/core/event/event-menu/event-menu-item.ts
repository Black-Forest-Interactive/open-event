export class EventMenuItem {
  constructor(
    public icon: string,
    public text: string,
    public callback: Function,
    public disabled: boolean = false,
    public permissions: string[] = []
  ) {
  }

  isAccessible(permission: string): boolean {
    if (this.permissions.length <= 0) return true
    return this.permissions.find(p => p === permission) != null
  }

  isIconAvailable() {
    return this.icon != null && this.icon.length > 0
  }

  handleClick() {
    this.callback()
  }
}
