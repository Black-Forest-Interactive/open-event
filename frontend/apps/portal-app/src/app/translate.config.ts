import { provideTranslateHttpLoader } from '@ngx-translate/http-loader';
import { provideTranslateService } from '@ngx-translate/core';

export const provideTranslateConfig = () =>
  provideTranslateService({
    loader: provideTranslateHttpLoader({
      prefix: '/i18n/',
      suffix: '.json',
    }),
    fallbackLang: 'de',
    lang: 'en',
  });
