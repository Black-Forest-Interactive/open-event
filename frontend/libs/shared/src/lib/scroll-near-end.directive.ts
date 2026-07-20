import { Directive, ElementRef, inject, input, OnInit, output } from '@angular/core'

@Directive({
  selector: '[libScrollNearEnd]',
  host: {
    '(window:scroll)': 'onScroll()'
  }
})
export class ScrollNearEndDirective implements OnInit {
  nearEnd = output<void>()
  /** threshold in PX when to emit before page end scroll */
  threshold = input(120)
  private el = inject(ElementRef)
  private window!: Window

  ngOnInit(): void {
    this.window = window
  }

  onScroll(): void {
    const heightOfWholePage = this.window.document.documentElement.scrollHeight
    const heightOfElement = this.el.nativeElement.scrollHeight
    const currentScrolledY = this.window.scrollY
    const innerHeight = this.window.innerHeight
    const spaceOfElementAndPage = heightOfWholePage - heightOfElement
    const scrollToBottom = heightOfElement - innerHeight - currentScrolledY + spaceOfElementAndPage

    if (scrollToBottom < this.threshold()) {
      this.nearEnd.emit()
    }
  }
}
