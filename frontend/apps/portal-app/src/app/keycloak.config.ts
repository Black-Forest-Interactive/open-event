import {createInterceptorCondition, INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG, IncludeBearerTokenCondition, provideKeycloak} from 'keycloak-angular';
import {environment} from "../environments/environment";

const localhostCondition = createInterceptorCondition<IncludeBearerTokenCondition>({
  urlPattern:  /^.*$/,
  bearerPrefix: 'Bearer'
});

export const provideKeycloakAngular = () =>
  provideKeycloak({
    config: environment.keycloak,
    initOptions: {
      onLoad: 'login-required',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html'
    },
    features: [
      // withAutoRefreshToken({
      //   onInactivityTimeout: 'logout',
      //   sessionTimeout: 60000
      // })
    ],
    providers: [
      // AutoRefreshTokenService,
      // UserActivityService,
      {
        provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
        useValue: [localhostCondition]
      }
    ]
  });
