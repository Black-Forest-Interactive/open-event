export interface UploadProgress {
  progress: number;
  uploading: boolean;
  error?: string;
  success?: boolean;
}

export interface UploadResponse {
  success: boolean;
  imageUrl?: string;
  message?: string;
  error?: string;
}
