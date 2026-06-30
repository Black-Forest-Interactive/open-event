import { Component, computed, inject, input, output, resource, signal } from '@angular/core'
import { DatePipe } from '@angular/common'
import { RouterLink } from '@angular/router'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatDialog } from '@angular/material/dialog'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatMenu, MatMenuTrigger } from '@angular/material/menu'
import { MatProgressBar } from '@angular/material/progress-bar'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { Audience, EventSearchEntry } from '@open-event/core'
import { AudienceService, CategoryService, EventService } from '@open-event/portal'
import { CategoryChipComponent, CategoryPickerComponent, EventBoardListComponent as LibEventBoardListComponent, EventBoardCalendarComponent, getCategoryStyle } from '@open-event/ui'
import { toPromise } from '@open-event/shared'
import { EventBroadcastSheetComponent } from '../../announcement/event-broadcast-sheet/event-broadcast-sheet.component'
import { EventCancelDialogComponent } from '../event-cancel-dialog/event-cancel-dialog.component'
import { EventEditDialogComponent } from '../event-edit/event-edit-dialog.component'
import { EventTextEditDialogComponent } from '../event-edit/event-text-edit-dialog.component'
import { EventShareSheetComponent } from '../../share/event-share-sheet/event-share-sheet.component'
import { toEventBoardEntry } from '../event-board-entry.mapper'

@Component({
  selector: 'portal-event-board-list',
  templateUrl: './event-board-list.component.html',
  styleUrl: './event-board-list.component.scss',
  imports: [LibEventBoardListComponent, EventBoardCalendarComponent, DatePipe, RouterLink, MatButton, MatIconButton, MatIcon, MatMenu, MatMenuTrigger, MatProgressBar, TranslatePipe, CategoryChipComponent, CategoryPickerComponent],
  standalone: true
})
export class EventBoardListComponent {
  entries = input.required<EventSearchEntry[]>()
  reloading = input.required<boolean>()
  layout = input.required<'cards' | 'rows' | 'calendar' | 'map'>()
  navView = input.required<'all' | 'saved' | 'regs' | 'own'>()
  hasMoreElements = input.required<boolean>()
  nearEnd = output<void>()

