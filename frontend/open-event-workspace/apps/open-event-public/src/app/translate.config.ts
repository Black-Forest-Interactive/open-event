import {HttpClient} from "@angular/common/http";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {importProvidersFrom} from "@angular/core";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";

const httpLoaderFactory: (http: HttpClient) => TranslateHttpLoader = (http: HttpClient) =>
  new TranslateHttpLoader(http, './i18n/', '.json')


export const provideTranslateConfig = () =>
  importProvidersFrom([TranslateModule.forRoot({
    loader: {
      provide: TranslateLoader,
      useFactory: httpLoaderFactory,
      deps: [HttpClient],
    },
  })])
