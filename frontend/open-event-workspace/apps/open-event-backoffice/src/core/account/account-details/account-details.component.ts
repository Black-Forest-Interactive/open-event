import {Component, computed, resource, signal} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {AccountService} from "@open-event-workspace/backoffice";
import {toPromise} from "@open-event-workspace/shared";
import {MatCardModule} from "@angular/material/card";
import {MatToolbarModule} from "@angular/material/toolbar";
import {AccountDetailsTitleComponent} from "../account-details-title/account-details-title.component";
import {AccountDetailsPreferencesComponent} from "../account-details-preferences/account-details-preferences.component";
import {AccountDetailsProfileComponent} from "../account-details-profile/account-details-profile.component";
import {AccountDetailsAddressComponent} from "../account-details-address/account-details-address.component";
import {AccountDetailsEventsComponent} from "../account-details-events/account-details-events.component";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {Location} from "@angular/common";
import {MatTabsModule} from "@angular/material/tabs";
import {BoardComponent} from "../../../shared/board/board.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-account-details',
  imports: [
    MatCardModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    AccountDetailsTitleComponent,
    AccountDetailsPreferencesComponent,
    AccountDetailsProfileComponent,
    AccountDetailsAddressComponent,
    AccountDetailsEventsComponent,
    MatTabsModule,
    BoardComponent,
    TranslatePipe
  ],
  templateUrl: './account-details.component.html',
  styleUrl: './account-details.component.scss'
})
export class AccountDetailsComponent {

  id = signal(-1)

  accountResource = resource({
    request: this.id,
    loader: (param) => {
      return toPromise(this.service.getAccount(param.request))
    }
  })


  account = computed(this.accountResource.value ?? undefined)
  loading = this.accountResource.isLoading
  error = this.accountResource.error

  constructor(private service: AccountService, private route: ActivatedRoute, private location: Location) {

    this.route.paramMap.subscribe(params => {
      let id = params.get('id')!
      this.id.set(+id)
    })

  }

  back() {
    this.location.back()
  }
}
