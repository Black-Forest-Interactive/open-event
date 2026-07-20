import { ChangeDetectionStrategy, Component, computed, ElementRef, input, output, signal, viewChild } from '@angular/core'
import { MatAutocomplete, MatAutocompleteSelectedEvent, MatAutocompleteTrigger, MatOption } from '@angular/material/autocomplete'
import { ChipSelectEntry } from './chip-select-entry'
import { FormControl, ReactiveFormsModule } from '@angular/forms'
import { COMMA, ENTER } from '@angular/cdk/keycodes'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatChipGrid, MatChipInput, MatChipRow } from '@angular/material/chips'
import { MatIcon } from '@angular/material/icon'

@Component({
  selector: 'lib-chip-select-pane',
  templateUrl: './chip-select-pane.component.html',
  styleUrls: ['./chip-select-pane.component.scss'],
  imports: [MatFormField, MatChipGrid, MatChipRow, MatIcon, ReactiveFormsModule, MatAutocompleteTrigger, MatChipInput, MatAutocomplete, MatOption, MatLabel],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class ChipSelectPaneComponent {
  removable = input<boolean>(true)
  formCtrl = input.required<FormControl>()
  placeholder = input<string>('Assigned entry ...')
  entries = input<ChipSelectEntry[]>([])

  changed = output<boolean>()
  separatorKeysCodes: number[] = [ENTER, COMMA]

  private entryInput = viewChild<ElementRef<HTMLInputElement>>('entryInput')

  readonly selectedEntries = signal<ChipSelectEntry[]>([])
  readonly filteredEntries = computed(() => this.entries().filter((o) => this.selectedEntries().indexOf(o) < 0))

  clear(emitEvent: boolean = true) {
    this.clearInput()
    this.selectedEntries.set([])
    this.formCtrl().reset({ emitEvent })
    this.changed.emit(true)
  }

  getSelectedEntryIds(): number[] {
    return this.selectedEntries().map((e) => e.id)
  }

  setSelectedValues(current: ChipSelectEntry[]) {
    this.selectedEntries.set(current)
    this.clearInput()
    this.formCtrl().setValue(
      current.map((s) => s.id),
      { emitEvent: true }
    )
    this.changed.emit(true)
  }

  handleRemoveEvent(entry: ChipSelectEntry): void {
    const updated = this.selectedEntries().filter((e) => e !== entry)
    if (updated.length === this.selectedEntries().length) return

    this.selectedEntries.set(updated)
    this.formCtrl().setValue(
      updated.map((s) => s.id),
      { emitEvent: true }
    )
    this.changed.emit(true)
    this.entryInput()?.nativeElement.blur()
  }

  handleSelectedEvent(event: MatAutocompleteSelectedEvent): void {
    this.selectedEntries.set([...this.selectedEntries(), event.option.value])
    this.clearInput()
    this.formCtrl().setValue(
      this.selectedEntries().map((s) => s.id),
      { emitEvent: true }
    )
    this.changed.emit(true)
    this.entryInput()?.nativeElement.blur()
  }

  private clearInput() {
    const el = this.entryInput()
    if (el) el.nativeElement.value = ''
  }
}
