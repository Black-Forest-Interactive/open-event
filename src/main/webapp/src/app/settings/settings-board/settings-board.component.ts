import {Component, EventEmitter} from '@angular/core';
import {SettingService} from "../model/setting.service";
import {HotToastService} from "@ngxpert/hot-toast";
import {Setting} from "../model/settings-api";
import {Page} from "../../shared/model/page";
import {PageEvent} from "@angular/material/paginator";
import {MatDialog} from "@angular/material/dialog";
import {SettingsChangeDialogComponent} from "../settings-change-dialog/settings-change-dialog.component";

@Component({
    selector: 'app-settings-board',
    templateUrl: './settings-board.component.html',
    styleUrls: ['./settings-board.component.scss'],
    standalone: false
})
export class SettingsBoardComponent {
  reloading: boolean = false
  pageNumber = 0
  pageSize = 10
  totalElements = 0

  data: Setting[] = []

  displayedColumns: string[] = ['id', 'value', 'type', 'cmd']

  keyUp: EventEmitter<string> = new EventEmitter<string>()


  constructor(
    private service: SettingService,
    private toastService: HotToastService,
    private dialog: MatDialog,
  ) {
  }

  ngOnInit(): void {
    this.reload()
  }

  handlePageChange(event: PageEvent) {
    if (this.reloading) return
    this.pageSize = event.pageSize
    this.loadPage(event.pageIndex)
  }

  private reload() {
    this.loadPage(0)
  }

  private loadPage(number: number) {
    if (this.reloading) return
    this.reloading = true

    this.service.getAllSetting(number, this.pageSize).subscribe(p => this.handleData(p))
  }

  private handleData(p: Page<Setting>) {
    this.data = p.content

    this.totalElements = p.totalSize
    this.pageNumber = p.pageable.number
    this.pageSize = p.pageable.size
    this.reloading = false
  }


  create() {
    const dialogRef = this.dialog.open(SettingsChangeDialogComponent, {
      width: '350px',
      data: null
    })
    dialogRef.afterClosed().subscribe(d => this.reload())
  }

  edit(entry: Setting) {
    const dialogRef = this.dialog.open(SettingsChangeDialogComponent, {
      width: '350px',
      data: entry
    })
    dialogRef.afterClosed().subscribe(d => this.reload())
  }

}
