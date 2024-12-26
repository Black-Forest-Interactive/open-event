import {Component} from '@angular/core';
import {Location} from "@angular/common";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {EventService} from "../../event/model/event.service";
import {TranslateService} from "@ngx-translate/core";
import {HotToastService} from "@ngxpert/hot-toast";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-event-create',
    templateUrl: './event-create.component.html',
    styleUrls: ['./event-create.component.scss'],
    standalone: false
})
export class EventCreateComponent {

  reloading: boolean = false

  fg: FormGroup
  eventForm: FormGroup
  locationForm: FormGroup
  registrationForm: FormGroup
  ownerForm: FormGroup

  hiddenFields: string[] = ['shortText', 'iconUrl', 'imageUrl', 'endDate', 'interestedAllowed', 'ticketsEnabled']

  constructor(
    private fb: FormBuilder,
    private location: Location,
    private service: EventService,
    private translationService: TranslateService,
    private toastService: HotToastService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
    this.eventForm = this.fb.group({
      startTime: this.fb.control('', Validators.required),
      startDate: this.fb.control('', Validators.required),
      endTime: this.fb.control('', Validators.required),
      endDate: this.fb.control(''),

      imageUrl: this.fb.control(''),
      iconUrl: this.fb.control(''),
      longText: this.fb.control('', Validators.required),
      shortText: this.fb.control(''),
      title: this.fb.control('', Validators.required),
      tags: this.fb.array([]),
    })
    this.locationForm = this.fb.group({
      city: ['', Validators.required],
      country: ['Deutschland', Validators.required],
      street: ['', Validators.required],
      streetNumber: ['', Validators.required],
      zip: ['', Validators.required],
      additionalInfo: ['']
    })

    this.registrationForm = this.fb.group({
      maxGuestAmount: [4, Validators.required],
      interestedAllowed: [false, Validators.required],
      ticketsEnabled: [false, Validators.required],
      categories: [[]]
    })
    this.ownerForm = this.fb.group({
      owner: [undefined, Validators.required],
    })

    this.fg = this.fb.group({
      event: this.eventForm,
      location: this.locationForm,
      registration: this.registrationForm,
      owner: this.ownerForm
    })
  }


  back() {
    this.location.back()
  }

  submit() {
    if (!this.fg.valid) {
      this.translationService.get("EVENT.MESSAGE.ERROR.FORM_INVALID").subscribe(msg => this.toastService.error(msg))
      return
    }
    this.reloading = true

    let request = this.service.createRequest(this.fg.value, true)
    if (!request) return

    let ownerId = this.ownerForm.value.owner
    if (!ownerId) return

    this.service.createBackofficeEvent(ownerId, request).subscribe({
      next: event => {
        this.translationService.get("event.message.create.succeed").subscribe(
          msg => {
            this.toastService.success(msg)
            this.router.navigate(["/event/details/" + event.id]).then()
          }
        )
      },
      error: err => this.translationService.get("event.message.create.failed").subscribe(
        msg => this.toastService.error(msg)
      )
    })
  }

}
