import {Injectable, OnInit, signal} from '@angular/core';
import {Title} from "@angular/platform-browser";
import {SettingsService} from "@open-event-workspace/app";

@Injectable({
  providedIn: 'root'
})
export class DashboardService implements OnInit {
  title = signal('app.title')

  constructor(private settingsService: SettingsService, private pageTitle: Title) {
  }

  ngOnInit(): void {
    this.settingsService.getTitle().subscribe(d => {
      this.pageTitle.setTitle(d.text)
      this.title.set(d.text + ' Backoffice')
    })
  }


}
