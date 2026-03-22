import {
  AutoRefreshTokenService,
  createInterceptorCondition,
  INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
  IncludeBearerTokenCondition,
  provideKeycloak,
  UserActivityService,
  withAutoRefreshToken
} from 'keycloak-angular'
import { environment } from '../environments/environment'

const localhostCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern: /^.*$/,
  bearerPrefix: 'Bearer'
})

export const provideKeycloakAngular = () =>
  provideKeycloak({
    config: environment.keycloak,
    initOptions: {
      onLoad: 'login-required',
      checkLoginIframe: false
    },
    features: [
      withAutoRefreshToken({
        onInactivityTimeout: 'login',
        sessionTimeout: 60000
      })
    ],
    providers: [
      AutoRefreshTokenService,
      UserActivityService,
      {
        provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
        useValue: [localhostCondition]
      }
    ]
  })
