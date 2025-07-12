import {Component, inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {ExternalParticipant} from "@open-event-workspace/external";
import {MatButtonModule} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {WaitingListPipe} from "@open-event-workspace/core";

@Component({
  selector: 'app-confirm-participation-response-dialog',
  imports: [
    MatDialogContent,
    TranslatePipe,
    MatIcon,
    MatButtonModule,
    MatDialogActions,
    MatDialogClose,
    MatDivider,
    WaitingListPipe,
    MatDialogTitle
  ],
  templateUrl: './confirm-participation-response-dialog.component.html',
  styleUrl: './confirm-participation-response-dialog.component.scss'
})
export class ConfirmParticipationResponseDialogComponent {
  participant = inject<ExternalParticipant>(MAT_DIALOG_DATA)
}
