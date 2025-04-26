import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Address, AddressChangeRequest} from "@open-event-workspace/core";
import {Observable} from "rxjs";
import {BaseService, Page} from "@open-event-workspace/shared";

@Injectable({
  providedIn: 'root'
})
export class AddressService extends BaseService {

  constructor(http: HttpClient) {
    super(http, 'app/address')
    this.retryCount = 1
  }

  getAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.getPaged('', page, size)
  }

  getAddress(id: number): Observable<Address> {
    return this.get('' + id)
  }

  importAddress(): Observable<Page<Address>> {
    return this.post('import', {})
  }

  createAddress(request: AddressChangeRequest): Observable<Address> {
    return this.post('', request)
  }

  updateAddress(id: number, request: AddressChangeRequest): Observable<Address> {
    return this.put('' + id, request)
  }


  deleteAddress(id: number): Observable<Address> {
    return this.delete('' + id)
  }

}
