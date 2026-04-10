import { provideTranslateHttpLoader } from '@ngx-translate/http-loader'
import { provideTranslateService } from '@ngx-translate/core'

const supportedLangs = ['de', 'en']

export const provideTranslateConfig = () => {
  const urlLang = new URLSearchParams(window.location.search).get('lang') ?? ''
  const lang = supportedLangs.includes(urlLang) ? urlLang : 'de'
  return provideTranslateService({
    loader: provideTranslateHttpLoader({
      prefix: '/external/i18n/',
      suffix: '.json'
    }),
    fallbackLang: 'en',
    lang
  })
}
