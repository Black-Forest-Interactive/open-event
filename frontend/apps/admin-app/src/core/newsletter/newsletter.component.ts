import { Component, computed, inject, resource, signal } from '@angular/core'
import { MatCard } from '@angular/material/card'
import { PageEvent } from '@angular/material/paginator'
import { toPromise } from '@open-event/shared'
import { NotificationService } from '@open-event/admin'
import { BoardComponent, BoardToolbarActions } from '../../shared/board/board.component'
import { NewsletterStateService } from '../../shared/newsletter-state.service'
import { NewsletterStatusButtonComponent } from '../../shared/newsletter-status-button/newsletter-status-button.component'
import { NewsletterTableComponent } from './newsletter-table/newsletter-table.component'

@Component({
  selector: 'admin-newsletter',
  imports: [MatCard, BoardComponent, BoardToolbarActions, NewsletterStatusButtonComponent, NewsletterTableComponent],
  templateUrl: './newsletter.component.html'
})
export class NewsletterComponent {
  private service = inject(NotificationService)
  readonly newsletterState = inject(NewsletterStateService)

  private page = signal(0)
  private size = signal(25)

  private criteria = computed(() => ({ page: this.page(), size: this.size() }))

  private subscriberResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.getNewsletterSubscribers(p.params.page, p.params.size), p.abortSignal)
  })

  readonly data = computed(() => this.subscriberResource.value()?.content ?? [])
  readonly totalElements = computed(() => this.subscriberResource.value()?.totalSize ?? 0)
  readonly pageNumber = computed(() => this.subscriberResource.value()?.pageable?.number ?? 0)
  readonly pageSize = computed(() => this.subscriberResource.value()?.pageable?.size ?? 25)
  readonly reloading = this.subscriberResource.isLoading

  reload() {
    this.subscriberResource.reload()
  }

  handlePageChange(event: PageEvent) {
    this.page.set(event.pageIndex)
    this.size.set(event.pageSize)
  }
}
