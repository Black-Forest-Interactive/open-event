import { inject, Injectable, signal } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { SettingsService } from '@open-event/admin'

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private settingsService = inject(SettingsService)
  private pageTitle = inject(Title)

  title = signal('app.title')

  constructor() {
    this.settingsService.getTitle().subscribe((d) => {
      this.pageTitle.setTitle(d.text)
      this.title.set(d.text + ' Backoffice')
    })
  }
}
