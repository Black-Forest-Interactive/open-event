import { Component, inject, OnInit } from '@angular/core'
import { RouterModule } from '@angular/router'
import { AsyncPipe, Location } from '@angular/common'
import { MatDialog, MatDialogRef } from '@angular/material/dialog'
import { Subscription } from 'rxjs'
import LogRocket from 'logrocket'
import { LoadingScreenComponent } from '@open-event/shared'
import { AppService } from '../shared/app.service'
import { environment } from '../environments/environment'
import { DashboardComponent } from '../shared/dashboard/dashboard.component'

@Component({
  imports: [RouterModule, AsyncPipe, DashboardComponent],
  selector: 'admin-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'admin-app'

  protected readonly service = inject(AppService)
  private location = inject(Location)
  private dialog = inject(MatDialog)

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
    this.subscription = this.service.validated.subscribe((d) => this.handleValidated(d))
    this.service.validate()
  }

  private handleValidated(validated: boolean) {
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
