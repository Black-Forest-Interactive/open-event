import {Component, computed, input, output} from '@angular/core';
import {MatDivider} from "@angular/material/divider";
import {MatIcon} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {TranslateModule} from "@ngx-translate/core";
import {MatProgressSpinner} from "@angular/material/progress-spinner";

@Component({
  selector: 'app-event-action',
  imports: [MatDivider, MatIcon, MatButtonModule, MatProgressSpinner, TranslateModule],
  templateUrl: './event-action.component.html',
  styleUrl: './event-action.component.scss'
})
export class EventActionComponent {

  processing = input(false)
  status = input.required<string>()

  participationPossible = computed(() => this.status() !== 'UNCONFIRMED' && !this.processing() )
  visible = computed(() => this.participationPossible())

  participateEvent = output()

}
