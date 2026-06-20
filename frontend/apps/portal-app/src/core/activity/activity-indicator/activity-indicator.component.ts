import { Component, inject, output, signal } from '@angular/core'
import { ActivityService } from '@open-event/portal'
import { interval, startWith, switchMap } from 'rxjs'
import { takeUntilDestroyed } from '@angular/core/rxjs-interop'
import { MatBadge } from '@angular/material/badge'
import { MatIcon } from '@angular/material/icon'
import { MatIconButton } from '@angular/material/button'
import { RouterLink } from '@angular/router'

@Component({
  selector: 'portal-activity-indicator',
  imports: [MatBadge, MatIcon, MatIconButton, RouterLink],
  templateUrl: './activity-indicator.component.html',
  styleUrl: './activity-indicator.component.scss'
})
export class ActivityIndicatorComponent {
  readonly unreadMessages = signal<number>(0)
  readonly clicked = output<MouseEvent>()
  private service = inject(ActivityService)

  constructor() {
    interval(10000)
      .pipe(
        startWith(0),
        switchMap(() => this.service.unreadAmount()),
        takeUntilDestroyed()
      )
      .subscribe((value) => this.unreadMessages.set(value))
  }
}
