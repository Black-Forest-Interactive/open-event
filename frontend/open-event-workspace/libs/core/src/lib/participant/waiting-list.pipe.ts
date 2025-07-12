import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'waitingList'
})
export class WaitingListPipe implements PipeTransform {

  transform(value: boolean, ...args: unknown[]): unknown {
    if (value) {
      return '<mat-icon>pending_actions</mat-icon>'
    }
    return null;
  }

}
