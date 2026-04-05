import { AfterViewInit, Component, ComponentFactoryResolver, effect, inject, Injector } from '@angular/core'
import * as L from 'leaflet'
import { icon, layerGroup, Map, Marker, MarkerClusterGroup } from 'leaflet'
import { EventBoardMapPopupComponent } from '../event-board-map-popup/event-board-map-popup.component'
import { EventNavigationService } from '../event-navigation.service'
import { Router } from '@angular/router'
import { EventBoardService } from '../event-board.service'
import { EventSearchEntry } from '@open-event/core'
import { MatCard } from '@angular/material/card'
import 'leaflet.markercluster'
import { LoadingBarComponent } from '@open-event/shared'

const iconRetinaUrl = 'marker/marker-icon-2x.png'
const iconUrl = 'marker/marker-icon.png'
const shadowUrl = 'marker/marker-shadow.png'
const iconDefault = icon({
  iconRetinaUrl,
  iconUrl,
  shadowUrl,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  tooltipAnchor: [16, -28],
  shadowSize: [41, 41]
})
Marker.prototype.options.icon = iconDefault

@Component({
  selector: 'portal-event-board-map',
  templateUrl: './event-board-map.component.html',
  styleUrl: './event-board-map.component.scss',
  imports: [MatCard, LoadingBarComponent],
  standalone: true
})
export class EventBoardMapComponent implements AfterViewInit {
  private service = inject(EventBoardService)
  private resolver = inject(ComponentFactoryResolver)
  private injector = inject(Injector)
  private router = inject(Router)

  readonly reloading = this.service.reloading

  private map: Map | undefined
  private markerLayer = layerGroup()

  constructor() {
    effect(() => {
      const entries = this.service.entries()
      if (this.map) this.updateMarker(entries)
    })
  }

  ngAfterViewInit(): void {
    this.map = L.map('map', { center: [48.88436, 8.69892], zoom: 11 })
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(this.map)
    this.updateMarker(this.service.entries())
  }

  private updateMarker(entries: EventSearchEntry[]) {
    if (!this.map) return

    if (this.map.hasLayer(this.markerLayer)) {
      this.markerLayer.clearLayers()
      this.map.removeLayer(this.markerLayer)
    }

    const group = L.markerClusterGroup()
    entries.filter(e => this.isValid(e)).forEach(e => this.addGroupEventMarker(group, e))
    this.map.addLayer(group)
  }

  private isValid(e: EventSearchEntry): boolean {
    return e.hasLocation && e.lat !== 0 && e.lon !== 0
  }

  private addGroupEventMarker(g: MarkerClusterGroup, e: EventSearchEntry) {
    const marker = this.createEventMarker(e)
    const component = this.resolver.resolveComponentFactory(EventBoardMapPopupComponent).create(this.injector)
    component.instance.data.set(e)
    component.changeDetectorRef.detectChanges()
    marker.bindPopup(component.location.nativeElement)
    component.instance.close.asObservable().subscribe(res => {
      marker.closePopup()
      if (res) EventNavigationService.navigateToEventDetails(this.router, +e.id)
    })
    g.addLayer(marker)
  }

  private createEventMarker(i: EventSearchEntry): Marker {
    return L.marker([i.lat, i.lon])
  }
}
