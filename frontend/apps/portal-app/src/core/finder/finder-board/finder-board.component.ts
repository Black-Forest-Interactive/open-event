import { ChangeDetectionStrategy, Component, computed, inject, resource } from '@angular/core'
import { DatePipe } from '@angular/common'
import { Router } from '@angular/router'
import { MatButton } from '@angular/material/button'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { FinderInfo } from '@open-event/core'
import { FinderService } from '@open-event/portal'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { EventBoardNavbarComponent } from '../../event/event-board-navbar/event-board-navbar.component'
import { FinderCriteriaChipsComponent } from '../finder-criteria-chips/finder-criteria-chips.component'
import { FinderEditorLauncher } from '../finder-editor-sheet/finder-editor-launcher'
import { finderInfoStats, FinderStats, progressPercent } from '../finder.util'

@Component({
  selector: 'portal-finder-board',
  templateUrl: './finder-board.component.html',
  styleUrls: ['../finder-shared.scss', './finder-board.component.scss'],
  imports: [EventBoardNavbarComponent, MatButton, MatCard, MatIcon, DatePipe, TranslatePipe, LoadingBarComponent, FinderCriteriaChipsComponent],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class FinderBoardComponent {
  private service = inject(FinderService)
  private router = inject(Router)
  private editor = inject(FinderEditorLauncher)
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)

  private finderResource = resource({
    loader: (p) => toPromise(this.service.getFinders(0, 100), p.abortSignal)
  })
  readonly reloading = this.finderResource.isLoading
  readonly finders = computed(() =>
    (this.finderResource.value()?.content ?? []).map((info) => {
      const stats = finderInfoStats(info)
      return { info, stats, percent: progressPercent(stats), actionKey: actionKey(stats) }
    })
  )

  open(finder: FinderInfo) {
    this.router.navigate(['/finder', finder.id]).then()
  }

  create() {
    this.editor.open().subscribe((finder) => {
      if (!finder) return
      this.translate.get('finder.message.created').subscribe((t) => this.toast.success(t))
      this.router.navigate(['/finder', finder.id]).then()
    })
  }
}

function actionKey(stats: FinderStats): string {
  if (stats.open === 0) return 'finder.action.review'
  return stats.rated > 0 ? 'finder.action.resume' : 'finder.action.start'
}
