import { inject, Injectable, Signal } from "@angular/core";
import { LangChangeEvent, TranslateService } from "@ngx-translate/core";
import { map, Subject } from "rxjs";
import { MatDialog } from "@angular/material/dialog";
import {
  Account,
  AccountInfo,
  AccountValidationResult,
  Profile,
} from "@open-event-workspace/core";
import {
  AuthService,
  ConfirmDialogComponent,
} from "@open-event-workspace/shared";
import { toSignal } from "@angular/core/rxjs-interop";
import { AccountService } from "@open-event-workspace/app";

@Injectable({
  providedIn: "root",
})
export class AppService {
  private accountService = inject(AccountService);
  private translate = inject(TranslateService);
  public authService = inject(AuthService);
  private dialog = inject(MatDialog);

  validated: Subject<boolean> = new Subject();
  account: Account | undefined;
  profile: Profile | undefined;
  info: AccountInfo | undefined;
  lang: Signal<string> = toSignal(
    this.translate.onLangChange.pipe(
      map((value: LangChangeEvent) => value.lang),
    ),
    { initialValue: this.translate.getCurrentLang() },
  );

  validate() {
    const l = this.lang() ?? this.translate.getCurrentLang() ?? "de";
    this.accountService
      .validate(l)
      .subscribe((d) => this.handleValidationResult(d));
  }

  private handleValidationResult(d: AccountValidationResult) {
    this.account = d.account;
    this.profile = d.profile;
    this.info = d.info;
    this.translate.setFallbackLang("en");
    this.translate.use(d.profile.language);

    this.validated.next(true);
  }

  logout() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: "250px",
      data: {
        title: "user.logout.confirm.Title",
        text: "user.logout.confirm.Text",
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) this.authService.logout();
    });
  }
}
