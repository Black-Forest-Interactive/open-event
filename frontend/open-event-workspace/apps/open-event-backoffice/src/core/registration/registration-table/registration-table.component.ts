import {Component, computed, effect, input, output, resource, signal} from '@angular/core';
import {RegistrationService} from '@open-event-workspace/backoffice';
import {Participant, ParticipantDetails, ParticipateResponse, RegistrationInfo} from "@open-event-workspace/core";
import {toPromise} from "@open-event-workspace/shared";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {TranslatePipe} from "@ngx-translate/core";
import {DatePipe} from "@angular/common";
import {MatDialog} from "@angular/material/dialog";
import {RegistrationParticipantEditDialogComponent} from "../registration-participant-edit-dialog/registration-participant-edit-dialog.component";
import {RegistrationParticipantRemoveDialogComponent} from "../registration-participant-remove-dialog/registration-participant-remove-dialog.component";

@Component({
  selector: 'app-registration-table',
  imports: [MatTableModule, MatButtonModule, MatIconModule, MatPaginatorModule, MatSortModule, TranslatePipe, DatePipe],
  templateUrl: './registration-table.component.html',
  styleUrl: './registration-table.component.scss'
})
export class RegistrationTableComponent {

  data = input.required<RegistrationInfo>()
  changeResponse = output<ParticipateResponse>()

  registrationResource = resource({
    request: this.data,
    loader: (param) => {
      return toPromise(this.service.getRegistrationDetails(param.request.registration.id))
    }
  })
  registration = computed(this.registrationResource.value ?? undefined)

  updating = signal(false)
  loading = computed(() => this.registrationResource.isLoading() || this.updating())
  error = this.registrationResource.error

  displayedColumns: string[] = ['rank', 'size', 'status', 'waitinglist', 'name', 'email', 'phone', 'mobile', 'timestamp', 'action']
  dataSource = new MatTableDataSource<ParticipantDetails>([])

  constructor(
    private service: RegistrationService,
    private dialog: MatDialog
  ) {
    effect(() => {
      this.dataSource.data = this.registration()?.participants ?? []
    });
  }

  editParticipant(part: Participant) {
    this.updating.set(true)
    let dialogRef = this.dialog.open(RegistrationParticipantEditDialogComponent, {data: {registration: this.registration()!!.registration, participant: part}})
    dialogRef.afterClosed().subscribe(response => {
      if (response) this.handleParticipateResponse(response)
      this.updating.set(false)
    })
  }

  removeParticipant(part: Participant) {
    this.updating.set(true)
    let dialogRef = this.dialog.open(RegistrationParticipantRemoveDialogComponent, {data: {registration: this.registration()!!.registration, participant: part}})
    dialogRef.afterClosed().subscribe(response => {
      if (response) this.handleParticipateResponse(response)
      this.updating.set(false)
    })
  }

  private handleParticipateResponse(response: ParticipateResponse) {
    this.changeResponse.emit(response)
    this.registrationResource.reload()
  }
}
