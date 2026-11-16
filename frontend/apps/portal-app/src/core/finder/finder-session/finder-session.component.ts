import { ChangeDetectionStrategy, Component, computed, DestroyRef, inject, linkedSignal, resource, signal, TemplateRef, viewChild } from '@angular/core'
import { toSignal } from '@angular/core/rxjs-interop'
import { DatePipe } from '@angular/common'
import { ActivatedRoute, Router } from '@angular/router'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { CreateHotToastRef, HotToastService } from '@ngxpert/hot-toast'
import { forkJoin, map, Observable } from 'rxjs'
import { EventSearchEntry, FinderAnswer, ParticipateRequest, ParticipateResponse } from '@open-event/core'
import { EventService, FinderService, RegistrationService } from '@open-event/portal'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { getCategoryStyle } from '@open-event/ui'
import { EventBoardNavbarComponent } from '../../event/event-board-navbar/event-board-navbar.component'
import { EventNavigationService } from '../../event/event-navigation.service'
import { RegistrationParticipateSheetComponent } from '../../registration/registration-participate-sheet/registration-participate-sheet.component'
import { FinderCriteriaChipsComponent } from '../finder-criteria-chips/finder-criteria-chips.component'
import { FinderEditorLauncher } from '../finder-editor-sheet/finder-editor-launcher'
import { FinderRatedListComponent, FinderRateEvent } from '../finder-rated-list/finder-rated-list.component'
import { FinderSwipeCardComponent, SwipeExit } from '../finder-swipe-card/finder-swipe-card.component'
import { FINDER_ANSWER_ICONS, finderStats, FinderTab, progressPercent } from '../finder.util'

/** Duration of the leave animation of a rated card, matches the css transition. */
const EXIT_DURATION = 230
const PAGE_SIZE = 500

interface RatingStep {
  id: number
  prev: FinderAnswer | null
}

