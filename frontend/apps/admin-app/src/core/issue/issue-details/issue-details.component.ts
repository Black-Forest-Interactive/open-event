import { Component, computed, inject, resource, signal } from '@angular/core'
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
  private service = inject(IssueService)
  private route = inject(ActivatedRoute)
  private location = inject(Location)

  id = signal(-1)

  private issueResource = resource({
    params: this.id,
    loader: (param) => toPromise(this.service.getIssue(param.params), param.abortSignal)
  })

  readonly issue = computed(this.issueResource.value ?? undefined)
  readonly loading = this.issueResource.isLoading
  readonly error = this.issueResource.error

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
    this.issueResource.reload()
  }

  changeStatus(status: string) {
    const i = this.issue()
    if (!i) return
    this.service.updateStatus(i.id, status).subscribe(() => this.issueResource.reload())
  }
}
