import { Component, ChangeDetectionStrategy } from '@angular/core'
import { RouterModule } from '@angular/router'

@Component({
  selector: 'lib-forbidden',
  imports: [RouterModule],
  templateUrl: './forbidden.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrls: ['./forbidden.component.scss']
})
export class ForbiddenComponent {}
