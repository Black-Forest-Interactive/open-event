import { Component, computed, inject, input, resource } from '@angular/core'
import { EventInfo, ParticipantDetails } from '@open-event/core'
import { RegistrationService } from '@open-event/portal'
import { AuthService, toPromise } from '@open-event/shared'
import { AccountDisplayNamePipe, AvatarComponent } from '@open-event/ui'
import { TranslatePipe } from '@ngx-translate/core'
import { DatePipe, NgTemplateOutlet } from '@angular/common'
import { MatButton } from '@angular/material/button'
import { MatIcon } from '@angular/material/icon'
import { MatDivider } from '@angular/material/divider'
import FileSaver from 'file-saver'

@Component({
  selector: 'portal-event-guest-list',
  templateUrl: './event-guest-list.component.html',
  styleUrl: './event-guest-list.component.scss',
  imports: [AvatarComponent, AccountDisplayNamePipe, TranslatePipe, DatePipe, NgTemplateOutlet, MatButton, MatIcon, MatDivider],
  standalone: true
})
export class EventGuestListComponent {
  info = input.required<EventInfo>()

  private service = inject(RegistrationService)
  private authService = inject(AuthService)

  private registrationId = computed(() => this.info().registration?.registration.id)

  private detailsResource = resource({
    params: this.registrationId,
    loader: (p) => (p.params !== undefined ? toPromise(this.service.getDetails(p.params), p.abortSignal) : Promise.resolve(undefined))
  })

  private details = computed(() => this.detailsResource.value())

  readonly guests = computed(() => (this.details()?.participants ?? []).filter((p) => !p.waitingList))
  readonly waitList = computed(() => (this.details()?.participants ?? []).filter((p) => p.waitingList))
  readonly totalRegistrations = computed(() => this.guests().length)
  readonly totalPersons = computed(() => this.guests().reduce((sum, p) => sum + p.size, 0))

  isSelf(participant: ParticipantDetails) {
    return participant.author.email === this.authService.getPrincipal()?.email
  }

  emailAll() {
    const emails = this.guests()
      .map((g) => g.author.email)
      .filter((e) => e.length > 0)
    window.location.href = `mailto:?bcc=${encodeURIComponent(emails.join(','))}`
  }

  exportCsv() {
    const rows = [['Name', 'E-Mail', 'Telefon', 'Personen', 'Angemeldet seit']]
    for (const p of [...this.guests(), ...this.waitList()]) {
      rows.push([this.fullName(p), p.author.email, p.author.phone || p.author.mobile, String(p.size), p.timestamp])
    }
    const csv = rows.map((r) => r.map((c) => `"${String(c).replace(/"/g, '""')}"`).join(';')).join('\r\n')
    const blob = new Blob(['﻿' + csv], { type: 'text/csv;charset=utf-8' })
    FileSaver.saveAs(blob, `teilnehmer-${this.info().event.id}.csv`)
  }

  private fullName(participant: ParticipantDetails) {
    const author = participant.author
    return author.firstName && author.lastName ? `${author.firstName} ${author.lastName}` : author.name
  }
}
