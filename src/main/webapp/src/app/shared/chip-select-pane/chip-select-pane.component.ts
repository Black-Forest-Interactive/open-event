import {Component, ElementRef, EventEmitter, Input, Output, ViewChild} from '@angular/core';
import {MatAutocomplete, MatAutocompleteSelectedEvent} from "@angular/material/autocomplete";
import {ChipSelectEntry} from "./chip-select-entry";
import {FormControl} from "@angular/forms";
import {COMMA, ENTER} from "@angular/cdk/keycodes";

@Component({
  selector: 'app-chip-select-pane',
  templateUrl: './chip-select-pane.component.html',
  styleUrls: ['./chip-select-pane.component.scss']
})
export class ChipSelectPaneComponent {

  @Input()
  set entries(data: ChipSelectEntry[]) {
    this.allEntries = data
    this.updateAutocomplete()
  }

  @Input()
  removable = true

  @Input()
  formCtrl: FormControl | undefined

  @Input()
  placeholder: string = "Assigned entry ..."

  @Output()
  changed: EventEmitter<Boolean> = new EventEmitter<Boolean>();


  separatorKeysCodes: number[] = [ENTER, COMMA]

  filteredEntries: ChipSelectEntry[] = []
  selectedEntries: ChipSelectEntry[] = []
  allEntries: ChipSelectEntry[] = []

  @ViewChild('entryInput') entryInput: ElementRef<HTMLInputElement> | undefined
  @ViewChild('auto') matAutocomplete: MatAutocomplete | undefined

  constructor() {
  }


  clear(emitEvent: boolean = true) {
    if (this.entryInput) this.entryInput.nativeElement.value = '';
    this.selectedEntries = [];
    if (this.formCtrl) this.formCtrl.reset({emitEvent: emitEvent});
    this.updateAutocomplete();
  }

  getSelectedEntryIds(): number[] {
    return this.selectedEntries.map(e => e.id);
  }

  setSelectedValues(current: ChipSelectEntry[]) {
    this.selectedEntries = current;
    if (this.entryInput) this.entryInput.nativeElement.value = '';
    if (this.formCtrl) this.formCtrl.setValue(this.selectedEntries.map(s => s.id), {emitEvent: true});
    this.updateAutocomplete();
  }

  handleRemoveEvent(entry: ChipSelectEntry): void {
    const index = this.selectedEntries.indexOf(entry);
    if (index < 0) return;

    this.selectedEntries.splice(index, 1);
    if (this.formCtrl) this.formCtrl.setValue(this.selectedEntries.map(s => s.id), {emitEvent: true});
    this.updateAutocomplete();
    if (this.entryInput) this.entryInput.nativeElement.blur();
  }

  handleSelectedEvent(event: MatAutocompleteSelectedEvent): void {
    this.selectedEntries.push(event.option.value)
    if (this.entryInput) this.entryInput.nativeElement.value = ''
    if (this.formCtrl) this.formCtrl.setValue(this.selectedEntries.map(s => s.id), {emitEvent: true})
    this.updateAutocomplete()
    if (this.entryInput) this.entryInput.nativeElement.blur()
  }

  private updateAutocomplete() {
    this.filteredEntries = this.allEntries.filter(o => this.selectedEntries.indexOf(o) < 0)
    this.changed.emit(true)
  }


}
