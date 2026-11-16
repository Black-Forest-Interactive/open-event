import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core'
import { toObservable, toSignal } from '@angular/core/rxjs-interop'
import { FormBuilder, ReactiveFormsModule } from '@angular/forms'
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from '@angular/material/bottom-sheet'
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatDatepickerModule } from '@angular/material/datepicker'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatIcon } from '@angular/material/icon'
import { MatInput } from '@angular/material/input'
import { MatSlideToggle } from '@angular/material/slide-toggle'
import { TranslatePipe, TranslateService } from '@ngx-translate/core'
import { HotToastService } from '@ngxpert/hot-toast'
import { DateTime } from 'luxon'
import { catchError, debounceTime, map, of, startWith, switchMap } from 'rxjs'
import { EventSearchRequest, Finder, FinderChangeRequest, FinderInfo, triExcluded, triFromLists, triIncluded, triToggle } from '@open-event/core'
import { FinderService } from '@open-event/portal'
import { CategoryFilterComponent } from '../../event/event-board-filter/category-filter/category-filter.component'
import { AudienceFilterComponent } from '../../event/event-board-filter/audience-filter/audience-filter.component'

export interface FinderEditorSheetData {
  finder?: FinderInfo
}

@Component({
  selector: 'portal-finder-editor-sheet',
  templateUrl: './finder-editor-sheet.component.html',
  imports: [ReactiveFormsModule, MatButton, MatIconButton, MatDatepickerModule, MatFormFieldModule, MatIcon, MatInput, MatSlideToggle, TranslatePipe, CategoryFilterComponent, AudienceFilterComponent],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class FinderEditorSheetComponent {
  private dialogRef = inject<MatDialogRef<FinderEditorSheetComponent, Finder>>(MatDialogRef, { optional: true })
  private sheetRef = inject<MatBottomSheetRef<FinderEditorSheetComponent, Finder>>(MatBottomSheetRef, { optional: true })
  private data: FinderEditorSheetData = inject(MAT_DIALOG_DATA, { optional: true }) ?? inject(MAT_BOTTOM_SHEET_DATA, { optional: true }) ?? {}
  private service = inject(FinderService)
  private toast = inject(HotToastService)
  private translate = inject(TranslateService)
  private fb = inject(FormBuilder)

  readonly isNew = !this.data.finder
  private readonly initial = this.data.finder?.request

  readonly fg = this.fb.group({
    name: this.fb.nonNullable.control(this.data.finder?.name ?? this.translate.instant('finder.defaultName')),
    from: this.fb.control<DateTime | null>(this.initial?.from ? DateTime.fromISO(this.initial.from) : DateTime.now().startOf('day')),
    to: this.fb.control<DateTime | null>(this.initial?.to ? DateTime.fromISO(this.initial.to) : DateTime.now().startOf('day').plus({ days: 30 })),
    onlyAvailableEvents: this.fb.nonNullable.control(this.initial?.onlyAvailableEvents ?? false)
  })
  readonly categories = signal(triFromLists(this.initial?.categories, this.initial?.excludedCategories))
  readonly audiences = signal(triFromLists(this.initial?.audiences, this.initial?.excludedAudiences))
  readonly tried = signal(false)
  readonly saving = signal(false)

  private value = toSignal(
    this.fg.valueChanges.pipe(
      startWith(this.fg.value),
      map(() => this.fg.getRawValue())
    ),
    { initialValue: this.fg.getRawValue() }
  )
  readonly nameError = computed(() => this.value().name.trim().length === 0)
  readonly rangeError = computed(() => {
    const v = this.value()
    return !v.from || !v.to || v.to < v.from
  })

  private request = computed(() => {
    const v = this.value()
    return new EventSearchRequest(
      this.initial?.fullTextSearch ?? '',
      v.from?.toISODate() ?? undefined,
      v.to?.toISODate() ?? undefined,
      false,
      false,
      v.onlyAvailableEvents,
      triIncluded(this.categories()),
      false,
      false,
      triIncluded(this.audiences()),
      [],
      triExcluded(this.categories()),
      triExcluded(this.audiences())
    )
  })

  readonly matches = toSignal(
    toObservable(this.request).pipe(
      debounceTime(250),
      switchMap((request) => {
        if (!request.from || !request.to || request.to < request.from) return of(0)
        return this.service.preview(new FinderChangeRequest('preview', request)).pipe(
          map((p) => p.total),
          catchError(() => of(undefined))
        )
      })
    )
  )

  toggleCategory(name: string) {
    this.categories.update((c) => triToggle(c, name))
  }

  toggleAudience(name: string) {
    this.audiences.update((a) => triToggle(a, name))
  }

  cancel() {
    this.close()
  }

  submit() {
    this.tried.set(true)
    if (this.nameError() || this.rangeError() || this.saving()) return
    const change = new FinderChangeRequest(this.value().name.trim(), this.request())
    const id = this.data.finder?.id
    const call = id !== undefined ? this.service.updateFinder(id, change) : this.service.createFinder(change)
    this.saving.set(true)
    call.subscribe({
      next: (finder) => {
        this.saving.set(false)
        this.close(finder)
      },
      error: () => {
        this.saving.set(false)
        this.toast.error(this.translate.instant('finder.editor.error'))
      }
    })
  }

  private close(result?: Finder) {
    this.dialogRef?.close(result)
    this.sheetRef?.dismiss(result)
  }
}
