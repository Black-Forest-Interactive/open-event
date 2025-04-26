import {Pipe, PipeTransform} from '@angular/core';
import {AccountInfo} from "@open-event-workspace/core";

@Pipe({
  name: 'accountDisplayName',
  standalone: true
})
export class AccountDisplayNamePipe implements PipeTransform {

  transform(account: AccountInfo, ...args: any[]): any {
    if (account == null) return "";
    if (this.isStringValid(account.firstName) && this.isStringValid(account.lastName)) {
      return account.firstName + " " + account.lastName
    } else {
      return account.name
    }
  }

  private isStringValid(str: string): boolean {
    return str != null && str.length > 0;
  }

}
