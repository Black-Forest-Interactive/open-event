import { Component, inject, OnInit } from '@angular/core'
import { RouterModule } from '@angular/router'
import { MatDialog, MatDialogRef } from '@angular/material/dialog'
import { Subscription } from 'rxjs'
import { AppService } from '../shared/app.service'
import { environment } from '../environments/environment'
import { AsyncPipe, Location } from '@angular/common'
import LogRocket from 'logrocket'
import { LoadingScreenComponent } from '@open-event/shared'
import { DashboardComponent } from '../shared/dashboard/dashboard.component'

@Component({
  imports: [RouterModule, AsyncPipe, DashboardComponent],
  selector: 'portal-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  protected service = inject(AppService)
  private location = inject(Location)
  private dialog = inject(MatDialog)

  title = 'open-event-app'

  dialogRef: MatDialogRef<any> | undefined
  private subscription: Subscription | undefined

  constructor() {
    if (environment.logrocket && environment.logrocketAppId.length > 0) {
      LogRocket.init(environment.logrocketAppId)
    }
  }

  ngOnInit() {
    const url = this.location.path()
    if (url.includes('share/info')) return

    this.dialogRef = this.dialog.open(LoadingScreenComponent, {
      disableClose: true
    })
    this.subscription = this.service.validated.subscribe(() => this.handleValidated())
    this.service.validate()
  }

  private handleValidated() {
    if (environment.logrocket && this.service.account) {
      LogRocket.identify(this.service.account.id + '')
    }
    this.dialogRef?.close()
    if (this.subscription) {
      this.subscription.unsubscribe()
      this.subscription = undefined
    }
  }
}
