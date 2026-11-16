import { ChangeDetectionStrategy, Component, effect, inject, input, OnInit } from '@angular/core'
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms'
import { MatFormFieldModule } from '@angular/material/form-field'
import { MatInputModule } from '@angular/material/input'
import { MatIconModule } from '@angular/material/icon'
import { TranslatePipe } from '@ngx-translate/core'
import { EventContact, EventContactChangeRequest } from '@open-event/core'

const PHONE_PATTERN = /^[0-9+()/\-. ]*$/

/**
 * Optional phone / mobile of the organizer, visible only for registered participants.
 */
@Component({
  selector: 'lib-event-change-contact',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatIconModule, TranslatePipe],
  changeDetection: ChangeDetectionStrategy.Eager,
  template: `
    <div [formGroup]="fg" class="flex flex-col gap-2">
      <p class="text-sm text-on-surface-variant m-0">{{ 'event.contact.hint' | translate }}</p>
      <div class="flex flex-col sm:flex-row gap-3">
        <mat-form-field class="flex-1">
          <mat-label>{{ 'event.contact.phone' | translate }}</mat-label>
          <mat-icon matPrefix>call</mat-icon>
          <input formControlName="phone" matInput type="tel" autocomplete="tel" />
          @if (fg.get('phone')?.invalid) {
            <mat-error>{{ 'event.contact.invalid' | translate }}</mat-error>
          }
        </mat-form-field>
        <mat-form-field class="flex-1">
          <mat-label>{{ 'event.contact.mobile' | translate }}</mat-label>
          <mat-icon matPrefix>smartphone</mat-icon>
          <input formControlName="mobile" matInput type="tel" autocomplete="tel" />
          @if (fg.get('mobile')?.invalid) {
            <mat-error>{{ 'event.contact.invalid' | translate }}</mat-error>
          }
        </mat-form-field>
      </div>
    </div>
  `
})
export class EventChangeContactComponent implements OnInit {
  parent = input.required<FormGroup>()
  data = input<Pick<EventContact, 'phone' | 'mobile'> | null>()

  fg: FormGroup

  constructor() {
    const fb = inject(FormBuilder)
    this.fg = fb.group({
      phone: ['', [Validators.maxLength(50), Validators.pattern(PHONE_PATTERN)]],
      mobile: ['', [Validators.maxLength(50), Validators.pattern(PHONE_PATTERN)]]
    })

    effect(() => {
      const contact = this.data()
      if (contact) this.fg.setValue({ phone: contact.phone ?? '', mobile: contact.mobile ?? '' })
    })
  }

  ngOnInit() {
    this.parent().addControl('contact', this.fg)
  }
}

export function toContactChangeRequest(value: any): EventContactChangeRequest | undefined {
  if (!value) return undefined
  return new EventContactChangeRequest((value.phone ?? '').trim(), (value.mobile ?? '').trim())
}
