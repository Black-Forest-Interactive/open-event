import { AfterViewInit, Component, createComponent, effect, ElementRef, EnvironmentInjector, inject, ViewChild } from '@angular/core'
import * as L from 'leaflet'
import { icon, Map, Marker, MarkerClusterGroup } from 'leaflet'
import { EventBoardMapPopupComponent } from '../event-board-map-popup/event-board-map-popup.component'
import { EventNavigationService } from '../event-navigation.service'
import { Router } from '@angular/router'
import { EventBoardService } from '../event-board.service'
import { EventSearchEntry } from '@open-event/core'
import { MatCard } from '@angular/material/card'
import 'leaflet.markercluster'
import { LoadingBarComponent } from '@open-event/shared'

const iconDefault = icon({
  iconRetinaUrl: 'marker/marker-icon-2x.png',
  iconUrl: 'marker/marker-icon.png',
  shadowUrl: 'marker/marker-shadow.png',
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
  private environmentInjector = inject(EnvironmentInjector)
  private router = inject(Router)

  @ViewChild('map') mapContainerRef!: ElementRef<HTMLDivElement>

  readonly reloading = this.service.reloading
  private map: Map | undefined

  constructor() {
    effect(() => {
      const entries = this.service.entries()
      if (this.map) this.updateMarkers(entries)
    })
  }

  ngAfterViewInit(): void {
    this.map = L.map(this.mapContainerRef.nativeElement, { center: [51.1657, 10.4515], zoom: 6, zoomControl: false })
    L.control.zoom({ position: 'bottomright' }).addTo(this.map)
    L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
      attribution: '&copy; OpenStreetMap &copy; CARTO',
      subdomains: 'abcd',
      maxZoom: 19
    }).addTo(this.map)
    this.updateMarkers(this.service.entries())
  }

  private updateMarkers(entries: EventSearchEntry[]) {
    if (!this.map) return
    this.map.eachLayer((layer) => {
      if (layer instanceof L.MarkerClusterGroup) this.map!.removeLayer(layer)
    })

    const group = L.markerClusterGroup({ showCoverageOnHover: false })
    entries.filter((e) => e.hasLocation && e.lat !== 0 && e.lon !== 0).forEach((e) => this.addMarker(group, e))
    this.map.addLayer(group)
  }

  private addMarker(group: MarkerClusterGroup, e: EventSearchEntry) {
    const marker = L.marker([e.lat, e.lon])
    const ref = createComponent(EventBoardMapPopupComponent, { environmentInjector: this.environmentInjector })
    ref.instance.data.set(e)
    ref.changeDetectorRef.detectChanges()
    marker.bindPopup(ref.location.nativeElement, { maxWidth: 320, minWidth: 220 })
    ref.instance.close.subscribe((navigate) => {
      marker.closePopup()
      if (navigate) EventNavigationService.navigateToEventDetails(this.router, +e.id)
    })
    group.addLayer(marker)
  }
}
