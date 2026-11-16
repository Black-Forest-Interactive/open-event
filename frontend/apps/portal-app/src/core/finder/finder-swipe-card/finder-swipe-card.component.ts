import { ChangeDetectionStrategy, Component, computed, input, output, signal } from '@angular/core'
import { DatePipe } from '@angular/common'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { EventSearchEntry, FinderAnswer } from '@open-event/core'
import { AccountDisplayNamePipe, AvatarComponent, CategoryChipComponent, getCategoryStyle, RegistrationStatusComponent } from '@open-event/ui'
import { durationMinutes, plainText } from '../finder.util'

export type SwipeExit = 'right' | 'left' | 'down'

/** Drag distance in px after which a released card counts as rated. */
const SWIPE_THRESHOLD = 110

@Component({
  selector: 'portal-finder-swipe-card',
  templateUrl: './finder-swipe-card.component.html',
  styleUrl: './finder-swipe-card.component.scss',
  imports: [DatePipe, MatButton, MatIcon, TranslatePipe, AccountDisplayNamePipe, AvatarComponent, CategoryChipComponent, RegistrationStatusComponent],
  changeDetection: ChangeDetectionStrategy.Eager,
  host: {
    '[class.is-drag]': 'dragging()',
    '[class.is-exit-right]': "exiting() === 'right'",
    '[class.is-exit-left]': "exiting() === 'left'",
    '[class.is-exit-down]': "exiting() === 'down'",
    '[style.transform]': 'transform()',
    '(pointerdown)': 'down($event)',
    '(pointermove)': 'move($event)',
    '(pointerup)': 'up()',
    '(pointercancel)': 'up()'
  }
})
export class FinderSwipeCardComponent {
  entry = input.required<EventSearchEntry>()
  exiting = input<SwipeExit | null>(null)
  rate = output<FinderAnswer>()
  openEvent = output<void>()

  readonly dx = signal(0)
  readonly dragging = signal(false)
  private startX: number | null = null

  readonly media = computed(() => getCategoryStyle(this.entry().categories[0] ?? ''))
  readonly duration = computed(() => durationMinutes(this.entry()))
  readonly longText = computed(() => plainText(this.entry().longText))
  readonly hasRegistration = computed(() => this.entry().maxGuestAmount > 0)
  readonly yesOpacity = computed(() => Math.max(0, Math.min(1, this.dx() / SWIPE_THRESHOLD)))
  readonly noOpacity = computed(() => Math.max(0, Math.min(1, -this.dx() / SWIPE_THRESHOLD)))
  readonly transform = computed(() => {
    if (this.exiting()) return null
    const dx = this.dx()
    if (dx === 0) return null
    const rotation = Math.max(-12, Math.min(12, dx / 18))
    return `translateX(${dx}px) rotate(${rotation}deg)`
  })

  down(event: PointerEvent) {
    if (event.button !== 0 || this.exiting()) return
    if ((event.target as HTMLElement).closest('button,a')) return
    this.startX = event.clientX
    this.dragging.set(true)
    ;(event.currentTarget as HTMLElement).setPointerCapture?.(event.pointerId)
  }

  move(event: PointerEvent) {
    if (this.startX === null) return
    this.dx.set(event.clientX - this.startX)
  }

  up() {
    if (this.startX === null) return
    this.startX = null
    this.dragging.set(false)
    const dx = this.dx()
    if (dx > SWIPE_THRESHOLD) this.rate.emit('YES')
    else if (dx < -SWIPE_THRESHOLD) this.rate.emit('NO')
    else this.dx.set(0)
  }
}
