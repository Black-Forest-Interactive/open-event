import {Component} from '@angular/core';
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {LoadingScreenComponent} from "./dashboard/loading-screen/loading-screen.component";
import {environment} from './../environments/environment';
import LogRocket from 'logrocket';
import {Location} from '@angular/common';
import {AppService} from "./app.service";
import {Subscription} from "rxjs";


@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    standalone: false
})
export class AppComponent {
  title = 'open-event';

  dialogRef: MatDialogRef<any> | undefined
  private subscription: Subscription | undefined

  constructor(
    protected service: AppService,
    private location: Location,
    private dialog: MatDialog
  ) {

    if (environment.logrocket && environment.logrocketAppId.length > 0) {
      LogRocket.init(environment.logrocketAppId)
    }
  }

  ngOnInit() {
    let url = this.location.path()
    if (url.includes("share/info")) return

    this.dialogRef = this.dialog.open(LoadingScreenComponent, {disableClose: true})
    this.subscription = this.service.validated.subscribe(d => this.handleValidated(d))
    this.service.validate()

  }

  private handleValidated(validated: boolean) {
    if (environment.logrocket && this.service.account) {
      LogRocket.identify(this.service.account.id + '')
    }
    this.dialogRef?.close()
    if (this.subscription) {
      this.subscription.unsubscribe()
      this.subscription = undefined
    }
  }
}
