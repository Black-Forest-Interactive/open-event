import { Component, effect, inject } from '@angular/core'
import { RouterModule } from '@angular/router'
import { Location } from '@angular/common'
import { MatDialog, MatDialogRef } from '@angular/material/dialog'
import LogRocket from 'logrocket'
import { LoadingScreenComponent } from '@open-event/shared'
import { AppService } from '../shared/app.service'
import { environment } from '../environments/environment'
import { DashboardComponent } from '../shared/dashboard/dashboard.component'

@Component({
  imports: [RouterModule, DashboardComponent],
  selector: 'admin-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  protected readonly service = inject(AppService)
  private readonly location = inject(Location)
  private readonly dialog = inject(MatDialog)

  private dialogRef: MatDialogRef<unknown> | undefined

  constructor() {
    if (environment.logrocket && environment.logrocketAppId.length > 0) {
      LogRocket.init(environment.logrocketAppId)
    }

    if (!this.location.path().includes('share/info')) {
      this.dialogRef = this.dialog.open(LoadingScreenComponent, { disableClose: true })

      effect(() => {
        if (!this.service.isValidated()) return
        const account = this.service.account()
        if (environment.logrocket && account) {
          LogRocket.identify(account.id + '')
        }
        this.dialogRef?.close()
      })
    }
  }
}
