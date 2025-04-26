import {provideQuillConfig} from "ngx-quill";

export const provideQuill = () =>
  provideQuillConfig({
    modules: {
      syntax: false,
      toolbar: false,
    }
  })
