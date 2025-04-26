import {Component, Input} from '@angular/core';
import {HttpResponse} from "@angular/common/http";
import FileSaver from "file-saver";
import {AuthService} from "@open-event-workspace/shared";
import {Event, ExportService} from "@open-event-workspace/core";
import {MatIcon} from "@angular/material/icon";
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import {MatMiniFabButton} from "@angular/material/button";
import {Roles} from "../../../shared/roles";

@Component({
  selector: 'app-event-action-export',
  templateUrl: './event-action-export.component.html',
  styleUrls: ['./event-action-export.component.scss'],
  imports: [
    MatIcon,
    MatProgressSpinner,
    MatMiniFabButton
  ],
  standalone: true
})
export class EventActionExportComponent {
  data: Event | undefined
  accessible: boolean = false
  exporting: boolean = false

  constructor(
    private authService: AuthService,
    private exportService: ExportService,
  ) {
  }

  @Input()
  set event(value: Event | undefined) {
    this.data = value
  }

  ngOnInit() {
    this.accessible = this.authService.hasRole(Roles.PERMISSION_EXPORT)
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
