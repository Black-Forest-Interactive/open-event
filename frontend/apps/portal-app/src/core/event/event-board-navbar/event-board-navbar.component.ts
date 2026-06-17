import { Component, computed, inject } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { EventBoardService } from '../event-board.service'

@Component({
  selector: 'portal-event-board-navbar',
  templateUrl: './event-board-navbar.component.html',
  imports: [MatIcon, TranslatePipe],
  standalone: true
})
export class EventBoardNavbarComponent {
  protected service = inject(EventBoardService)

  readonly isExplore = computed(() => this.service.navView() === 'all' && this.service.layout() !== 'calendar')
  readonly isCalendar = computed(() => this.service.layout() === 'calendar')
  readonly isSaved = computed(() => this.service.navView() === 'saved')
  readonly isRegs = computed(() => this.service.navView() === 'regs')

  selectExplore() {
    this.service.setNavView('all')
    if (this.service.layout() === 'calendar') this.service.setLayout('cards')
  }

  selectCalendar() {
    this.service.setNavView('all')
    this.service.setLayout('calendar')
  }

  selectSaved() {
    this.service.setNavView('saved')
    if (this.service.layout() === 'calendar') this.service.setLayout('cards')
  }

  selectRegs() {
    this.service.setNavView('regs')
    if (this.service.layout() === 'calendar') this.service.setLayout('cards')
  }
}
