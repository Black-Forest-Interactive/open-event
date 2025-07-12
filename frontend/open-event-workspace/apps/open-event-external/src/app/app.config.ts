import {ApplicationConfig, LOCALE_ID, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';
import {appRoutes} from './app.routes';
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import {provideLuxonDateAdapter} from "@angular/material-luxon-adapter";
import {MAT_DATE_LOCALE} from "@angular/material/core";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";
import {provideToastConfig} from "../../../open-event-app/src/app/hot-toast.config";
import {FullscreenOverlayContainer, OverlayContainer} from "@angular/cdk/overlay";
import {provideHttpClient} from "@angular/common/http";
import {provideShareButtonsOptions} from "ngx-sharebuttons";
import {shareIcons} from "ngx-sharebuttons/icons";
import {MatPaginatorIntl} from "@angular/material/paginator";
import {MatPaginatorI18nService} from "@open-event-workspace/shared";
import {provideTranslateConfig} from "./translate.config";
import {provideServiceConfig} from './service.config';
import {provideQuill} from "./quill.config";
import {registerLocaleData} from "@angular/common";
import localeDe from '@angular/common/locales/de';
import localeDeExtra from '@angular/common/locales/extra/de';

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
    provideServiceConfig(),
    {provide: OverlayContainer, useClass: FullscreenOverlayContainer},
    provideZoneChangeDetection({eventCoalescing: true}),
    provideHttpClient(),
    provideTranslateConfig(),
    provideShareButtonsOptions(shareIcons()),
    {
      provide: MatPaginatorIntl,
      useClass: MatPaginatorI18nService,
    },
  ],
};
