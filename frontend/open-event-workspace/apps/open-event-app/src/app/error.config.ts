import {BaseIssueService} from "@open-event-workspace/core";
import {IssueService} from "@open-event-workspace/app";
import {Provider} from "@angular/core";

export const ISSUE_SERVICE_PROVIDER: Provider = {
  provide: BaseIssueService,
  useClass: IssueService
}

export const provideErrorConfig = () => ISSUE_SERVICE_PROVIDER

