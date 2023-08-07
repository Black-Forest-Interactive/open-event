import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {retry} from "rxjs/operators";
import {Page} from "./page";


export abstract class BaseService {
  api = 'api/';
  protected retryCount: number = 3;


  protected constructor(private http: HttpClient, private urlPrefix: string) {
  }

  protected getAll<T>(suffix: string = ''): Observable<Array<T>> {
    const url = this.createUrl(suffix);
    console.debug("Get all '" + url + "'")
    return this.http.get<T[]>(url).pipe(retry(this.retryCount));
  }

  protected get<T>(suffix: string, params = new HttpParams()): Observable<T> {
    const url = this.createUrl(suffix);
    console.debug("Get '" + url + "'")
    return this.http.get<T>(url, {params: params}).pipe(retry(this.retryCount));
  }

  protected getPaged<T>(suffix: string, page: number, size: number, params = new HttpParams(), queryParams: string = ''): Observable<Page<T>> {
    const url = this.createUrl(suffix);
    console.debug("Get paged '" + url + "'")
    const uri = url + "?page=" + page.toString() + "&size=" + size.toString() + queryParams
    return this.http.get<Page<T>>(uri).pipe(retry(this.retryCount))
  }


  protected postPaged<T>(suffix: string, body: any, page: number, size: number, params = new HttpParams()): Observable<Page<T>> {
    const url = this.createUrl(suffix);
    console.debug("Post paged '" + url + "'")
    const uri = url + "?page=" + page.toString() + "&size=" + size.toString();
    return this.http.post<Page<T>>(uri, body)
  }

  protected getBlob(suffix: string): Observable<HttpResponse<Blob>> {
    const url = this.createUrl(suffix);
    console.debug("Get blob '" + url + "'")
    const headers = new HttpHeaders().set("Accept", "application/octet-stream");

    // @ts-ignore
    return this.http.get<Blob>(url, {
      headers: headers,
      observe: 'response',
      // @ts-ignore
      responseType: 'blob'
    })
  }

  protected put<T>(suffix: string, body: any): Observable<T> {
    const url = this.createUrl(suffix);
    console.debug("Put '" + url + "'")
    return this.http.put<T>(url, body).pipe(retry(this.retryCount));
  }

  protected post<T>(suffix: string, body: any): Observable<T> {
    const url = this.createUrl(suffix);
    console.debug("Post '" + url + "'")
    return this.http.post<T>(url, body);
  }

  protected patch<T>(suffix: string, body: any): Observable<T> {
    const url = this.createUrl(suffix);
    console.debug("Patch '" + url + "'")
    return this.http.patch<T>(url, body).pipe(retry(this.retryCount));
  }

  protected delete<T>(suffix: string): Observable<T> {
    const url = this.createUrl(suffix);
    console.debug("Delete '" + url + "'")
    return this.http.delete<T>(url,)
  }


  private createUrl(suffix: string): string {
    if (suffix.length === 0) {
      return (this.urlPrefix.length === 0) ? `${this.api}` : `${this.api}${this.urlPrefix}`
    } else {
      return (this.urlPrefix.length === 0) ? `${this.api}${suffix}` : `${this.api}${this.urlPrefix}/${suffix}`;
    }
  }

}
