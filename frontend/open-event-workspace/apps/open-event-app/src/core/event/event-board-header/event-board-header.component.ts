import {Component, EventEmitter, input, OnInit, Output} from '@angular/core';
import {debounceTime, distinctUntilChanged} from "rxjs";
import {EventBoardService} from "../event-board.service";
import {MatToolbar} from "@angular/material/toolbar";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {RouterLink} from "@angular/router";
import {MatButtonToggle, MatButtonToggleGroup} from "@angular/material/button-toggle";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";

@Component({
  selector: 'app-event-board-header',
  templateUrl: './event-board-header.component.html',
  styleUrls: ['./event-board-header.component.scss'],
  imports: [
    MatToolbar,
    MatFormField,
    MatIcon,
    TranslatePipe,
    MatInput,
    MatIconButton,
    MatProgressSpinner,
    MatButton,
    RouterLink,
    MatButtonToggleGroup,
    MatButtonToggle,
    MatMenuTrigger,
    MatMenuItem,
    MatMenu,
    MatLabel
  ],
  standalone: true
})
export class EventBoardHeaderComponent implements OnInit {

  keyUp: EventEmitter<string> = new EventEmitter<string>()
  mobileView = input<boolean>(false)
  mode = input<string>('')

  @Output() modeChanged = new EventEmitter<string>


  constructor(public service: EventBoardService) {
  }

  ngOnInit() {
    this.keyUp.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(query => this.service.fullTextSearch = query)
  }


}
