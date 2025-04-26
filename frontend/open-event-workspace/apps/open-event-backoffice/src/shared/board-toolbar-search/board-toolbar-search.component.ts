import {Component, ElementRef, input, OnInit, output, viewChild} from '@angular/core';
import {MatInput, MatLabel, MatSuffix} from "@angular/material/input";
import {TranslatePipe} from "@ngx-translate/core";
import {MatFormField} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {debounceTime, distinctUntilChanged, Subject} from "rxjs";

@Component({
  selector: 'app-board-toolbar-search',
  imports: [
    MatFormField,
    MatIcon,
    MatIconButton,
    MatInput,
    MatLabel,
    MatSuffix,
    TranslatePipe
  ],
  templateUrl: './board-toolbar-search.component.html',
  styleUrl: './board-toolbar-search.component.scss'
})
export class BoardToolbarSearchComponent implements OnInit {
  placeholder = input('')
  fullTextSearch = output<string>()

  private input = viewChild.required<ElementRef<HTMLInputElement>>('input')
  private keyUpSubject = new Subject<string>()

  onKeyUp(value: string) {
    this.keyUpSubject.next(value)
  }

  clear() {
    this.input().nativeElement.value = ''
    this.fullTextSearch.emit('')
  }

  ngOnInit(): void {
    this.keyUpSubject.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(query => this.fullTextSearch.emit(query))
  }

}
