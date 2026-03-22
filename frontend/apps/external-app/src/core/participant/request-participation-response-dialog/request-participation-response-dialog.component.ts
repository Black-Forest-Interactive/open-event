import {Component} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent} from "@angular/material/dialog";
import {TranslatePipe} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";

@Component({
  selector: 'app-request-participation-response-dialog',
  imports: [
    MatDialogContent,
    TranslatePipe,
    MatIcon,
    MatButtonModule,
    MatDialogActions,
    MatDialogClose,
    MatDivider
  ],
  templateUrl: './request-participation-response-dialog.component.html',
  styleUrl: './request-participation-response-dialog.component.scss'
})
export class RequestParticipationResponseDialogComponent {

}
