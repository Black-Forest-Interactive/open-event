import {Observable} from "rxjs";
import {Page} from "@open-event-workspace/shared";

export interface Address {
  id: number,

  street: string,
  streetNumber: string,
  zip: string,
  city: string,
  country: string,
  additionalInfo: string,

  lat: number,
  lon: number,
}


export class AddressChangeRequest {
  constructor(
    public street: string,
    public streetNumber: string,
    public zip: string,
    public city: string,
    public country: string,
    public additionalInfo: string,
    public lat: number,
    public lon: number,
  ) {
  }
}

export interface AddressReadAPI {
  getAllAddresses(page: number, size: number): Observable<Page<Address>>

  getAddress(id: number): Observable<Address>
}
