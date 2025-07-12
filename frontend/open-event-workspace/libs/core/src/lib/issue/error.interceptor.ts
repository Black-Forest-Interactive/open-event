import {HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {MatDialog} from '@angular/material/dialog';
import {IssueCreateDialogComponent} from "./issue-create/issue-create-dialog.component";
import {environment} from "../../../../../apps/open-event-app/src/environments/environment";


export function errorInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn): Observable<HttpEvent<unknown>> {

  const dialog = inject(MatDialog)

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      console.log("Error intercepted")
      if (environment.features.errorInterception) {
        if (error.status >= 500) {
          dialog.open(IssueCreateDialogComponent, {
            data: error
          });
        }
      }
      return throwError(() => error);
    })
  );
}
