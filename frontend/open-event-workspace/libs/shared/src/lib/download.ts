import { HttpResponse } from '@angular/common/http'
import FileSaver from 'file-saver'

export function download(response: HttpResponse<Blob>) {
  const contentDispositionHeader = response.headers.get('content-disposition')
  if (contentDispositionHeader) {
    const fileName = contentDispositionHeader.split(';')[1].trim().split('=')[1].replace(/"/g, '')
    const content = response.body
    if (content) FileSaver.saveAs(content, fileName)
  }
}
