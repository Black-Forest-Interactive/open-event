import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-event-change-help',
  templateUrl: './event-change-help.component.html',
  styleUrls: ['./event-change-help.component.scss']
})
export class EventChangeHelpComponent {
  @Input() step: number = 0
}
