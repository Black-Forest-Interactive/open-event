import { Component, input, output } from '@angular/core'
import { RouterLink, RouterLinkActive } from '@angular/router'
import { MatIcon } from '@angular/material/icon'
import { MatTooltip } from '@angular/material/tooltip'
import { TranslatePipe } from '@ngx-translate/core'
import { NavGroup } from '../nav.api'

@Component({
  selector: 'lib-app-sidenav',
  templateUrl: './app-sidenav.component.html',
  styleUrl: './app-sidenav.component.scss',
  standalone: true,
  host: { class: 'flex flex-col h-full w-full overflow-hidden' },
  imports: [RouterLink, RouterLinkActive, MatIcon, MatTooltip, TranslatePipe]
})
export class AppSidenavComponent {
  navGroups = input<NavGroup[]>([])
  collapsed = input<boolean>(false)
  showCollapse = input<boolean>(false)

  logoutClick = output<void>()
  collapseClick = output<void>()
}
