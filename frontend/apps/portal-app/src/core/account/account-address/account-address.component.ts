import { Component } from '@angular/core'

import { AddressBoardComponent } from '../../address/address-board/address-board.component'

@Component({
  selector: 'portal-account-address',
  imports: [AddressBoardComponent],
  templateUrl: './account-address.component.html',
  styleUrl: './account-address.component.scss'
})
export class AccountAddressComponent {}
