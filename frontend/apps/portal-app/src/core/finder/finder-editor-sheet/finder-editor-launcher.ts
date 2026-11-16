import { inject, Injectable } from '@angular/core'
import { BreakpointObserver } from '@angular/cdk/layout'
import { MatBottomSheet } from '@angular/material/bottom-sheet'
import { MatDialog } from '@angular/material/dialog'
import { Finder, FinderInfo } from '@open-event/core'
import { Observable } from 'rxjs'
import { FinderEditorSheetComponent, FinderEditorSheetData } from './finder-editor-sheet.component'

/** Opens the finder editor as bottom sheet on mobile and as dialog on larger screens. */
@Injectable({
  providedIn: 'root'
})
export class FinderEditorLauncher {
  private static readonly MOBILE_QUERY = '(max-width: 759.98px)'

  private dialog = inject(MatDialog)
  private bottomSheet = inject(MatBottomSheet)
  private breakpointObserver = inject(BreakpointObserver)

  open(finder?: FinderInfo): Observable<Finder | undefined> {
    const data: FinderEditorSheetData = { finder }
    if (this.breakpointObserver.isMatched(FinderEditorLauncher.MOBILE_QUERY)) {
      return this.bottomSheet.open<FinderEditorSheetComponent, FinderEditorSheetData, Finder>(FinderEditorSheetComponent, { data }).afterDismissed()
    }
    return this.dialog.open<FinderEditorSheetComponent, FinderEditorSheetData, Finder>(FinderEditorSheetComponent, { data, width: '540px', maxWidth: '95vw' }).afterClosed()
  }
}
