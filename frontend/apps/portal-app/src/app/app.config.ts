import { ApplicationConfig, LOCALE_ID, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core'
import { provideRouter } from '@angular/router'
import { appRoutes } from './app.routes'
import { provideHttpClient, withInterceptors } from '@angular/common/http'
import { includeBearerTokenInterceptor } from 'keycloak-angular'
import { provideKeycloakAngular } from './keycloak.config'
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async'
import { MAT_DATE_LOCALE } from '@angular/material/core'
import { MAT_FORM_FIELD_DEFAULT_OPTIONS } from '@angular/material/form-field'
import { registerLocaleData } from '@angular/common'
import localeDe from '@angular/common/locales/de'
import localeDeExtra from '@angular/common/locales/extra/de'
import { provideToastConfig } from './hot-toast.config'
import { provideQuill } from './quill.config'
import { provideLuxonDateAdapter } from '@angular/material-luxon-adapter'
import { provideTranslateConfig } from './translate.config'
import { ENVIRONMENT } from '@open-event/shared'
import { environment } from '../environments/environment'
import { provideServiceConfig } from './service.config'

registerLocaleData(localeDe, 'de-DE', localeDeExtra)

export const appConfig: ApplicationConfig = {
  providers: [
    provideAnimationsAsync(),
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideLuxonDateAdapter(),
    { provide: MAT_DATE_LOCALE, useValue: 'de-DE' },
    { provide: LOCALE_ID, useValue: 'de-DE' },
    { provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'outline' } },
    provideToastConfig(),
    provideKeycloakAngular(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),
    provideTranslateConfig(),
    provideQuill(),
    provideRouter(appRoutes),
    { provide: ENVIRONMENT, useValue: environment },
    provideServiceConfig()
  ]
}
