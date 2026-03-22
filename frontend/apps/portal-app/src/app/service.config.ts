import {Provider} from "@angular/core";
import {BASE_API_URL} from "../../../../libs/shared/src/lib/base-service";


export const BASE_URL_PROVIDER: Provider = {
  provide: BASE_API_URL,
  useValue: 'api/',
}


export const provideServiceConfig = () => BASE_URL_PROVIDER
