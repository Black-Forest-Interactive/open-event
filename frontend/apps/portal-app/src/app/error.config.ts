import { BaseIssueService } from "@open-event/core";
import { IssueService } from "@open-event/app";
import { Provider } from "@angular/core";

export const ISSUE_SERVICE_PROVIDER: Provider = {
  provide: BaseIssueService,
  useClass: IssueService,
};

export const provideErrorConfig = () => ISSUE_SERVICE_PROVIDER;
