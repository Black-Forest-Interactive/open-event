import {Component, OnInit} from '@angular/core';
import {RouterModule} from '@angular/router';
import {AsyncPipe, Location} from "@angular/common";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Subscription} from "rxjs";
import LogRocket from "logrocket";
import {LoadingScreenComponent} from "@open-event-workspace/shared";
import {AppService} from '../shared/app.service';
import {DashboardComponent} from '../shared/dashboard/dashboard.component';
import {environment} from "../environments/environment";

@Component({
  imports: [RouterModule, AsyncPipe, DashboardComponent],
  selector: 'boffice-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  title = 'open-event-backoffice';

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
