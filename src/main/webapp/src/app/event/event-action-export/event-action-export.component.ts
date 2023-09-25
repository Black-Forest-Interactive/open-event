import {Component, Input} from '@angular/core';
import {Event} from "../model/event-api";
import {AuthService} from "../../auth/auth.service";
import {ExportService} from "../../backoffice/model/export.service";
import {HttpResponse} from "@angular/common/http";
import FileSaver from "file-saver";

@Component({
  selector: 'app-event-action-export',
  templateUrl: './event-action-export.component.html',
  styleUrls: ['./event-action-export.component.scss']
})
export class EventActionExportComponent {
  @Input()
  set event(value: Event | undefined) {
    this.data = value
  }

  data: Event | undefined
  accessible: boolean = false
  exporting: boolean = false

  constructor(
    private authService: AuthService,
    private exportService: ExportService,
  ) {
  }

  ngOnInit() {
    this.accessible = this.authService.hasRole(AuthService.PERMISSION_EXPORT)
  }


  export() {
    if (!this.data) return

    if (this.exporting) return
    this.exporting = true
    this.exportService.exportEvent(this.data.id).subscribe(r => this.handleExportResponse(r))
  }

  private handleExportResponse(response: HttpResponse<Blob>) {
    let contentDispositionHeader = response.headers.get("content-disposition")
    if (contentDispositionHeader) {
      let fileName = contentDispositionHeader.split(';')[1].trim().split('=')[1].replace(/"/g, '')
      let content = response.body
      if (content) FileSaver.saveAs(content, fileName)
    }
    this.exporting = false
  }
}
