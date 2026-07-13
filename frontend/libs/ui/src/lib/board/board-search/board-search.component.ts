import { Component, ElementRef, input, OnInit, output, viewChild, ChangeDetectionStrategy } from '@angular/core'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs'

@Component({
  selector: 'lib-board-search',
  templateUrl: './board-search.component.html',
  styleUrl: './board-search.component.scss',
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [MatIcon, TranslatePipe]
})
export class BoardSearchComponent implements OnInit {
  placeholder = input('')
  searchChange = output<string>()

  private inputRef = viewChild.required<ElementRef<HTMLInputElement>>('input')
  private keyUp = new Subject<string>()

  onKeyUp(value: string) {
    this.keyUp.next(value)
  }

  clear(input: HTMLInputElement) {
    input.value = ''
    this.searchChange.emit('')
  }

  ngOnInit() {
    this.keyUp.pipe(debounceTime(500), distinctUntilChanged()).subscribe((query) => this.searchChange.emit(query))
  }
}
