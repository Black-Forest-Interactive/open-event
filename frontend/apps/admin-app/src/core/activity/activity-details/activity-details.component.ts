import { Component, computed, inject, resource, signal } from '@angular/core'
import { toPromise } from '@open-event/shared'
import { ActivityService } from '@open-event/admin'
import { ActivatedRoute } from '@angular/router'
import { Location } from '@angular/common'
import { BoardComponent } from '../../../shared/board/board.component'

@Component({
  selector: 'admin-activity-details',
  imports: [BoardComponent],
  templateUrl: './activity-details.component.html',
  styleUrl: './activity-details.component.scss'
})
export class ActivityDetailsComponent {
  id = signal(-1)
  private service = inject(ActivityService)
  private route = inject(ActivatedRoute)
  private location = inject(Location)
  private activityResource = resource({
    params: this.id,
    loader: (param) => toPromise(this.service.getActivity(param.params), param.abortSignal)
  })

  readonly activity = computed(this.activityResource.value ?? undefined)
  readonly loading = this.activityResource.isLoading
  readonly error = this.activityResource.error

  constructor() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id')
      if (id) this.id.set(+id)
    })
  }

  back() {
    this.location.back()
  }
  reload() {
    this.activityResource.reload()
  }
}
