import { computed, effect, inject, Injectable, resource, Signal } from '@angular/core'
import { LangChangeEvent, TranslateService } from '@ngx-translate/core'
import { map } from 'rxjs'
import { MatDialog } from '@angular/material/dialog'
import { Account, AccountInfo, AccountValidationResult, Profile } from '@open-event/core'
import { AuthService, ConfirmDialogComponent, toPromise } from '@open-event/shared'
import { toSignal } from '@angular/core/rxjs-interop'
import { AccountService } from '@open-event/portal'

@Injectable({
  providedIn: 'root'
})
export class AppService {
  readonly authService = inject(AuthService)
  private readonly accountService = inject(AccountService)
  private readonly translate = inject(TranslateService)
  readonly lang: Signal<string> = toSignal(this.translate.onLangChange.pipe(map((e: LangChangeEvent) => e.lang)), { initialValue: this.translate.getCurrentLang() })
  private readonly dialog = inject(MatDialog)
  private readonly validationResource = resource<AccountValidationResult, string>({
    params: () => this.translate.getCurrentLang() ?? 'de',
    loader: ({ params: lang, abortSignal }) => toPromise(this.accountService.validate(lang), abortSignal)
  })

  readonly account = computed<Account | undefined>(() => this.validationResource.value()?.account)
  readonly profile = computed<Profile | undefined>(() => this.validationResource.value()?.profile)
  readonly info = computed<AccountInfo | undefined>(() => this.validationResource.value()?.info)
  readonly isValidated = computed(() => !!this.validationResource.value())

  constructor() {
    effect(() => {
      const result = this.validationResource.value()
      if (!result) return
      this.translate.setFallbackLang('en')
      this.translate.use(result.profile.language)
    })
  }

  logout() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '250px',
      data: { title: 'user.logout.confirm.Title', text: 'user.logout.confirm.Text' }
    })
    dialogRef.afterClosed().subscribe((result) => {
      if (result) this.authService.logout()
    })
  }
}
