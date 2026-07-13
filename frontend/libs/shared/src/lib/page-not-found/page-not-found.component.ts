import { Component, ChangeDetectionStrategy } from '@angular/core'

@Component({
  selector: 'lib-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.scss'],
  changeDetection: ChangeDetectionStrategy.Eager,
  standalone: true
})
export class PageNotFoundComponent {}
