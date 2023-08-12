import {Injectable} from '@angular/core';
import {SettingService} from "../../settings/model/setting.service";
import {Title} from "@angular/platform-browser";

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  title: string = 'app.title'

  constructor(private settingsService: SettingService, private pageTitle: Title) {

    settingsService.getTitle().subscribe(d => {
      this.pageTitle.setTitle(d.text)
      this.title = d.text
    })
  }
}
