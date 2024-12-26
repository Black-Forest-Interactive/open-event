import {Component} from '@angular/core';
import {ExportService} from "../model/export.service";
import {HttpResponse} from "@angular/common/http";
import FileSaver from "file-saver";
import {HotToastService} from "@ngxpert/hot-toast";
import {TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'app-board-card-export',
    templateUrl: './board-card-export.component.html',
    styleUrls: ['./board-card-export.component.scss'],
    standalone: false
})
export class BoardCardExportComponent {
  exporting: boolean = false
  summarizing: boolean = false

  constructor(
    private exportService: ExportService,
    private translateService: TranslateService,
    private toastService: HotToastService
  ) {
  }

  export() {
    if (this.exporting) return
    this.exporting = true
    this.exportService.exportEvents().subscribe(r => this.handleExportResponse(r))
  }

  exportMail() {
    this.exportService.exportEventsToEmail().subscribe(
      {
        next: _ => this.translateService.get('backoffice.export.action.mail.success').subscribe(text => this.toastService.success(text)),
        error: err => this.translateService.get('backoffice.export.action.mail.error').subscribe(text => this.toastService.error(text)),
      }
    )
  }

  exportSummary() {
    if (this.summarizing) return
    this.summarizing = true
    this.exportService.exportSummary().subscribe(r => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    let contentDispositionHeader = response.headers.get("content-disposition")
    if (contentDispositionHeader) {
      let fileName = contentDispositionHeader.split(';')[1].trim().split('=')[1].replace(/"/g, '')
      let content = response.body
      if (content) FileSaver.saveAs(content, fileName)
    }
    this.exporting = false
    this.summarizing = false
  }
}
