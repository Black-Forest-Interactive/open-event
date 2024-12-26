import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CategoryBoardComponent} from "./category-board/category-board.component";

const routes: Routes = [
  {path: '', component: CategoryBoardComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CategoryRoutingModule {
}