@Component({
  selector: 'portal-finder-session',
  templateUrl: './finder-session.component.html',
  styleUrls: ['../finder-shared.scss', './finder-session.component.scss'],
  imports: [DatePipe, MatButton, MatIcon, TranslatePipe, LoadingBarComponent, EventBoardNavbarComponent, FinderCriteriaChipsComponent, FinderSwipeCardComponent, FinderRatedListComponent],
  changeDetection: ChangeDetectionStrategy.Eager,
  host: { '(window:keydown)': 'handleKey($event)' }
})
export class FinderSessionComponent {
  private route = inject(ActivatedRoute)
  private router = inject(Router)
  private service = inject(FinderService)
  private eventService = inject(EventService)
  private registrationService = inject(RegistrationService)
  private editor = inject(FinderEditorLauncher)
  private bottomSheet = inject(MatBottomSheet)
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)

  private undoToast = viewChild.required<TemplateRef<unknown>>('undoToast')

  readonly icons = FINDER_ANSWER_ICONS
  readonly tab = signal<FinderTab>('discover')
  readonly laterRound = signal(false)
  readonly exiting = signal<SwipeExit | null>(null)
  readonly history = signal<RatingStep[]>([])
  readonly confirm = signal<'delete' | 'reset' | null>(null)
  private skipped = signal(new Set<number>())
  private busy = false
  private exitTimer: ReturnType<typeof setTimeout> | undefined
  private lastToast: CreateHotToastRef<unknown> | undefined

  private finderId = toSignal(
    this.route.paramMap.pipe(
      map((p) => {
        const id = p.get('id')
        return id ? +id : undefined
      })
    )
  )

  private infoResource = resource({
    params: this.finderId,
    loader: (p) => toPromise(this.service.getFinder(p.params), p.abortSignal)
  })
  private candidateResource = resource({
    params: this.finderId,
    loader: (p) =>
      toPromise(
        forkJoin({
          open: this.service.getOpen(p.params, 0, PAGE_SIZE),
          yes: this.service.getEntries(p.params, 'YES', 0, PAGE_SIZE),
          maybe: this.service.getEntries(p.params, 'MAYBE', 0, PAGE_SIZE),
          no: this.service.getEntries(p.params, 'NO', 0, PAGE_SIZE)
        }),
        p.abortSignal
      )
  })

  readonly info = computed(() => this.infoResource.value())
  readonly reloading = computed(() => this.infoResource.isLoading() || this.candidateResource.isLoading())

  // local copies, ratings and registrations are updated optimistically and persisted in the background
  readonly entries = linkedSignal(() => {
    const c = this.candidateResource.value()
    if (!c) return []
    const answered = [...c.yes.content, ...c.maybe.content, ...c.no.content].map((e) => e.entry)
    return [...c.open.content, ...answered].sort((a, b) => a.start.localeCompare(b.start))
  })
  readonly ratings = linkedSignal(() => {
    const c = this.candidateResource.value()
    const result: Record<string, FinderAnswer> = {}
    if (!c) return result
    ;[...c.yes.content, ...c.maybe.content, ...c.no.content].forEach((e) => (result[e.entry.id] = e.answer))
    return result
  })
  readonly loaded = computed(() => this.candidateResource.hasValue())

  readonly stats = computed(() => finderStats(this.entries(), this.ratings()))
  readonly percent = computed(() => progressPercent(this.stats()))
  readonly queue = computed(() => {
    const ratings = this.ratings()
    const skipped = this.skipped()
    if (this.laterRound()) return this.entries().filter((e) => ratings[e.id] === 'MAYBE' && !skipped.has(e.id))
    return this.entries().filter((e) => !ratings[e.id])
  })
  readonly current = computed(() => this.queue()[0])
  readonly peekHue = computed(() => {
    const next = this.queue()[1]
    return next ? getCategoryStyle(next.categories[0] ?? '').hue : undefined
  })
  readonly listEntries = computed(() => {
    const tab = this.tab()
    const ratings = this.ratings()
    return tab === 'discover' ? [] : this.entries().filter((e) => ratings[e.id] === tab)
  })
  readonly tabs = computed(() => {
    const s = this.stats()
    return [
      { key: 'discover' as FinderTab, icon: 'swipe', label: 'finder.tab.discover', count: s.open },
      { key: 'YES' as FinderTab, icon: this.icons.YES, label: 'finder.tab.YES', count: s.yes },
      { key: 'MAYBE' as FinderTab, icon: this.icons.MAYBE, label: 'finder.tab.MAYBE', count: s.maybe },
      { key: 'NO' as FinderTab, icon: this.icons.NO, label: 'finder.tab.NO', count: s.no }
    ]
  })
  readonly selectedListAnswer = computed(() => {
    const tab = this.tab()
    return tab === 'discover' ? undefined : tab
  })

  constructor() {
    inject(DestroyRef).onDestroy(() => {
      clearTimeout(this.exitTimer)
      this.lastToast?.close()
    })
  }

  selectTab(tab: FinderTab) {
    this.tab.set(tab)
    if (tab !== 'discover') this.leaveLaterRound()
  }

  back() {
    this.router.navigate(['/finder']).then()
  }

  startLaterRound() {
    this.skipped.set(new Set())
    this.laterRound.set(true)
  }

  rate(answer: FinderAnswer) {
    const entry = this.current()
    if (!entry || this.busy) return
    this.busy = true
    const id = entry.id
    this.history.update((h) => [...h, { id, prev: this.ratings()[id] ?? null }])
    const keepLater = this.laterRound() && answer === 'MAYBE'
    this.exiting.set(answer === 'YES' ? 'right' : answer === 'NO' ? 'left' : 'down')
    this.exitTimer = setTimeout(() => {
      // in the later round a "later" only moves on, the event stays postponed
      if (keepLater) this.skipped.update((s) => new Set(s).add(id))
      else this.apply(id, answer)
      this.exiting.set(null)
      this.busy = false
      if (this.laterRound() && this.queue().length === 0) this.leaveLaterRound()
    }, EXIT_DURATION)
    if (!keepLater) this.showUndoToast('finder.message.rated.' + answer)
  }

  undo() {
    const steps = this.history()
    const last = steps[steps.length - 1]
    if (!last || this.busy) return
    this.history.set(steps.slice(0, -1))
    this.skipped.update((s) => {
      const result = new Set(s)
      result.delete(last.id)
      return result
    })
    this.apply(last.id, last.prev)
    this.tab.set('discover')
    this.lastToast?.close()
  }

  rerate(event: FinderRateEvent) {
    this.apply(event.entry.id, event.answer)
  }

  openEvent(entry: EventSearchEntry) {
    EventNavigationService.navigateToEventDetails(this.router, +entry.id)
  }

  edit() {
    const info = this.info()
    if (!info) return
    this.editor.open(info).subscribe((finder) => {
      if (!finder) return
      this.history.set([])
      this.leaveLaterRound()
      this.infoResource.reload()
      this.candidateResource.reload()
      this.translate.get('finder.message.saved').subscribe((t) => this.toast.success(t))
    })
  }

  confirmAction() {
    const action = this.confirm()
    const id = this.finderId()
    this.confirm.set(null)
    if (!action || id === undefined) return
    this.history.set([])
    if (action === 'delete') {
      this.service.deleteFinder(id).subscribe({
        next: () => {
          this.translate.get('finder.message.deleted').subscribe((t) => this.toast.success(t))
          this.router.navigate(['/finder']).then()
        },
        error: () => this.showError()
      })
    } else {
      this.leaveLaterRound()
      this.ratings.set({})
      this.service.resetAnswers(id).subscribe({
        next: () => this.translate.get('finder.message.reset').subscribe((t) => this.toast.success(t)),
        error: () => this.showError()
      })
    }
  }

  register(entry: EventSearchEntry) {
    this.eventService.getEventInfo(+entry.id).subscribe((info) => {
      const registration = info.registration
      if (!registration) return
      this.bottomSheet
        .open(RegistrationParticipateSheetComponent, {
          data: {
            info,
            titleKey: 'registration.dialog.accept.title',
            submit: (request: ParticipateRequest) => this.registrationService.addParticipant(registration.registration.id, request)
          }
        })
        .afterDismissed()
        .subscribe((response?: ParticipateResponse) => {
          if (response) this.handleParticipateResponse(entry, response)
        })
    })
  }

  handleKey(event: KeyboardEvent) {
    if (this.tab() !== 'discover' || event.ctrlKey || event.metaKey || event.altKey) return
    const target = event.target as HTMLElement | null
    if (target?.closest('input,textarea,select,[contenteditable]')) return
    if (document.querySelector('.mat-mdc-dialog-container, .mat-bottom-sheet-container')) return
    const action: Record<string, () => void> = {
      ArrowRight: () => this.rate('YES'),
      ArrowLeft: () => this.rate('NO'),
      ArrowDown: () => this.rate('MAYBE'),
      z: () => this.undo(),
      Z: () => this.undo()
    }
    const handler = action[event.key]
    if (!handler) return
    event.preventDefault()
    handler()
  }

  private apply(id: number, answer: FinderAnswer | null) {
    const finderId = this.finderId()
    if (finderId === undefined) return
    this.ratings.update((r) => {
      const result = { ...r }
      if (answer) result[id] = answer
      else delete result[id]
      return result
    })
    const call: Observable<unknown> = answer ? this.service.setAnswer(finderId, +id, answer) : this.service.clearAnswer(finderId, +id)
    call.subscribe({
      error: () => {
        this.showError()
        this.candidateResource.reload()
      }
    })
  }

  private leaveLaterRound() {
    this.laterRound.set(false)
    this.skipped.set(new Set())
  }

  private showUndoToast(key: string) {
    this.lastToast?.close()
    this.lastToast = this.toast.show(this.undoToast(), { data: { message: this.translate.instant(key) }, duration: 3200, icon: '✓' })
  }

  private handleParticipateResponse(entry: EventSearchEntry, response: ParticipateResponse) {
    const registered = response.status === 'ACCEPTED' || response.status === 'WAITING_LIST' || response.status === 'WAITING_LIST_DECREASE_SIZE'
    if (registered) this.entries.update((list) => list.map((e) => (e.id === entry.id ? { ...e, participatingEvent: true } : e)))
    switch (response.status) {
      case 'ACCEPTED':
        this.translate.get('registration.message.accepted').subscribe((msg) => this.toast.success(msg))
        break
      case 'WAITING_LIST_DECREASE_SIZE':
      case 'WAITING_LIST':
        this.translate.get('registration.message.waiting').subscribe((msg) => this.toast.info(msg))
        break
      case 'DECLINED':
        this.translate.get('registration.message.declined').subscribe((msg) => this.toast.warning(msg))
        break
      case 'FAILED':
        this.translate.get('registration.message.failed').subscribe((msg) => this.toast.error(msg))
        break
    }
  }

  private showError() {
    this.translate.get('finder.message.error').subscribe((t) => this.toast.error(t))
  }
}
