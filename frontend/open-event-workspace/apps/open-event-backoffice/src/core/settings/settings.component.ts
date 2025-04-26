import {Component, EventEmitter} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Setting} from "@open-event-workspace/core";
import {MatDialog} from "@angular/material/dialog";
import {MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {Page} from "@open-event-workspace/shared";
import {SettingsService} from "@open-event-workspace/backoffice";
import {SettingsChangeDialogComponent} from "./settings-change-dialog/settings-change-dialog.component";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {TranslatePipe} from "@ngx-translate/core";
import {MatTableModule} from "@angular/material/table";
import {MatButton, MatIconButton} from "@angular/material/button";
import {BoardComponent, BoardToolbarActions} from "../../shared/board/board.component";

@Component({
  selector: 'boffice-settings',
  imports: [
    CommonModule,
    TranslatePipe,
    MatToolbarModule,
    MatCardModule,
    MatIconModule,
    MatTableModule,
    MatPaginatorModule,
    MatButton,
    BoardComponent,
    BoardToolbarActions,
    MatIconButton,

  ],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.scss',
})
export class SettingsComponent {

  reloading: boolean = false
  pageNumber = 0
  pageSize = 10
  totalElements = 0

  data: Setting[] = []

  displayedColumns: string[] = ['id', 'value', 'type', 'cmd']

  keyUp: EventEmitter<string> = new EventEmitter<string>()


  constructor(
    private service: SettingsService,
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
