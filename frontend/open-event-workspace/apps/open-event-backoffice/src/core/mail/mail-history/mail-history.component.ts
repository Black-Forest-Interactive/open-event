import {Component, EventEmitter} from '@angular/core';
import {DatePipe, Location} from "@angular/common";
import {LoadingBarComponent, Page} from "@open-event-workspace/shared";
import {MatCard} from "@angular/material/card";
import {MatCell, MatCellDef, MatColumnDef, MatHeaderCell, MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable} from "@angular/material/table";
import {MatIcon} from "@angular/material/icon";
import {MatMiniFabButton} from "@angular/material/button";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatToolbar} from "@angular/material/toolbar";
import {TranslatePipe} from "@ngx-translate/core";
import {MailJobHistoryEntry} from "@open-event-workspace/core";
import {HotToastService} from "@ngxpert/hot-toast";
import {ActivatedRoute, Router} from "@angular/router";
import {debounceTime, distinctUntilChanged} from "rxjs";
import {MailService} from "@open-event-workspace/backoffice";

@Component({
  selector: 'boffice-mail-history',
  imports: [
    DatePipe,
    LoadingBarComponent,
    MatCard,
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatMiniFabButton,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    MatToolbar,
    TranslatePipe
  ],
  templateUrl: './mail-history.component.html',
  styleUrl: './mail-history.component.scss'
})
export class MailHistoryComponent {
  reloading: boolean = false
  pageNumber = 0
  pageSize = 25
  totalElements = 0

  data: MailJobHistoryEntry[] = []

  displayedColumns: string[] = ['timestamp', 'message']

  keyUp: EventEmitter<string> = new EventEmitter<string>()

  jobId: number | undefined

  constructor(
    private service: MailService,
    private toastService: HotToastService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location
  ) {
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')
    if (id != null) {
      this.jobId = +id
      this.loadPage(0)
    }

    this.keyUp.pipe(
      debounceTime(500),
      distinctUntilChanged()
    ).subscribe(data => this.search(data))
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.loadPage(event.pageIndex)
  }

  back() {
    this.location.back()
  }

  private loadPage(number: number) {
    if (!this.jobId) return
    if (this.reloading) return
    this.reloading = true

    this.service.getJobHistory(this.jobId, number, this.pageSize).subscribe(p => this.handleData(p))
  }

  private handleData(page: Page<MailJobHistoryEntry>) {
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

  private search(data: string) {
    this.toastService.error("Sorry searching '" + data + "' is not supported yet")
  }
}
