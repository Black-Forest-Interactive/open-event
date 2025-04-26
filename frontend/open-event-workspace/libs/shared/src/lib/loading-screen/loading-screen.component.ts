import {Component} from '@angular/core';
import {MatDialogContent} from "@angular/material/dialog";
import {TranslatePipe} from "@ngx-translate/core";
import {MatProgressBar} from "@angular/material/progress-bar";

@Component({
  selector: 'app-loading-screen',
  templateUrl: './loading-screen.component.html',
  styleUrls: ['./loading-screen.component.scss'],
  imports: [
    MatDialogContent,
    TranslatePipe,
    MatProgressBar
  ],
  standalone: true
})
export class LoadingScreenComponent {

}
