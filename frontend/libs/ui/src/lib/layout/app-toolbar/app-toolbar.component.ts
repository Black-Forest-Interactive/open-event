import { Component, input, output } from '@angular/core'
import { MatToolbar } from '@angular/material/toolbar'
import { MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'

@Component({
  selector: 'lib-app-toolbar',
  templateUrl: './app-toolbar.component.html',
  styleUrl: './app-toolbar.component.scss',
  standalone: true,
  imports: [MatToolbar, MatIconButton, MatIcon, TranslatePipe]
})
export class AppToolbarComponent {
  title = input<string>('')
  showMenuToggle = input<boolean>(true)

  menuToggle = output<void>()
}
