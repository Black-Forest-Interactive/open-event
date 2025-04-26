import {Component, computed, resource, signal} from '@angular/core';
import {toPromise} from "@open-event-workspace/shared";
import {IssueService} from "@open-event-workspace/backoffice";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";
import {BoardComponent} from "../../../shared/board/board.component";
import {IssueCardComponent} from "../issue-card/issue-card.component";

@Component({
  selector: 'app-issue-details',
  imports: [
    BoardComponent,
    IssueCardComponent,
  ],
  templateUrl: './issue-details.component.html',
  styleUrl: './issue-details.component.scss'
})
export class IssueDetailsComponent {
  id = signal(-1)

  issueResource = resource({
    request: this.id,
    loader: (param) => {
      return toPromise(this.service.getIssue(param.request))
    }
  })


  issue = computed(this.issueResource.value ?? undefined)
  loading = this.issueResource.isLoading
  error = this.issueResource.error

  constructor(private service: IssueService, private route: ActivatedRoute, private location: Location) {
    this.route.paramMap.subscribe(params => {
      let id = params.get('id')!
      this.id.set(+id)
    })

  }

  back() {
    this.location.back()
  }

  changeStatus(status: string) {
    let i = this.issue()
    if (!i) return
    this.service.updateStatus(i.id, status).subscribe(() => this.issueResource.reload())
  }
}
