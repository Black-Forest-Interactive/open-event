import {Account} from "@open-event-workspace/core";

export interface Feedback {
  id: number,
  subject: string,
  description: string,
  topic: string,
  tags: string[],
  rating: number,

  account: Account,

  clientIp: string,
  userAgent: string,

  timestamp: string
}


export class FeedbackChangeRequest {
  constructor(
    public subject: string,
    public description: string,
    public topic: string,
    public tags: string[],
    public rating: number,
  ) {
  }
}
