import {Component, computed, resource, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {toPromise} from "@open-event-workspace/shared";
import {SettingsService} from "@open-event-workspace/app";
import {DomSanitizer} from "@angular/platform-browser";
import {MatCard} from "@angular/material/card";
import {TranslatePipe} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";

@Component({
  selector: 'app-imprint',
  imports: [CommonModule, MatCard, TranslatePipe, MatDivider],
  templateUrl: './imprint.component.html',
  styleUrl: './imprint.component.scss',
})
export class ImprintComponent {

  termsResource = resource({
    request: signal(""),
    loader: (param) => {
      return toPromise(this.service.getTerms())
    }
  })

  terms = computed(() => this.sanitizer.bypassSecurityTrustHtml(this.termsResource.value()?.text ?? ""))

  constructor(private service: SettingsService, private sanitizer: DomSanitizer) {
  }

}
