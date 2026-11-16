import { Component, input, ChangeDetectionStrategy } from '@angular/core'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'lib-app-footer',
  templateUrl: './app-footer.component.html',
  styleUrl: './app-footer.component.scss',
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [TranslatePipe]
})
export class AppFooterComponent {
  version = input<string>('')
  info = input<string>('')
  currentYear = new Date().getFullYear()
}
