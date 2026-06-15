import { Component, input, output } from '@angular/core'
import { MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'

@Component({
  selector: 'lib-stepper-input',
  imports: [MatIconButton, MatIcon],
  templateUrl: './stepper-input.component.html',
  styleUrl: './stepper-input.component.scss'
})
export class StepperInputComponent {
  value = input.required<number>()
  min = input<number>(1)
  step = input<number>(5)

  changed = output<number>()

  decrement() {
    this.changed.emit(Math.max(this.min(), this.value() - this.step()))
  }

  increment() {
    this.changed.emit(this.value() + this.step())
  }
}
