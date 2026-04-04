import { Component, ElementRef, input, OnInit, output, viewChild } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs'

@Component({
  selector: 'admin-board-toolbar-search',
  imports: [MatIcon, TranslatePipe],
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

  clear(input: HTMLInputElement) {
    input.value = ''
    this.fullTextSearch.emit('')
  }

  ngOnInit(): void {
    this.keyUpSubject.pipe(debounceTime(500), distinctUntilChanged()).subscribe((query) => this.fullTextSearch.emit(query))
  }
}
