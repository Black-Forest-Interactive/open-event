import {NGX_ECHARTS_CONFIG} from "ngx-echarts";
import {FactoryProvider} from "@angular/core";


export const ECHARTS_PROVIDER: FactoryProvider = {
  provide: NGX_ECHARTS_CONFIG,
  useFactory: () => ({
    echarts: () => import('echarts'),
  }),
};

export const provideEchartsConfig = () => ECHARTS_PROVIDER
