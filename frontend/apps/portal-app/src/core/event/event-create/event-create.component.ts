import { Component, inject } from '@angular/core'
import { Router } from '@angular/router'
import { EventCreateComponent as UiEventCreateComponent } from '@open-event/ui'
import { AddressService, AudienceService, CategoryService, EventService } from '@open-event/portal'
import { AddressChangeRequest } from '@open-event/core'

@Component({
  selector: 'portal-event-create',
  imports: [UiEventCreateComponent],
  templateUrl: './event-create.component.html',
  styleUrl: './event-create.component.scss'
})
export class EventCreateComponent {
  private service = inject(EventService)
  private addressService = inject(AddressService)
  private categoryService = inject(CategoryService)
  private audienceService = inject(AudienceService)
  private router = inject(Router)

  addressReadAPI = {
    getAllAddresses: (page: number, size: number) => this.addressService.getAddresses(page, size),
    getAddress: (id: number) => this.addressService.getAddress(id),
    createAddress: (request: AddressChangeRequest) => this.addressService.createAddress(request)
  }

  categoryReadAPI = {
    getAllCategories: (page: number, size: number) => this.categoryService.getCategories(page, size),
    getCategory: (id: number) => this.categoryService.getCategory(id)
  }

  audienceReadAPI = {
    getAllAudiences: (page: number, size: number) => this.audienceService.getAudiences(page, size),
    getAudience: (id: number) => this.audienceService.getAudience(id)
  }

  eventReadAPI = {
    getEvent: (id: number) => this.service.getEvent(id),
    getEventInfo: (id: number) => this.service.getEventInfo(id),
    create: (request: any) => this.service.create(request)
  }

  navigateToEvent(eventId: number) {
    this.router.navigate(['/event/details/' + eventId]).then()
  }
}
