import { Component, computed, inject, input, output, resource, ChangeDetectionStrategy } from '@angular/core'
import { AudienceService } from '@open-event/portal'
import { AudienceFilterComponent as LibAudienceFilterComponent } from '@open-event/ui'
import { toPromise } from '@open-event/shared'
import { TriFilter } from '@open-event/core'

@Component({
  selector: 'portal-audience-filter',
  templateUrl: './audience-filter.component.html',
  imports: [LibAudienceFilterComponent],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class AudienceFilterComponent {
  state = input.required<TriFilter>()
  label = input<string>()
  toggled = output<string>()

  private audienceService = inject(AudienceService)

  private audienceResource = resource({
    loader: (p) => toPromise(this.audienceService.getAudiences(0, 100), p.abortSignal)
  })
  readonly audiences = computed(() => this.audienceResource.value()?.content ?? [])
}
