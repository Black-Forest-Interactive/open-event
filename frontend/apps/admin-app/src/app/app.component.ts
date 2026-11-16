import { Component, effect, inject, ChangeDetectionStrategy } from '@angular/core'
import { RouterModule } from '@angular/router'
import { Location } from '@angular/common'
import { MatDialog, MatDialogRef } from '@angular/material/dialog'
import { LoadingScreenComponent } from '@open-event/shared'
import { AppService } from '../shared/app.service'
import { DashboardComponent } from '../shared/dashboard/dashboard.component'

@Component({
  imports: [RouterModule, DashboardComponent],
  selector: 'admin-root',
  templateUrl: './app.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  protected readonly service = inject(AppService)
  private readonly location = inject(Location)
  private readonly dialog = inject(MatDialog)

  private dialogRef: MatDialogRef<unknown> | undefined

  constructor() {
    if (!this.location.path().includes('share/info')) {
      this.dialogRef = this.dialog.open(LoadingScreenComponent, { disableClose: true })

      effect(() => {
        if (!this.service.isValidated()) return
        this.dialogRef?.close()
      })
    }
  }
}
