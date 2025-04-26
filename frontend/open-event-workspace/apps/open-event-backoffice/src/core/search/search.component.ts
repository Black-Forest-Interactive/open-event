import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {TranslatePipe} from "@ngx-translate/core";
import {Subject, switchMap, takeUntil, timer} from "rxjs";
import {tap} from "rxjs/operators";
import {SearchService} from "@open-event-workspace/backoffice";
import {HotToastService} from "@ngxpert/hot-toast";
import {SearchOperatorInfo} from "@open-event-workspace/core";
import {MatCard} from "@angular/material/card";
import {MatProgressBar} from "@angular/material/progress-bar";
import {BoardComponent} from "../../shared/board/board.component";

@Component({
  selector: 'boffice-search',
  imports: [CommonModule, MatIcon, TranslatePipe, MatCard, MatButton, MatProgressBar, BoardComponent],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss',
})
export class SearchComponent {

  reloading: boolean = false
  data: SearchOperatorInfo[] = []
  private unsub = new Subject<void>();

  constructor(
    private service: SearchService,
    private toast: HotToastService,
  ) {
  }

  ngOnInit(): void {
    timer(0, 500).pipe(
      tap((x) => console.log(x)),
      takeUntil(this.unsub),
      switchMap(async () => this.reload())
    ).subscribe();
  }

  ngOnDestroy(): void {
    this.unsub.next();
    this.unsub.complete();
  }

  reload() {
    if (this.reloading) return
    this.reloading = true

    this.service.getAllInfo().subscribe({
      next: value => this.handleData(value),
      error: err => this.handleError(err)
    })
  }

  private handleData(value: SearchOperatorInfo[]) {
    this.data = value
    this.reloading = false
  }

  private handleError(err: any) {
    if (err) this.toast.error(err)
    this.reloading = false
  }

  setup(info: SearchOperatorInfo) {
    this.service.setup(info.key).subscribe()
  }
}
