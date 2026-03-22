import { Injectable } from '@angular/core'
import { BaseService, Page } from '@open-event/shared'
import { Observable } from 'rxjs'
import { Address, AddressChangeRequest } from '@open-event/core'

@Injectable({
  providedIn: 'root'
})
export class AddressService extends BaseService {
  constructor() {
    super('backoffice/address')
    this.retryCount = 0
  }

  getAllAddresses(page: number, size: number): Observable<Page<Address>> {
    return this.getPaged('', page, size)
  }

  getAddress(id: number): Observable<Address> {
    return this.get('' + id)
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

  importAddress(): Observable<Page<Address>> {
    return this.post('import', {})
  }
}
