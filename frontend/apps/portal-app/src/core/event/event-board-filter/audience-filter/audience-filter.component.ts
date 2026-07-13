import { Component, computed, inject, input, output, resource, ChangeDetectionStrategy } from '@angular/core'
import { AudienceService } from '@open-event/portal'
import { AudienceFilterComponent as LibAudienceFilterComponent } from '@open-event/ui'
import { toPromise } from '@open-event/shared'

@Component({
  selector: 'portal-audience-filter',
  templateUrl: './audience-filter.component.html',
  imports: [LibAudienceFilterComponent],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true
})
export class AudienceFilterComponent {
  selected = input.required<Set<string>>()
  toggled = output<string>()

  private audienceService = inject(AudienceService)

  private audienceResource = resource({
    loader: (p) => toPromise(this.audienceService.getAudiences(0, 100), p.abortSignal)
  })
  readonly audiences = computed(() => this.audienceResource.value()?.content ?? [])
}
