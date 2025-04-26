import {Component, ElementRef, EventEmitter, input, Input, Output, ViewChild} from '@angular/core';
import {MatAutocomplete, MatAutocompleteSelectedEvent, MatAutocompleteTrigger, MatOption} from "@angular/material/autocomplete";
import {ChipSelectEntry} from "./chip-select-entry";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatChipGrid, MatChipInput, MatChipRow} from "@angular/material/chips";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-chip-select-pane',
  templateUrl: './chip-select-pane.component.html',
  styleUrls: ['./chip-select-pane.component.scss'],
  imports: [
    MatFormField,
    MatChipGrid,
    MatChipRow,
    MatIcon,
    ReactiveFormsModule,
    MatAutocompleteTrigger,
    MatChipInput,
    MatAutocomplete,
    MatOption,
    MatLabel
  ],
  standalone: true
})
export class ChipSelectPaneComponent {

  removable = input<boolean>(true)
  formCtrl = input.required<FormControl>()
  placeholder = input<string>("Assigned entry ...")

  @Output()
  changed: EventEmitter<boolean> = new EventEmitter<boolean>();
  separatorKeysCodes: number[] = [ENTER, COMMA]
  filteredEntries: ChipSelectEntry[] = []
  selectedEntries: ChipSelectEntry[] = []
  allEntries: ChipSelectEntry[] = []
  @ViewChild('entryInput') entryInput: ElementRef<HTMLInputElement> | undefined
  @ViewChild('auto') matAutocomplete: MatAutocomplete | undefined

  constructor() {
  }

  @Input()
  set entries(data: ChipSelectEntry[]) {
    this.allEntries = data
    this.updateAutocomplete()
  }

  clear(emitEvent: boolean = true) {
    if (this.entryInput) this.entryInput.nativeElement.value = ''
    this.selectedEntries = []
    this.formCtrl().reset({emitEvent: emitEvent});
    this.updateAutocomplete();
  }

  getSelectedEntryIds(): number[] {
    return this.selectedEntries.map(e => e.id);
  }

  setSelectedValues(current: ChipSelectEntry[]) {
    this.selectedEntries = current;
    if (this.entryInput) this.entryInput.nativeElement.value = '';
    this.formCtrl().setValue(this.selectedEntries.map(s => s.id), {emitEvent: true});
    this.updateAutocomplete();
  }

  handleRemoveEvent(entry: ChipSelectEntry): void {
    const index = this.selectedEntries.indexOf(entry);
    if (index < 0) return;

    this.selectedEntries.splice(index, 1);
    this.formCtrl().setValue(this.selectedEntries.map(s => s.id), {emitEvent: true});
    this.updateAutocomplete();
    if (this.entryInput) this.entryInput.nativeElement.blur();
  }

  handleSelectedEvent(event: MatAutocompleteSelectedEvent): void {
    this.selectedEntries.push(event.option.value)
    if (this.entryInput) this.entryInput.nativeElement.value = ''
    this.formCtrl().setValue(this.selectedEntries.map(s => s.id), {emitEvent: true})
    this.updateAutocomplete()
    if (this.entryInput) this.entryInput.nativeElement.blur()
  }

  private updateAutocomplete() {
    this.filteredEntries = this.allEntries.filter(o => this.selectedEntries.indexOf(o) < 0)
    this.changed.emit(true)
  }


}
