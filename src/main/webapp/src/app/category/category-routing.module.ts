import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CategoryBoardComponent} from "./category-board/category-board.component";
import {CategoryChangeComponent} from "./category-change/category-change.component";

const routes: Routes = [
  {path: '', component: CategoryBoardComponent},
  {path: 'create', component: CategoryChangeComponent},
  {path: 'edit/:id', component: CategoryChangeComponent},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CategoryRoutingModule {
}
