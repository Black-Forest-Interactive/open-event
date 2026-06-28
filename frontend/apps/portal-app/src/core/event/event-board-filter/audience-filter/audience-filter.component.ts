import { Component, computed, inject, input, output, resource } from '@angular/core'
import { Audience } from '@open-event/core'
import { AudienceService } from '@open-event/portal'
import { toPromise } from '@open-event/shared'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-audience-filter',
  templateUrl: './audience-filter.component.html',
  imports: [TranslatePipe],
  standalone: true
})
export class AudienceFilterComponent {
  selected = input.required<Set<string>>()
  toggle = output<string>()

  private audienceService = inject(AudienceService)

  private audienceResource = resource({
    loader: (p) => toPromise(this.audienceService.getAudiences(0, 100), p.abortSignal)
  })
  readonly audiences = computed<Audience[]>(() => this.audienceResource.value()?.content ?? [])
}
