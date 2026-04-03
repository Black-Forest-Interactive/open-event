import { Component, computed, effect, inject, input, output, resource, signal } from '@angular/core'
import { RegistrationService } from '@open-event/admin'
import { Participant, ParticipantDetails, ParticipateResponse, RegistrationInfo } from '@open-event/core'
import { toPromise } from '@open-event/shared'
import { MatTableDataSource, MatTableModule } from '@angular/material/table'
import { MatButtonModule } from '@angular/material/button'
import { MatIconModule } from '@angular/material/icon'
import { MatPaginatorModule } from '@angular/material/paginator'
import { MatSortModule } from '@angular/material/sort'
import { TranslatePipe } from '@ngx-translate/core'
import { DatePipe } from '@angular/common'
import { MatDialog } from '@angular/material/dialog'
import { RegistrationParticipantEditDialogComponent } from '../registration-participant-edit-dialog/registration-participant-edit-dialog.component'
import { RegistrationParticipantRemoveDialogComponent } from '../registration-participant-remove-dialog/registration-participant-remove-dialog.component'

@Component({
  selector: 'admin-registration-table',
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatPaginatorModule, MatSortModule, TranslatePipe, DatePipe],
  templateUrl: './registration-table.component.html',
  styleUrl: './registration-table.component.scss'
})
export class RegistrationTableComponent {
  private service = inject(RegistrationService)
  private dialog = inject(MatDialog)

  data = input.required<RegistrationInfo>()
  changeResponse = output<ParticipateResponse>()

  private registrationResource = resource({
    params: this.data,
    loader: (param) => toPromise(this.service.getRegistrationDetails(param.params.registration.id), param.abortSignal)
  })
  readonly registration = computed(this.registrationResource.value ?? undefined)

  updating = signal(false)
  readonly loading = computed(() => this.registrationResource.isLoading() || this.updating())
  readonly error = this.registrationResource.error

  displayedColumns: string[] = ['rank', 'size', 'status', 'waitinglist', 'name', 'email', 'phone', 'mobile', 'timestamp', 'action']
  dataSource = new MatTableDataSource<ParticipantDetails>([])

  constructor() {
    effect(() => {
      this.dataSource.data = this.registration()?.participants ?? []
    })
  }

  editParticipant(part: Participant) {
    this.updating.set(true)
    const dialogRef = this.dialog.open(RegistrationParticipantEditDialogComponent, {
      data: {
        registration: this.registration()?.registration,
        participant: part
      }
    })
    dialogRef.afterClosed().subscribe((response) => {
      if (response) this.handleParticipateResponse(response)
      this.updating.set(false)
    })
  }

  removeParticipant(part: Participant) {
    this.updating.set(true)
    const dialogRef = this.dialog.open(RegistrationParticipantRemoveDialogComponent, {
      data: {
        registration: this.registration()?.registration,
        participant: part
      }
    })
    dialogRef.afterClosed().subscribe((response) => {
      if (response) this.handleParticipateResponse(response)
      this.updating.set(false)
    })
  }

  private handleParticipateResponse(response: ParticipateResponse) {
    this.changeResponse.emit(response)
    this.registrationResource.reload()
  }
}
