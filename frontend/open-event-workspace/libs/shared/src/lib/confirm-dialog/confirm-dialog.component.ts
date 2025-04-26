import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslatePipe} from "@ngx-translate/core";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {ConfirmDialogConfig} from "./confirm-dialog-config";

@Component({
  selector: 'lib-confirm-dialog',
  imports: [CommonModule,
    TranslatePipe,
    MatDialogActions,
    MatDialogClose,
    MatDialogTitle,
    MatDialogContent,
    MatButton,
    MatIcon],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.scss',
})
export class ConfirmDialogComponent {

  readonly config = inject<ConfirmDialogConfig>(MAT_DIALOG_DATA)
}
