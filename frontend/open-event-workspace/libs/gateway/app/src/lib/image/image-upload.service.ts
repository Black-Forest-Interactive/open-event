import {HttpClient, HttpEventType, HttpRequest} from "@angular/common/http";
import {finalize, map, Observable} from "rxjs";
import {BaseService} from "@open-event-workspace/shared";
import {computed, Injectable, signal} from "@angular/core";
import {UploadProgress, UploadResponse} from "@open-event-workspace/core";

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/image')
    this.retryCount = 1
  }

  private uploadProgressSignal = signal<UploadProgress>({
    progress: 0,
    uploading: false
  })

  uploadProgress = this.uploadProgressSignal.asReadonly()
  isUploading = computed(() => this.uploadProgressSignal().uploading)
  uploadPercentage = computed(() => this.uploadProgressSignal().progress)

  getBannerImageUrl(eventId: number): string {
    return this.createUrl('event/' + eventId + '/banner')
  }

  getBannerImage(eventId: number): Observable<any> {
    return this.getBlob('event/' + eventId + '/banner')
  }

  uploadBannerImage(eventId: number, file: File): Observable<UploadResponse> {
    this.uploadProgressSignal.set({progress: 0, uploading: true})

    const formData = new FormData()
    formData.append('image', file)

    const url = this.createUrl('event/' + eventId + '/banner')
    const request = new HttpRequest('POST', url, formData, {
      reportProgress: true,
      responseType: 'json'
    });

    return this.request<FormData, UploadResponse>(request).pipe(
      map(event => {
        switch (event.type) {
          case HttpEventType.UploadProgress:
            if (event.total) {
              const progress = Math.round(100 * event.loaded / event.total)
              this.uploadProgressSignal.update(current => ({
                ...current,
                progress,
                uploading: true
              }))
            }
            return {success: false}; // Continue uploading

          case HttpEventType.Response:
            this.uploadProgressSignal.update(current => ({
              ...current,
              progress: 100,
              uploading: false,
              success: true
            }))
            return event.body as UploadResponse

          default:
            return {success: false};
        }
      }),
      finalize(() => {
        // Reset progress after upload completes
        setTimeout(() => {
          this.uploadProgressSignal.set({progress: 0, uploading: false});
        }, 1000)
      })
    );
  }

  // Validate image file
  validateImage(file: File): { valid: boolean; error?: string } {
    const maxSize = 5 * 1024 * 1024; // 5MB
    const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];

    if (!allowedTypes.includes(file.type)) {
      return {
        valid: false,
        error: 'Invalid file type. Please upload JPEG, PNG, GIF, or WebP images.'
      };
    }

    if (file.size > maxSize) {
      return {
        valid: false,
        error: 'File size too large. Maximum size is 5MB.'
      };
    }

    return {valid: true};
  }
}
