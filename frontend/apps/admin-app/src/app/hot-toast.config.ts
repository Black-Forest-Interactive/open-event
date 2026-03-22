import {provideHotToastConfig} from "@ngxpert/hot-toast";


export const provideToastConfig = () =>
  provideHotToastConfig({
    autoClose: true,
    position: 'top-center'
  })
