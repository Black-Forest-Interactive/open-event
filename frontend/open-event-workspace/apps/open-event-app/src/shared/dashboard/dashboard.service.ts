import {Injectable} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {SettingsService} from "@open-event-workspace/app";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  title: string = 'app.title'

  constructor(settingsService: SettingsService, private pageTitle: Title) {

    settingsService.getTitle().subscribe(d => {
      this.pageTitle.setTitle(d.text)
      this.title = d.text
    })

  }


}
