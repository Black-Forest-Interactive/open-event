import { Directive, ElementRef, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core'

@Directive({
  selector: '[libScrollNearEnd]',
  standalone: true
})
export class ScrollNearEndDirective implements OnInit {
  @Output() nearEnd = new EventEmitter<void>()

  /** threshold in PX when to emit before page end scroll */
  @Input() threshold = 120

  private window!: Window

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    this.window = window
  }

  @HostListener('window:scroll', ['$event'])
  onScroll(event: Event): void {
    const heightOfWholePage = this.window.document.documentElement.scrollHeight
    const heightOfElement = this.el.nativeElement.scrollHeight
    const currentScrolledY = this.window.scrollY
    const innerHeight = this.window.innerHeight
    const spaceOfElementAndPage = heightOfWholePage - heightOfElement
    const scrollToBottom = heightOfElement - innerHeight - currentScrolledY + spaceOfElementAndPage

    if (scrollToBottom < this.threshold) {
      this.nearEnd.emit()
    }
  }
}
