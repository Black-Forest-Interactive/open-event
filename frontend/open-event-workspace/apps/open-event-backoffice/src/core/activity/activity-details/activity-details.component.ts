import {Component, computed, resource, signal} from '@angular/core';
import {toPromise} from "@open-event-workspace/shared";
import {ActivityService} from "@open-event-workspace/backoffice";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {BoardComponent} from "../../../shared/board/board.component";

@Component({
  selector: 'app-activity-details',
  imports: [
    BoardComponent
  ],
  templateUrl: './activity-details.component.html',
  styleUrl: './activity-details.component.scss'
})
export class ActivityDetailsComponent {
  id = signal(-1)

  activityResource = resource({
    request: this.id,
    loader: (param) => {
      return toPromise(this.service.getActivity(param.request))
    }
  })


  activity = computed(this.activityResource.value ?? undefined)
  loading = this.activityResource.isLoading
  error = this.activityResource.error

  constructor(private service: ActivityService, private route: ActivatedRoute, private location: Location) {
    this.route.paramMap.subscribe(params => {
      let id = params.get('id')!
      this.id.set(+id)
    })

  }

  back() {
    this.location.back()
  }
}
