import { Component, computed, input } from '@angular/core'

@Component({
  selector: 'lib-avatar',
  templateUrl: './avatar.component.html'
})
export class AvatarComponent {
  name = input.required<string>()
  size = input<'sm' | 'md' | 'lg'>('md')

  readonly initials = computed(() => {
    const parts = this.name().trim().split(/\s+/).filter((p) => p.length > 0)
    if (parts.length === 0) return ''
    if (parts.length === 1) return parts[0].charAt(0).toUpperCase()
    return (parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase()
  })
}
