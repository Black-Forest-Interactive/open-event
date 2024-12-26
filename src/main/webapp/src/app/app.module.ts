import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {HttpClient, provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {DashboardModule} from "./dashboard/dashboard.module";
import {QuillModule} from "ngx-quill";
import {NgxEchartsModule} from "ngx-echarts";
import {MAT_DATE_LOCALE} from "@angular/material/core";
import {registerLocaleData} from "@angular/common";
import localeDe from '@angular/common/locales/de';
import localeDeExtra from '@angular/common/locales/extra/de';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";
import {AuthModule} from "./auth/auth.module";
import {FALLBACK, GravatarModule, RATING} from "ngx-gravatar";
import {FullCalendarModule} from "@fullcalendar/angular";
import {provideHotToastConfig} from "@ngxpert/hot-toast";
import {FullscreenOverlayContainer, OverlayContainer} from "@angular/cdk/overlay";
import {provideShareButtonsOptions} from "ngx-sharebuttons";
import {shareIcons} from "ngx-sharebuttons/icons";
import {MatPaginatorIntl} from "@angular/material/paginator";
import {MatPaginatorI18nService} from "./shared/mat-paginator-i18n.service";

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

registerLocaleData(localeDe, 'de-DE', localeDeExtra);

@NgModule({
  declarations: [
    AppComponent
  ],
  bootstrap: [AppComponent], imports: [BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    GravatarModule.forRoot({
      fallback: FALLBACK.mp,
      rating: RATING.x,
      backgroundColor: 'rgba(0, 0, 0, 0.1)',
      borderColor: 'rgba(0, 0, 0, 0.1)',
      hasBorder: true,
      preferGravatar: false
    }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    QuillModule.forRoot({
      modules: {
        syntax: false,
        toolbar: [
          ['bold', 'italic', 'underline', 'strike'],
          // ['blockquote', 'code-block'],
          // [{ 'list': 'ordered'}, { 'list': 'bullet' }],
          // [{ 'indent': '-1'}, { 'indent': '+1' }],
          [{'direction': 'rtl'}],
          [{'size': ['small', false, 'large', 'huge']}],
          [{'header': [1, 2, 3, 4, 5, 6, false]}],
          // [{ 'color': [] }, { 'background': [] }],
          // [{ 'font': [] }],
          [{'align': []}],
          ['clean'],
        ]
      }
    }),
    NgxEchartsModule.forRoot({
      echarts: () => import('echarts'),
    }),
    FullCalendarModule,
    // my modules
    DashboardModule,
    AuthModule], providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'de-DE'},
    {provide: LOCALE_ID, useValue: 'de-DE'},
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'outline'}},
    provideHotToastConfig({
      autoClose: true,
      position: 'top-center'
    }),
    {provide: OverlayContainer, useClass: FullscreenOverlayContainer},
    provideHttpClient(withInterceptorsFromDi()),
    provideShareButtonsOptions(shareIcons()),
    {
      provide: MatPaginatorIntl,
      useClass: MatPaginatorI18nService,
    },
  ]
})
export class AppModule {
}
