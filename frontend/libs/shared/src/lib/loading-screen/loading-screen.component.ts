import { Component, ChangeDetectionStrategy } from '@angular/core'
import { MatDialogContent } from '@angular/material/dialog'
import { TranslatePipe } from '@ngx-translate/core'
import { MatProgressBar } from '@angular/material/progress-bar'

@Component({
  selector: 'lib-loading-screen',
  templateUrl: './loading-screen.component.html',
  styleUrls: ['./loading-screen.component.scss'],
  imports: [MatDialogContent, TranslatePipe, MatProgressBar],
  changeDetection: ChangeDetectionStrategy.Eager
})
export class LoadingScreenComponent {}
