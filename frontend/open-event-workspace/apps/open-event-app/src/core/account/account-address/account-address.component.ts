import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AddressBoardComponent} from "../../address/address-board/address-board.component";

@Component({
  selector: 'app-account-address',
  imports: [CommonModule, AddressBoardComponent],
  templateUrl: './account-address.component.html',
  styleUrl: './account-address.component.scss',
})
export class AccountAddressComponent {

}