  private bottomSheet = inject(MatBottomSheet)
  private dialog = inject(MatDialog)
  private eventService = inject(EventService)
  private categoryService = inject(CategoryService)
  private audienceService = inject(AudienceService)
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)

  readonly mapped = computed(() => this.entries().map(e => toEventBoardEntry(e)))
  readonly listLayout = computed(() => this.layout() === 'rows' ? 'rows' as const : 'cards' as const)

  private allCatsResource = resource({ loader: (p) => toPromise(this.categoryService.getCategories(0, 100), p.abortSignal) })
  readonly allCategories = computed(() => this.allCatsResource.value()?.content ?? [])

  private allAudsResource = resource({ loader: (p) => toPromise(this.audienceService.getAudiences(0, 100), p.abortSignal) })
  readonly allAudiences = computed(() => this.allAudsResource.value()?.content ?? [])

  // Committed overrides — shown in the chip row after saving
  private localCats = signal(new Map<number, string[]>())
  private localAuds = signal(new Map<number, string[]>())

  // Buffered state while picker is open — flushed to backend on Save
  private pendingCats = signal(new Map<number, string[]>())
  private pendingAuds = signal(new Map<number, string[]>())

  readonly savingCat = signal(false)
  readonly savingAud = signal(false)

  private activeCatTrigger: MatMenuTrigger | null = null
  private activeAudTrigger: MatMenuTrigger | null = null

  // ── Chip row (committed) ───────────────────────────────────────────────────

  entryCats(entry: EventSearchEntry): string[] {
    return this.localCats().get(entry.id) ?? entry.categories
  }

  entryAuds(entry: EventSearchEntry): string[] {
    return this.localAuds().get(entry.id) ?? entry.audiences
  }

  categoryStyle(entry: EventSearchEntry) {
    return getCategoryStyle(this.entryCats(entry)[0] ?? '')
  }

  isGhostAud(entry: EventSearchEntry): boolean {
    return this.entryAuds(entry).length === 0
  }

  isAllAud(entry: EventSearchEntry): boolean {
    return this.entryAuds(entry).length > 1
  }

  audLabel(entry: EventSearchEntry): string {
    const auds = this.entryAuds(entry)
    return auds.length === 1 ? auds[0] : ''
  }

  // ── Picker (pending) ───────────────────────────────────────────────────────

  pendingCatIds(entry: EventSearchEntry): number[] {
    const names = this.pendingCats().get(entry.id) ?? this.entryCats(entry)
    return this.allCategories().filter(c => names.includes(c.name)).map(c => c.id)
  }

  pendingAudIds(entry: EventSearchEntry): number[] {
    const names = this.pendingAuds().get(entry.id) ?? this.entryAuds(entry)
    return this.allAudiences().filter(a => names.includes(a.name)).map(a => a.id)
  }

  isAudPendingSelected(entry: EventSearchEntry, aud: Audience): boolean {
    const names = this.pendingAuds().get(entry.id) ?? this.entryAuds(entry)
    return names.includes(aud.name)
  }

  // ── Menu open handlers ─────────────────────────────────────────────────────

  initPendingCats(entry: EventSearchEntry, trigger: MatMenuTrigger) {
    this.activeCatTrigger = trigger
    this.pendingCats.update(m => new Map(m).set(entry.id, [...this.entryCats(entry)]))
  }

  initPendingAuds(entry: EventSearchEntry, trigger: MatMenuTrigger) {
    this.activeAudTrigger = trigger
    this.pendingAuds.update(m => new Map(m).set(entry.id, [...this.entryAuds(entry)]))
  }

  // ── Picker toggle (no backend call) ───────────────────────────────────────

  togglePendingCategory(entry: EventSearchEntry, catId: number) {
    const cat = this.allCategories().find(c => c.id === catId)
    if (!cat) return
    const current = this.pendingCats().get(entry.id) ?? this.entryCats(entry)
    const isSelected = current.includes(cat.name)
    if (isSelected && current.length === 1) return
    const newNames = isSelected ? current.filter(n => n !== cat.name) : [...current, cat.name]
    this.pendingCats.update(m => new Map(m).set(entry.id, newNames))
  }

  togglePendingAudience(entry: EventSearchEntry, audId: number) {
    const aud = this.allAudiences().find(a => a.id === audId)
    if (!aud) return
    const current = this.pendingAuds().get(entry.id) ?? this.entryAuds(entry)
    const newNames = current.includes(aud.name) ? current.filter(n => n !== aud.name) : [...current, aud.name]
    this.pendingAuds.update(m => new Map(m).set(entry.id, newNames))
  }

  // ── Save (single backend call per picker) ─────────────────────────────────

  savePendingCats(entry: EventSearchEntry) {
    if (this.savingCat()) return
    const names = this.pendingCats().get(entry.id) ?? this.entryCats(entry)
    const newIds = this.allCategories().filter(c => names.includes(c.name)).map(c => c.id)
    this.savingCat.set(true)
    this.eventService.setCategories(entry.id, newIds).subscribe({
      next: () => {
        this.localCats.update(m => new Map(m).set(entry.id, names))
        this.savingCat.set(false)
        this.activeCatTrigger?.closeMenu()
      },
      error: () => {
        this.savingCat.set(false)
        this.translate.get('event.message.update.failed').subscribe(msg => this.toast.error(msg))
      }
    })
  }

  savePendingAuds(entry: EventSearchEntry) {
    if (this.savingAud()) return
    const names = this.pendingAuds().get(entry.id) ?? this.entryAuds(entry)
    const newIds = this.allAudiences().filter(a => names.includes(a.name)).map(a => a.id)
    this.savingAud.set(true)
    this.eventService.setAudiences(entry.id, newIds).subscribe({
      next: () => {
        this.localAuds.update(m => new Map(m).set(entry.id, names))
        this.savingAud.set(false)
        this.activeAudTrigger?.closeMenu()
      },
      error: () => {
        this.savingAud.set(false)
        this.translate.get('event.message.update.failed').subscribe(msg => this.toast.error(msg))
      }
    })
  }

  // ── Misc helpers ───────────────────────────────────────────────────────────

  fillPct(entry: EventSearchEntry): number {
    if (!entry.maxGuestAmount) return 0
    return Math.round((entry.amountAccepted / entry.maxGuestAmount) * 100)
  }

  edit(entry: EventSearchEntry) {
    this.dialog.open(EventEditDialogComponent, { data: { id: entry.id }, width: '680px', maxWidth: '95vw', disableClose: true })
  }

  openTextEdit(entry: EventSearchEntry) {
    this.dialog.open(EventTextEditDialogComponent, { data: { id: entry.id }, width: '360px', maxWidth: '95vw', disableClose: true })
  }

  openBroadcast(entry: EventSearchEntry) {
    this.bottomSheet.open(EventBroadcastSheetComponent, { data: { eventId: entry.id, eventTitle: entry.title, participantCount: entry.amountAccepted } })
  }

  openShare(entry: EventSearchEntry) {
    this.bottomSheet.open(EventShareSheetComponent, { data: { eventId: entry.id, eventTitle: entry.title } })
  }

  openCancel(entry: EventSearchEntry) {
    this.dialog.open(EventCancelDialogComponent, { width: '400px', data: { event: { id: entry.id, title: entry.title }, participantCount: entry.amountAccepted } })
  }
}
