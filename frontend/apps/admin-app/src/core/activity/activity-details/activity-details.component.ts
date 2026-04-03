import { Component, computed, resource, signal, inject } from '@angular/core'
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
  private service = inject(ActivityService);
  private route = inject(ActivatedRoute);
  private location = inject(Location);

  id = signal(-1)

  activityResource = resource({
    params: this.id,
    loader: (param) => {
      return toPromise(this.service.getActivity(param.params))
    }
  })

  activity = computed(this.activityResource.value ?? undefined)
  loading = this.activityResource.isLoading
  error = this.activityResource.error

  constructor() {
    this.route.paramMap.subscribe((params) => {
      let id = params.get('id')!
      this.id.set(+id)
    })
  }

  back() {
    this.location.back()
  }
}
