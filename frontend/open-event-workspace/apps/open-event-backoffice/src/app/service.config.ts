import {Provider} from "@angular/core";
import {BASE_API_URL} from "@open-event-workspace/shared";


export const BASE_URL_PROVIDER: Provider = {
  provide: BASE_API_URL,
  useValue: 'api/',
}


export const provideServiceConfig = () => BASE_URL_PROVIDER
