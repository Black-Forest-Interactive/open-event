export interface Share {
  id: string,
  eventId: number,
  enabled: boolean,
  created: string,
  changed: string | null
}

export interface ShareInfo {
  share: Share,
  url: string,
}

export class ShareChangeRequest {
  constructor(
    public enabled: boolean,
  ) {
  }
}
