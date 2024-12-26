import {Component, EventEmitter} from '@angular/core';
import {Page} from "../../shared/model/page";
import {MailJob} from "../model/mail-api";
import {PageEvent} from "@angular/material/paginator";
import {MailService} from "../model/mail.service";
import {Subject, switchMap, takeUntil, timer} from "rxjs";
import {tap} from "rxjs/operators";

@Component({
    selector: 'app-mail-board',
    templateUrl: './mail-board.component.html',
    styleUrls: ['./mail-board.component.scss'],
    standalone: false
})
export class MailBoardComponent {

  reloading: boolean = false
  pageNumber = 0
  pageSize = 25
  totalElements = 0

  data: MailJob[] = []

  displayedColumns: string[] = ['timestamp', 'status', 'title', 'cmd']

  keyUp: EventEmitter<string> = new EventEmitter<string>()
  private unsub = new Subject<void>();

  constructor(
    private service: MailService
  ) {
  }

  ngOnInit(): void {
    timer(0, 15000).pipe(
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
    this.loadPage(this.pageNumber)
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.loadPage(event.pageIndex)
  }


  private loadPage(number: number) {
    if (this.reloading) return
    this.reloading = true

    this.service.getJobs(number, this.pageSize).subscribe(p => this.handleData(p))
  }

  private handleData(page: Page<MailJob>) {
    if (page == null) {
      this.data = [];
      this.pageNumber = 0;
      this.pageSize = 20;
      this.totalElements = 0;
    } else {
      this.data = page.content.filter(d => d != null);
      this.pageNumber = page.pageable.number;
      this.pageSize = page.pageable.size;
      this.totalElements = page.totalSize;
    }
    this.reloading = false;
  }
}
