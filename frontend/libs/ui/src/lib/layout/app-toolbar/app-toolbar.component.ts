import { Component, inject, input, output, ChangeDetectionStrategy } from '@angular/core'
import { MatToolbar } from '@angular/material/toolbar'
import { MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { ThemeService, TourService } from '@open-event/shared'

@Component({
  selector: 'lib-app-toolbar',
  templateUrl: './app-toolbar.component.html',
  styleUrl: './app-toolbar.component.scss',
  changeDetection: ChangeDetectionStrategy.Eager,
  imports: [MatToolbar, MatIconButton, MatIcon, TranslatePipe]
})
export class AppToolbarComponent {
  title = input<string>('')
  showMenuToggle = input<boolean>(true)
  menuToggle = output<void>()
  protected readonly themeService = inject(ThemeService)
  protected readonly tourService = inject(TourService)
}
