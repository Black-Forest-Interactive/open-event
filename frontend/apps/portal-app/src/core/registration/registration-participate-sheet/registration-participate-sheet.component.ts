import { Component, computed, inject, signal } from '@angular/core'
import { MAT_BOTTOM_SHEET_DATA, MatBottomSheetRef } from '@angular/material/bottom-sheet'
import { Observable } from 'rxjs'
import { EventInfo, Participant, ParticipateRequest, ParticipateResponse } from '@open-event/core'
import { downloadIcs } from '@open-event/shared'
import { TranslatePipe } from '@ngx-translate/core'
import { MatButton, MatIconButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { DatePipe } from '@angular/common'
import { MatFormField, MatLabel } from '@angular/material/form-field'
import { MatInput } from '@angular/material/input'

export interface RegistrationParticipateSheetData {
  info: EventInfo
  participant?: Participant
  titleKey: string
  submit: (request: ParticipateRequest) => Observable<ParticipateResponse>
}

@Component({
  selector: 'portal-registration-participate-sheet',
  templateUrl: './registration-participate-sheet.component.html',
  styleUrl: './registration-participate-sheet.component.scss',
  imports: [TranslatePipe, MatButton, MatIconButton, MatIcon, DatePipe, MatFormField, MatLabel, MatInput],
  standalone: true
})
export class RegistrationParticipateSheetComponent {
  data = inject<RegistrationParticipateSheetData>(MAT_BOTTOM_SHEET_DATA)

  private bottomSheetRef = inject<MatBottomSheetRef<RegistrationParticipateSheetComponent, ParticipateResponse>>(MatBottomSheetRef)
  private currentSize = this.data.participant?.size ?? 0
  private response = signal<ParticipateResponse | undefined>(undefined)

  readonly step = signal<'form' | 'success'>('form')
  readonly submitting = signal(false)
  readonly persons = signal(Math.max(1, this.currentSize))
  readonly note = signal(this.data.participant?.note ?? '')

  readonly event = computed(() => this.data.info.event)
  readonly remaining = computed(() => {
    const registration = this.data.info.registration
    if (!registration) return 0
    const taken = registration.participants.filter((p) => !p.waitingList).reduce((sum, p) => sum + p.size, 0)
    return Math.max(0, registration.registration.maxGuestAmount - taken + this.currentSize)
  })
  readonly maxPersons = computed(() => Math.max(1, Math.min(this.remaining(), 6)))

  increment() {
    if (this.persons() < this.maxPersons()) this.persons.set(this.persons() + 1)
  }

  decrement() {
    if (this.persons() > 1) this.persons.set(this.persons() - 1)
  }

  submit() {
    if (this.submitting()) return
    this.submitting.set(true)
    this.data.submit(new ParticipateRequest(this.persons(), this.note())).subscribe((response) => {
      this.submitting.set(false)
      this.response.set(response)
      this.step.set('success')
    })
  }

  close() {
    this.bottomSheetRef.dismiss(this.response())
  }

  downloadCalendar() {
    const info = this.data.info
    downloadIcs({ title: info.event.title, longText: info.event.longText, start: info.event.start, finish: info.event.finish, location: info.location })
  }
}
