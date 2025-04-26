import {Component} from '@angular/core';
import {MatCard} from "@angular/material/card";

@Component({
  selector: 'app-preferences-board',
  templateUrl: './preferences-board.component.html',
  styleUrl: './preferences-board.component.scss',
  imports: [
    MatCard
  ],
  standalone: true
})
export class PreferencesBoardComponent {

}
