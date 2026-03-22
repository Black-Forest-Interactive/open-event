import { Injectable, signal } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { SettingsService } from '@open-event/admin'

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  title = signal('app.title')

  constructor(
    private settingsService: SettingsService,
    private pageTitle: Title
  ) {
    this.settingsService.getTitle().subscribe((d) => {
      this.pageTitle.setTitle(d.text)
      this.title.set(d.text + ' Backoffice')
    })
  }
}
