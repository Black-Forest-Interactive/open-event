import { Component, computed, inject, OnDestroy, OnInit, resource } from '@angular/core'
import { CommonModule } from '@angular/common'
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { TranslatePipe } from '@ngx-translate/core'
import { Subject, switchMap, takeUntil, timer } from 'rxjs'
import { SearchService } from '@open-event/admin'
import { SearchOperatorInfo, SearchOperatorStats } from '@open-event/core'
import { MatCardModule } from '@angular/material/card'
import { MatProgressBarModule } from '@angular/material/progress-bar'
import { BoardComponent } from '../../shared/board/board.component'
import { toPromise } from '@open-event/shared'
import { MatChipsModule } from '@angular/material/chips'

@Component({
  selector: 'admin-search',
  imports: [MatCardModule, MatButtonModule, MatIconModule, MatProgressBarModule, MatChipsModule, CommonModule, TranslatePipe, BoardComponent],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent implements OnInit, OnDestroy {
  private service = inject(SearchService)

  private searchResource = resource({
    loader: (param) => {
      return toPromise(this.service.getAllInfo(), param.abortSignal)
    }
  })

  entries = computed(() => this.searchResource.value() ?? [])
  reloading = this.searchResource.isLoading

  private unsub = new Subject<void>()

  ngOnInit(): void {
    timer(0, 500)
      .pipe(
        takeUntil(this.unsub),
        switchMap(async () => this.searchResource.reload())
      )
      .subscribe()
  }

  ngOnDestroy(): void {
    this.unsub.next()
    this.unsub.complete()
  }

  setup(info: SearchOperatorInfo) {
    this.service.setup(info.key).subscribe()
  }

  getStatusClass(status: string): string {
    const baseClasses = 'transition-all'

    switch (status) {
      case 'IDLE':
        return `${baseClasses} !bg-gray-100 !text-gray-700`

      case 'UNKNOWN':
        return `${baseClasses} !bg-gray-200 !text-gray-800`

      case 'CREATE_INDEX':
        return `${baseClasses} !bg-orange-100 !text-orange-700`

      case 'INITIAL_LOAD':
        return `${baseClasses} !bg-yellow-100 !text-yellow-700`

      case 'READY':
        return `${baseClasses} !bg-green-100 !text-green-700`

      default:
        return baseClasses
    }
  }

  getStatusIcon(status: string): string {
    switch (status) {
      case 'IDLE':
        return 'pause_circle'

      case 'UNKNOWN':
        return 'help_outline'

      case 'CREATE_INDEX':
        return 'construction'

      case 'INITIAL_LOAD':
        return 'sync'

      case 'READY':
        return 'check_circle'

      default:
        return 'info'
    }
  }

  calculateProgress(stats: SearchOperatorStats): number {
    if (stats.total === 0) {
      return 0
    }
    return ((stats.successful + stats.failed) / stats.total) * 100
  }

  getProgressColor(status: string): 'primary' | 'accent' | 'warn' {
    switch (status) {
      case 'READY':
        return 'primary'

      case 'CREATE_INDEX':
      case 'INITIAL_LOAD':
        return 'accent'

      case 'IDLE':
      case 'UNKNOWN':
        return 'warn'

      default:
        return 'primary'
    }
  }
}
