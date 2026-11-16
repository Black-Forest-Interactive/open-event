import { ChangeDetectionStrategy, Component, computed, input, output } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { TriFilter } from '@open-event/core'

interface ActiveChip {
  kind: 'category' | 'audience'
  key: string
  out: boolean
}

@Component({
  selector: 'portal-active-filter-chips',
  templateUrl: './active-filter-chips.component.html',
  styleUrl: './active-filter-chips.component.scss',
  imports: [MatIcon, TranslatePipe],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class ActiveFilterChipsComponent {
  categoryFilter = input.required<TriFilter>()
  audienceFilter = input.required<TriFilter>()
  showAvailableOnly = input(false)

  removeCategory = output<string>()
  removeAudience = output<string>()
  clearAvailable = output<void>()
  resetFilter = output<void>()

  readonly chips = computed<ActiveChip[]>(() => {
    const c = this.categoryFilter()
    const a = this.audienceFilter()
    return [...Object.keys(c).map((key) => ({ kind: 'category' as const, key, out: c[key] === 'out' })), ...Object.keys(a).map((key) => ({ kind: 'audience' as const, key, out: a[key] === 'out' }))]
  })
  readonly visible = computed(() => this.chips().length > 0 || this.showAvailableOnly())

  remove(chip: ActiveChip) {
    if (chip.kind === 'category') this.removeCategory.emit(chip.key)
    else this.removeAudience.emit(chip.key)
  }
}
