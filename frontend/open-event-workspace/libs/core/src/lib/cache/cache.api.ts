export interface CacheInfo {
  key: string,
  name: string,
  hitCount: number,
  missCount: number,
  loadSuccessCount: number,
  loadFailureCount: number,
  totalLoadTime: number,
  evictionCount: number,
  evictionWeight: number,
}
