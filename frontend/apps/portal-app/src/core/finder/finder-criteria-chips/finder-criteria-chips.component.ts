import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { EventSearchRequest } from '@open-event/core'
import { getCategoryStyle } from '@open-event/ui'

interface CriteriaChip {
  key: string
  label: string
  hue: number
  out: boolean
}

// audiences have no own hue, use a warm neutral tone to tell them apart from categories
const AUDIENCE_HUE = 30

@Component({
  selector: 'portal-finder-criteria-chips',
  templateUrl: './finder-criteria-chips.component.html',
  styleUrl: './finder-criteria-chips.component.scss',
  imports: [MatIcon, TranslatePipe],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class FinderCriteriaChipsComponent {
  request = input.required<EventSearchRequest>()
  showAvailable = input(false)

  readonly chips = computed<CriteriaChip[]>(() => {
    const r = this.request()
    const category = (name: string, out: boolean) => ({ key: 'c' + name, label: name, hue: getCategoryStyle(name).hue, out })
    const audience = (name: string, out: boolean) => ({ key: 'a' + name, label: name, hue: AUDIENCE_HUE, out })
    return [
      ...(r.categories ?? []).map((c) => category(c, false)),
      ...(r.excludedCategories ?? []).map((c) => category(c, true)),
      ...(r.audiences ?? []).map((a) => audience(a, false)),
      ...(r.excludedAudiences ?? []).map((a) => audience(a, true))
    ]
  })
}
