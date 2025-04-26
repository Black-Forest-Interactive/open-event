import {Routes} from "@angular/router";

export const routes: Routes = [
  {path: '', loadComponent: () => import('./issue.component').then(m => m.IssueComponent)},
  {path: 'details/:id', loadComponent: () => import('./issue-details/issue-details.component').then(m => m.IssueDetailsComponent)},
];
