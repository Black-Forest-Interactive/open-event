import { Component, computed, inject, OnDestroy, OnInit, resource, signal, ChangeDetectionStrategy } from '@angular/core'
import { DatePipe } from '@angular/common'
import { MailJob } from '@open-event/core'
import { MailService } from '@open-event/admin'
import { toPromise } from '@open-event/shared'
import { MatCard } from '@angular/material/card'
import { MatIconModule } from '@angular/material/icon'
import { MatChipsModule } from '@angular/material/chips'
import { MatButtonModule } from '@angular/material/button'
import { MatPaginator, PageEvent } from '@angular/material/paginator'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { Subject, switchMap, takeUntil, timer } from 'rxjs'
import { BoardComponent, BoardFilters } from '../../shared/board/board.component'

@Component({
  selector: 'admin-mail',
  imports: [MatCard, MatIconModule, MatChipsModule, MatButtonModule, MatPaginator, DatePipe, TranslatePipe, BoardComponent, BoardFilters],
  templateUrl: './mail.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './mail.component.scss'
})
export class MailComponent implements OnInit, OnDestroy {
  readonly FILTERS = ['all', 'FINISHED', 'QUEUED', 'RETRY', 'FAILED']

  private service = inject(MailService)
  private toast = inject(HotToastService)
  private translateService = inject(TranslateService)

  private page = signal(0)
  private size = signal(25)
  readonly typeFilter = signal('all')
  private criteria = computed(() => ({ page: this.page(), size: this.size(), status: this.typeFilter() }))
  private jobsResource = resource({
    params: this.criteria,
    loader: (p) => toPromise(this.service.getJobs(p.params.page, p.params.size, '', p.params.status === 'all' ? '' : p.params.status), p.abortSignal)
  })
  private result = computed(() => this.jobsResource.value())

  readonly jobs = computed(() => this.result()?.content ?? [])
  readonly pageNumber = computed(() => this.result()?.pageable.number ?? 0)
  readonly pageSize = computed(() => this.result()?.pageable.size ?? 25)
  readonly totalElements = computed(() => this.result()?.totalSize ?? 0)
  readonly reloading = this.jobsResource.isLoading

  private selectedJobId = signal<number | undefined>(undefined)
  readonly selected = computed(() => this.jobs().find((j) => j.id === this.selectedJobId()) ?? this.jobs()[0])

  private historyCriteria = computed(() => this.selected()?.id)
  private historyResource = resource({
    params: this.historyCriteria,
    loader: (p) => (p.params ? toPromise(this.service.getJobHistory(p.params, 0, 50), p.abortSignal) : Promise.resolve(undefined))
  })
  readonly stages = computed(() => this.historyResource.value()?.content ?? [])
  readonly historyLoading = this.historyResource.isLoading

  private unsub = new Subject<void>()

  ngOnInit() {
    timer(15000, 15000)
      .pipe(
        takeUntil(this.unsub),
        switchMap(async () => this.reload())
      )
      .subscribe()
  }

  ngOnDestroy() {
    this.unsub.next()
    this.unsub.complete()
  }

  selectJob(id: number) {
    this.selectedJobId.set(id)
  }

  resend(job: MailJob) {
    this.service.retryFailedJob(job.id).subscribe({
      next: () => {
        this.translateService.get('mail.message.resent').subscribe((t) => this.toast.success(t))
        this.reload()
      },
      error: () => this.translateService.get('mail.message.resentFailed').subscribe((t) => this.toast.error(t))
    })
  }

  setTypeFilter(type: string) {
    this.typeFilter.set(type)
  }

  reload() {
    this.jobsResource.reload()
  }

  handlePageChange(event: PageEvent) {
    this.size.set(event.pageSize)
    this.page.set(event.pageIndex)
  }

  statusIcon(status: string): string {
    switch (status) {
      case 'FINISHED':
        return 'check_circle'
      case 'QUEUED':
        return 'schedule'
      case 'RETRY':
        return 'autorenew'
      case 'FAILED':
        return 'error'
      default:
        return 'info'
    }
  }

  statusClass(status: string): string {
    const base = 'transition-all'
    switch (status) {
      case 'FINISHED':
        return `${base} !bg-green-100 !text-green-700`
      case 'QUEUED':
        return `${base} !bg-gray-100 !text-gray-700`
      case 'RETRY':
        return `${base} !bg-orange-100 !text-orange-700`
      case 'FAILED':
        return `${base} !bg-red-100 !text-red-700`
      default:
        return base
    }
  }

  stageIcon(message: string): string {
    if (message.includes('finished')) return 'check'
    if (message.includes('failed')) return 'error'
    if (message.includes('started')) return 'send'
    return 'schedule'
  }

  stageClass(message: string): string {
    const base = 'transition-all'
    if (message.includes('finished')) return `${base} !bg-green-100 !text-green-700`
    if (message.includes('failed')) return `${base} !bg-red-100 !text-red-700`
    if (message.includes('started')) return `${base} !bg-orange-100 !text-orange-700`
    return `${base} !bg-gray-100 !text-gray-700`
  }
}
