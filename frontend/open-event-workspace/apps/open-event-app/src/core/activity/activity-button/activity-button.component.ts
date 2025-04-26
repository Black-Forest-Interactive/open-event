import {Component, viewChild} from '@angular/core';
import {Subscription} from "rxjs";
import {MatMenuTrigger} from "@angular/material/menu";
import {Router} from "@angular/router";
import {Activity, ActivityInfo} from "@open-event-workspace/core";
import {ActivityIndicatorComponent} from "../activity-indicator/activity-indicator.component";
import {ActivityMenuComponent} from "../activity-menu/activity-menu.component";
import {ActivityService} from "@open-event-workspace/app";


@Component({
  selector: 'app-activity-button',
  templateUrl: './activity-button.component.html',
  styleUrl: './activity-button.component.scss',
  imports: [
    ActivityIndicatorComponent,
    ActivityMenuComponent
  ],
  standalone: true
})
export class ActivityButtonComponent {

  reloading: boolean = false
  data: ActivityInfo[] = []
  unreadInfos = 0
  menuTrigger = viewChild.required<MatMenuTrigger>('menuTrigger')

  private subscription: Subscription | undefined

  constructor(private service: ActivityService, private router: Router) {
  }

  ngOnInit() {
    // this.reload()
    // this.subscription = interval(10000).subscribe(value => this.reload())
  }

  ngOnDestroy() {
    if (this.subscription) this.subscription.unsubscribe()
  }

  private reload() {
    if (this.reloading) return
    this.refresh()
  }


  handleActivityClick(a: ActivityInfo) {
    this.service.markReadSingle(a.activity.id).subscribe({
      next: value => this.navigateToSource(value),
      error: err => this.handleError(err)
    })
  }

  private navigateToSource(activity: Activity) {
    if (activity.source === 'EVENT' || activity.source === 'REGISTRATION') {
      this.router.navigate(['event', 'details', activity.sourceId]).then()
      this.refresh()
    } else {
      this.refresh()
    }
  }

  private refresh() {
    if (this.reloading) return
    this.reloading = true
    this.service.unreadInfo().subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }

  handleMarkAllReadClick() {
    this.reloading = true
    this.service.markReadAll().subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }


  private handleData(value: ActivityInfo[]) {
    this.data = value
    this.unreadInfos = this.data.filter(d => !d.read).length
    this.reloading = false
    if (this.unreadInfos == 0) this.menuTrigger().closeMenu()
  }

  private handleError(err: any) {
    this.data = []
    this.unreadInfos = 0
    this.reloading = false
    this.menuTrigger().closeMenu()
  }
}
