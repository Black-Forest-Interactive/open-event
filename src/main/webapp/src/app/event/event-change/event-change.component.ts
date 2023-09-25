import {Component} from '@angular/core';
import {EventService} from "../model/event.service";
import {ActivatedRoute, ParamMap, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TranslateService} from "@ngx-translate/core";
import {HotToastService} from "@ngneat/hot-toast";
import {Location} from "@angular/common";
import {EventInfo,} from "../model/event-api";
import * as moment from "moment";
import {STEPPER_GLOBAL_OPTIONS, StepperOrientation} from "@angular/cdk/stepper";
import {BreakpointObserver} from "@angular/cdk/layout";
import {map, Observable} from "rxjs";

@Component({
  selector: 'app-event-change',
  templateUrl: './event-change.component.html',
  styleUrls: ['./event-change.component.scss'],
  providers: [{provide: STEPPER_GLOBAL_OPTIONS, useValue: {showError: true},},],
})
export class EventChangeComponent {

  title: string = "EVENT.MENU.Add"
  reloading: boolean = false

  fg: FormGroup
  eventForm: FormGroup
  locationForm: FormGroup
  registrationForm: FormGroup

  event: EventInfo | undefined
  hiddenFields: string[] = ['shortText', 'iconUrl', 'imageUrl', 'endDate', 'interestedAllowed', 'ticketsEnabled']

  helpVisible: boolean = false

  stepperOrientation: Observable<StepperOrientation>

  constructor(
    private fb: FormBuilder,
    private service: EventService,
    private translationService: TranslateService,
    private toastService: HotToastService,
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    breakpointObserver: BreakpointObserver
  ) {
    this.eventForm = this.fb.group({
      startTime: this.fb.control('', Validators.required),
      startDate: this.fb.control('', Validators.required),
      endTime: this.fb.control('', Validators.required),
      endDate: this.fb.control('', Validators.required),

      imageUrl: this.fb.control(''),
      iconUrl: this.fb.control(''),
      longText: this.fb.control('', Validators.required),
      shortText: this.fb.control(''),
      title: this.fb.control('', Validators.required)
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

    this.fg = this.fb.group({
      event: this.eventForm,
      location: this.locationForm,
      registration: this.registrationForm
    })

    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  }

  ngOnInit() {
    this.route.paramMap.subscribe(p => this.handleParams(p))
    let endDate = this.eventForm.get('endDate');
    if (endDate) endDate.validator = this.isEndHidden() ? null : Validators.required
  }

  private handleParams(p: ParamMap) {
    let idParam = p.get('id')
    let id = idParam !== null ? +idParam : null;

    if (id == null) {
      this.handleDataCreate()
      return
    }

    const url = this.route.snapshot.url;
    const cmd = url[url.length - 2].path

    switch (cmd) {
      case 'copy':
        this.loadData(id, (e) => this.handleDataCopy(e))
        break
      case 'edit':
        this.loadData(id, (e) => this.handleDataEdit(e))
        break
      default:
        this.handleDataCreate()
        break
    }
  }

  private handleDataCreate() {
    this.translationService.get("event.change.create").subscribe(text => this.title = text);
  }

  private loadData(id: number, callback: (e: EventInfo) => void) {
    this.reloading = true
    this.service.getEventInfo(id).subscribe(data => callback(data));
  }

  private handleDataCopy(e: EventInfo) {
    this.initValues(e)
    this.translationService.get("event.change.copy", {event: e.event.shortText}).subscribe(text => this.title = text);
    this.reloading = false
  }

  private handleDataEdit(e: EventInfo) {
    this.event = e
    this.initValues(e)
    this.translationService.get("event.change.update", {event: e.event.shortText}).subscribe(text => this.title = text);
    this.reloading = false
  }

  private initValues(e: EventInfo) {
    // init event form
    let start = moment(e.event.start)
    let startTime = start.format("HH:mm")
    let finish = moment(e.event.finish)
    let finishTime = finish.format("HH:mm")

    this.eventForm.setValue({
      startTime: startTime,
      startDate: start,
      endTime: finishTime,
      endDate: finish,

      imageUrl: e.event.imageUrl ?? "",
      iconUrl: e.event.iconUrl ?? "",
      longText: e.event.longText ?? "",
      shortText: e.event.shortText ?? "",
      title: e.event.title ?? "",
    })
    // init location form
    let location = e.location
    if (location) {
      this.locationForm.setValue({
        city: location.city ?? "",
        country: location.country ?? "",
        street: location.street ?? "",
        streetNumber: location.streetNumber ?? "",
        zip: location.zip ?? "",
        additionalInfo: location.additionalInfo ?? "",
      })
    }
    // init registration form
    let registration = e.registration
    if (registration) {
      this.registrationForm.setValue({
        ticketsEnabled: registration.registration.ticketsEnabled,
        maxGuestAmount: registration.registration.maxGuestAmount,
        interestedAllowed: registration.registration.interestedAllowed,
        categories: e.categories.map(c => c.id)
      })
    }
  }

  cancel() {
    this.location.back()
  }


  submit() {
    if (!this.fg.valid) {
      this.translationService.get("EVENT.MESSAGE.ERROR.FORM_INVALID").subscribe(msg => this.toastService.error(msg))
      return
    }
    this.reloading = true

    if (this.event) {
      this.update()
    } else {
      this.create()
    }
  }

  private update() {
    if (!this.event) return
    let request = this.service.createRequest(this.fg.value,this.isEndHidden() )
    if (!request) return
    this.service.updateEvent(this.event.event.id, request).subscribe({
      next: event => {
        this.translationService.get("event.message.update.succeed").subscribe(
          msg => {
            this.toastService.success(msg)
            this.router.navigate(["/event/details/" + event.id]).then()
          }
        )
      },
      error: err => this.translationService.get("event.message.update.failed").subscribe(
        msg => this.toastService.error(msg)
      )
    })
  }

  private create() {
    let request = this.service.createRequest(this.fg.value,this.isEndHidden() )
    if (!request) return
    this.service.createEvent(request).subscribe({
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

  private isEndHidden() {
    return this.hiddenFields.find(f => f === 'endDate') != null
  }

  toggleHelp(step: string) {
    this.helpVisible = !this.helpVisible
  }
}
