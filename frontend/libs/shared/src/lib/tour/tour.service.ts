import { computed, inject, Injectable, signal } from '@angular/core'
import { TranslateService } from '@ngx-translate/core'
import { driver } from 'driver.js'

export interface TourStep {
  element?: string
  title: string
  description: string
}

export interface TourDefinition {
  id: string
  steps: TourStep[]
}

@Injectable({ providedIn: 'root' })
export class TourService {
  private translate = inject(TranslateService)

  private tour = signal<TourDefinition | undefined>(undefined)
  readonly available = computed(() => this.tour() !== undefined)
  private autoStartTimer: ReturnType<typeof setInterval> | undefined

  register(tour: TourDefinition, ready: () => boolean = () => true) {
    this.cancelAutoStart()
    this.tour.set(tour)
    if (localStorage.getItem(this.storageKey(tour.id))) return
    const deadline = Date.now() + 5000
    this.autoStartTimer = setInterval(() => {
      if (!ready() && Date.now() < deadline) return
      this.cancelAutoStart()
      this.start()
    }, 300)
  }

  unregister(id: string) {
    if (this.tour()?.id !== id) return
    this.cancelAutoStart()
    this.tour.set(undefined)
  }

  start() {
    const tour = this.tour()
    if (!tour) return
    localStorage.setItem(this.storageKey(tour.id), new Date().toISOString())
    // steps whose target is not rendered or not visible (empty list, mobile-only controls, inactive wizard steps, ...) are skipped
    const steps = tour.steps.filter((s) => !s.element || this.isVisible(document.querySelector(s.element)))
    if (steps.length === 0) return
    const keys = steps.flatMap((s) => [s.title, s.description])
    this.translate.get([...keys, 'action.next', 'action.back', 'action.done', 'tour.progress']).subscribe((t) => {
      driver({
        showProgress: true,
        progressText: t['tour.progress'],
        nextBtnText: t['action.next'],
        prevBtnText: t['action.back'],
        doneBtnText: t['action.done'],
        steps: steps.map((s) => ({ element: s.element, popover: { title: t[s.title], description: t[s.description] } }))
      }).drive()
    })
  }

  private isVisible(el: Element | null): boolean {
    return el instanceof HTMLElement && el.offsetParent !== null
  }

  private cancelAutoStart() {
    if (this.autoStartTimer) clearInterval(this.autoStartTimer)
    this.autoStartTimer = undefined
  }

  private storageKey(id: string): string {
    return `tour.${id}`
  }
}
