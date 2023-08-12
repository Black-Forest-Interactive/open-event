export interface Setting {
  id: number,
  key: string,
  value: string,
  type: string
}

export class SettingChangeRequest {
  constructor(
    public key: string,
    public value: any,
    public type: string
  ) {
  }
}
export interface UrlResponse {
  url: string
}

export interface TextResponse {
  text: string
}

export const VALUE_TYPE_URL = 'URL'
export const VALUE_TYPE_EMAIL = 'EMAIL'
export const VALUE_TYPE_BOOLEAN = 'BOOLEAN'
export const VALUE_TYPE_STRING = 'STRING'
export const VALUE_TYPE_NUMBER = 'NUMBER'

export const VALUE_TYPES = [
  VALUE_TYPE_URL,
  VALUE_TYPE_EMAIL,
  VALUE_TYPE_BOOLEAN,
  VALUE_TYPE_STRING,
  VALUE_TYPE_NUMBER
]
