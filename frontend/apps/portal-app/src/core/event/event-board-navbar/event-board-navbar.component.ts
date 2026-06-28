import { Component, computed, inject } from '@angular/core'
import { NavigationEnd, Router, RouterLink } from '@angular/router'
import { toSignal } from '@angular/core/rxjs-interop'
import { filter, map, startWith } from 'rxjs'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'portal-event-board-navbar',
  templateUrl: './event-board-navbar.component.html',
  imports: [MatIcon, RouterLink, TranslatePipe],
  standalone: true
})
export class EventBoardNavbarComponent {
  private router = inject(Router)

  private currentUrl = toSignal(
    this.router.events.pipe(
      filter((e) => e instanceof NavigationEnd),
      map((e) => (e as NavigationEnd).urlAfterRedirects),
      startWith(this.router.url)
    ),
    { initialValue: this.router.url }
  )

  readonly isSaved = computed(() => this.currentUrl().includes('/event/saved'))
  readonly isRegs = computed(() => this.currentUrl().includes('/event/regs'))
  readonly isOwn = computed(() => this.currentUrl().includes('/event/own'))
  readonly isExplore = computed(() => !this.isSaved() && !this.isRegs() && !this.isOwn())
}
