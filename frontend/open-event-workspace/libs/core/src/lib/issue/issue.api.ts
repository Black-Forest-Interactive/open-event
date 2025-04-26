import {Account} from "../account/account.api";
import {Observable} from "rxjs";
import {BaseService} from "@open-event-workspace/shared";

export interface Issue {
  id: number,
  subject: string,
  description: string,

  error: string,
  url: string,


  account: Account,
  status: string,

  clientIp: string,
  userAgent: string,

  timestamp: string
}

export class IssueChangeRequest {
  public constructor(
    public subject: string,
    public description: string,
    public error: string,
    public url: string
  ) {
  }
}

export abstract class BaseIssueService  extends BaseService {
  abstract createIssue(request: IssueChangeRequest): Observable<any>
}
