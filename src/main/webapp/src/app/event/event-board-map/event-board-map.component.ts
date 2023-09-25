import {AfterViewInit, Component, ComponentFactoryResolver, Injector} from '@angular/core';
import {EventBoardService} from "../model/event-board.service";
import * as L from 'leaflet';
import {icon, layerGroup, Map, Marker} from 'leaflet';
import {Subscription} from "rxjs";
import {EventInfo} from "../model/event-api";
import {EventBoardMapPopupComponent} from "../event-board-map-popup/event-board-map-popup.component";
import {EventNavigationService} from "../event-navigation.service";
import {Router} from "@angular/router";

const iconRetinaUrl = 'assets/marker/marker-icon-2x.png';
const iconUrl = 'assets/marker/marker-icon.png';
const shadowUrl = 'assets/marker/marker-shadow.png';
const iconDefault = icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
});
Marker.prototype.options.icon = iconDefault;


@Component({
  selector: 'app-event-board-map',
  templateUrl: './event-board-map.component.html',
  styleUrls: ['./event-board-map.component.scss']
})
export class EventBoardMapComponent implements AfterViewInit {

  private map: Map | undefined
  private markerLayer = layerGroup()
  private subscription: Subscription | undefined

  constructor(
    public service: EventBoardService,
    private resolver: ComponentFactoryResolver,
    private injector: Injector,
    private router: Router,
  ) {
  }

  ngOnInit() {
    this.subscription = this.service.reloading.subscribe(_ => this.updateMarker())
  }

  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe()
      this.subscription = undefined
    }
  }

  ngAfterViewInit(): void {
    this.map = L.map('map', {
      center: [48.88436, 8.69892],
      zoom: 11
    })

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    })

    tiles.addTo(this.map)
    this.updateMarker()
  }

  private updateMarker() {
    if (!this.map) return

    if (this.map.hasLayer(this.markerLayer)) {
      this.markerLayer.clearLayers()
      this.map.removeLayer(this.markerLayer)
    }

    this.service.infos.forEach(i => this.addEventMarker(i))

    this.map.addLayer(this.markerLayer)
  }

  private addEventMarker(i: EventInfo) {
    let location = i.location
    if (!location) return

    let lat = location.lat
    let lon = location.lon
    if (lat == 0 && lon == 0) return

    let marker = L.marker([lat, lon]);
    let component = this.resolver
      .resolveComponentFactory(EventBoardMapPopupComponent)
      .create(this.injector);
    component.instance.data = i
    component.instance.close.asObservable().subscribe(res => {
        marker.closePopup()
        if (res) {
          EventNavigationService.navigateToEventDetails(this.router, i.event.id)
        }
      }
    )
    component.changeDetectorRef.detectChanges()

    marker.addTo(this.markerLayer).bindPopup(component.location.nativeElement);
  }
}
