import {ApplicationConfig, LOCALE_ID, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {appRoutes} from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {includeBearerTokenInterceptor} from "keycloak-angular";
import {provideKeycloakAngular} from "./keycloak.config";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {MAT_DATE_LOCALE} from "@angular/material/core";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";
import {FullscreenOverlayContainer, OverlayContainer} from "@angular/cdk/overlay";
import {provideShareButtonsOptions} from "ngx-sharebuttons";
import {shareIcons} from "ngx-sharebuttons/icons";
import {MatPaginatorIntl} from "@angular/material/paginator";
import {MatPaginatorI18nService} from "@open-event-workspace/shared";
import {registerLocaleData} from "@angular/common";
import localeDe from '@angular/common/locales/de';
import localeDeExtra from '@angular/common/locales/extra/de';
import {provideTranslateConfig} from "./translate.config";
import {provideToastConfig} from "./hot-toast.config";
import {provideQuill} from "./quill.config";
import {provideEchartsConfig} from "./echarts.config";
import {provideServiceConfig} from "./service.config";
import {provideLuxonDateAdapter} from "@angular/material-luxon-adapter";
import {errorInterceptor} from "@open-event-workspace/core";
import {provideErrorConfig} from "./error.config";


registerLocaleData(localeDe, 'de-DE', localeDeExtra);

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(appRoutes),
    provideAnimationsAsync(),
    provideLuxonDateAdapter(),
    {provide: MAT_DATE_LOCALE, useValue: 'de-DE'},
    {provide: LOCALE_ID, useValue: 'de-DE'},
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'outline'}},
    provideToastConfig(),
    provideQuill(),
    provideEchartsConfig(),
    provideServiceConfig(),
    {provide: OverlayContainer, useClass: FullscreenOverlayContainer},
    provideKeycloakAngular(),
    provideZoneChangeDetection({eventCoalescing: true}),
    provideHttpClient(withInterceptors([
      includeBearerTokenInterceptor,
      errorInterceptor,
    ])),
    provideTranslateConfig(),
    provideShareButtonsOptions(shareIcons()),
    {
      provide: MatPaginatorIntl,
      useClass: MatPaginatorI18nService,
    },
    provideErrorConfig()
  ],
};
