import { Injectable } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatDialog } from '@angular/material/dialog'
import { EventShareSheetComponent, EventShareSheetData } from './event-share-sheet/event-share-sheet.component'
import { EventShareDialogComponent } from './event-share-dialog/event-share-dialog.component'

@Injectable({
  providedIn: 'root'
})
export class EventShareLauncher {
  private static readonly MOBILE_QUERY = '(max-width: 759.98px)'

  static open(dialog: MatDialog, bottomSheet: MatBottomSheet, breakpointObserver: BreakpointObserver, data: EventShareSheetData) {
    if (breakpointObserver.isMatched(EventShareLauncher.MOBILE_QUERY)) {
      bottomSheet.open(EventShareSheetComponent, { data })
    } else {
      dialog.open(EventShareDialogComponent, { data, width: '480px', maxWidth: '95vw' })
    }
  }
}
