import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {Setting, SettingChangeRequest} from "@open-event-workspace/core";
import {SettingsService} from "@open-event-workspace/backoffice";
import {TranslatePipe} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {MatButtonModule} from "@angular/material/button";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'boffice-settings-change-dialog',
  templateUrl: './settings-change-dialog.component.html',
  styleUrl: './settings-change-dialog.component.scss',
  imports: [
    TranslatePipe,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatIconModule,
    MatInput
  ],
  standalone: true
})
export class SettingsChangeDialogComponent {
  fg: FormGroup

  constructor(
    private fb: FormBuilder,
    private service: SettingsService,
    public dialogRef: MatDialogRef<SettingsChangeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Setting | null
  ) {
    this.fg = this.fb.group({
        key: [data?.key ?? '', Validators.required],
        value: [data?.value ?? '', Validators.required],
        type: [data?.type ?? '', Validators.required]
      }
    )
    if (this.data) {
      this.fg.controls['key'].disable()
      this.fg.controls['type'].disable()
    }
  }

  onCancelClick(): void {
    this.dialogRef.close(false)
  }

  onSaveClick() {
    if (!this.fg.valid) {
      this.dialogRef.close(false)
    } else {
      this.fg.controls['key'].enable()
      this.fg.controls['type'].enable()

      let value = this.fg.value
      let request = new SettingChangeRequest(value.key, value.value, value.type)
      let observable = (this.data) ? this.service.updateSetting(this.data.id, request) : this.service.createSetting(request)
      observable.subscribe({
        next: val => this.dialogRef.close(true),
        error: err => this.dialogRef.close(true),
      })
    }
  }
}
