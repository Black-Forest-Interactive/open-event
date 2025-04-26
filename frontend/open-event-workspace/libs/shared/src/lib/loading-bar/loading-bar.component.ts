import {Component, input} from '@angular/core';
import {MatProgressBar} from "@angular/material/progress-bar";

@Component({
  selector: 'app-loading-bar',
  imports: [
    MatProgressBar
  ],
  templateUrl: './loading-bar.component.html',
  styleUrl: './loading-bar.component.scss'
})
export class LoadingBarComponent {
  reloading = input(false)
}
