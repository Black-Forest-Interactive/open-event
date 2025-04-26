import {Component, computed, input, resource} from '@angular/core';
import {Account} from "@open-event-workspace/core";
import {toPromise} from "@open-event-workspace/shared";
import {AccountService} from "@open-event-workspace/backoffice";
import {TranslatePipe} from "@ngx-translate/core";
import {BoardCardComponent} from "../../../shared/board-card/board-card.component";

@Component({
  selector: 'app-account-details-profile',
  imports: [
    TranslatePipe,
    BoardCardComponent
  ],
  templateUrl: './account-details-profile.component.html',
  styleUrl: './account-details-profile.component.scss'
})
export class AccountDetailsProfileComponent {
  data = input.required<Account>()

  profileResource = resource({
    request: this.data,
    loader: (param) => {
      return toPromise(this.service.getProfile(param.request.id))
    }
  })

  profile = computed(this.profileResource.value ?? undefined)
  loading = this.profileResource.isLoading
  error = this.profileResource.error

  constructor(private service: AccountService) {
  }
}
