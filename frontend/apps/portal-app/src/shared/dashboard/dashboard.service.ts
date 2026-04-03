import { inject, Injectable } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { SettingsService } from '@open-event/portal'

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private pageTitle = inject(Title)

  title: string = 'app.title'

  constructor() {
    const settingsService = inject(SettingsService)

    settingsService.getTitle().subscribe((d) => {
      this.pageTitle.setTitle(d.text)
      this.title = d.text
    })
  }
}
