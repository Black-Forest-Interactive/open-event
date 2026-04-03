import { Component, computed, resource, signal, inject } from '@angular/core'
import { toPromise } from '@open-event/shared'
import { IssueService } from '@open-event/admin'
import { ActivatedRoute } from '@angular/router'
import { Location } from '@angular/common'
import { BoardComponent } from '../../../shared/board/board.component'
import { IssueCardComponent } from '../issue-card/issue-card.component'

@Component({
  selector: 'admin-issue-details',
  imports: [BoardComponent, IssueCardComponent],
  templateUrl: './issue-details.component.html',
  styleUrl: './issue-details.component.scss'
})
export class IssueDetailsComponent {
  private service = inject(IssueService);
  private route = inject(ActivatedRoute);
  private location = inject(Location);

  id = signal(-1)

  issueResource = resource({
    params: this.id,
    loader: (param) => {
      return toPromise(this.service.getIssue(param.params))
    }
  })

  issue = computed(this.issueResource.value ?? undefined)
  loading = this.issueResource.isLoading
  error = this.issueResource.error

  constructor() {
    this.route.paramMap.subscribe((params) => {
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
