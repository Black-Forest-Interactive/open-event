import {Component, OnInit, output, signal} from '@angular/core';
import {ActivityService} from "@open-event-workspace/app";
import {interval, startWith, switchMap} from "rxjs";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {MatBadge} from "@angular/material/badge";
import {MatIcon} from "@angular/material/icon";
import {MatIconAnchor, MatIconButton} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-activity-indicator',
  imports: [
    MatBadge,
    MatIcon,
    MatIconAnchor,
    MatIconButton,
    RouterLink
  ],
  templateUrl: './activity-indicator.component.html',
  styleUrl: './activity-indicator.component.scss'
})
export class ActivityIndicatorComponent implements OnInit {

  unreadMessages = signal<number>(0)
  readonly clicked = output<MouseEvent>()

  constructor(private service: ActivityService) {
    interval(10000).pipe(
      startWith(0),
      switchMap(() => this.service.unreadAmount()),
      takeUntilDestroyed()
    ).subscribe(value => this.unreadMessages.set(value))
  }

  ngOnInit() {

  }
}
