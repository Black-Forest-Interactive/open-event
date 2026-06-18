import { Component, computed, createComponent, effect, ElementRef, EnvironmentInjector, inject, viewChild } from '@angular/core'
import * as L from 'leaflet'
import { icon, Map as LeafletMap, Marker, MarkerClusterGroup } from 'leaflet'
import { EventBoardMapPopupComponent } from '../event-board-map-popup/event-board-map-popup.component'
import { EventNavigationService } from '../event-navigation.service'
import { Router } from '@angular/router'
import { EventBoardService } from '../event-board.service'
import { EventSearchEntry } from '@open-event/core'
import { MatCard } from '@angular/material/card'
import { MatIcon } from '@angular/material/icon'
import { DatePipe } from '@angular/common'
import { TranslatePipe } from '@ngx-translate/core'
import 'leaflet.markercluster'
import { LoadingBarComponent } from '@open-event/shared'

interface VenueGroup {
  key: string
  street: string
  streetNumber: string
  zip: string
  city: string
  lat: number
  lon: number
  entries: EventSearchEntry[]
}

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
  imports: [MatCard, MatIcon, DatePipe, TranslatePipe, LoadingBarComponent],
  standalone: true
})
export class EventBoardMapComponent {
  private service = inject(EventBoardService)
  readonly reloading = this.service.reloading
  private environmentInjector = inject(EnvironmentInjector)
  private router = inject(Router)
  private mapContainerRef = viewChild<ElementRef<HTMLDivElement>>('map')
  private map: LeafletMap | undefined

  readonly venues = computed(() => {
    const groups = new Map<string, VenueGroup>()
    for (const e of this.service.entries()) {
      if (!e.hasLocation || (e.lat === 0 && e.lon === 0)) continue
      const key = `${e.street} ${e.streetNumber}|${e.zip} ${e.city}`
      const group = groups.get(key)
      if (group) group.entries.push(e)
      else groups.set(key, { key, street: e.street, streetNumber: e.streetNumber, zip: e.zip, city: e.city, lat: e.lat, lon: e.lon, entries: [e] })
    }
    return Array.from(groups.values())
  })

  constructor() {
    effect(() => {
      const container = this.mapContainerRef()
      if (container && !this.map) {
        this.map = L.map(container.nativeElement, { center: [51.1657, 10.4515], zoom: 6, zoomControl: false })
        L.control.zoom({ position: 'bottomright' }).addTo(this.map)
        L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
          attribution: '&copy; OpenStreetMap &copy; CARTO',
          subdomains: 'abcd',
          maxZoom: 19
        }).addTo(this.map)
        this.updateMarkers(this.service.entries())
      }
    })

    effect(() => {
      const entries = this.service.entries()
      if (this.map) this.updateMarkers(entries)
    })
  }

  private updateMarkers(entries: EventSearchEntry[]) {
    const map = this.map
    if (!map) return
    map.eachLayer((layer) => {
      if (layer instanceof L.MarkerClusterGroup) map.removeLayer(layer)
    })

    const group = L.markerClusterGroup({ showCoverageOnHover: false })
    entries.filter((e) => e.hasLocation && e.lat !== 0 && e.lon !== 0).forEach((e) => this.addMarker(group, e))
    map.addLayer(group)
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

  flyToVenue(venue: VenueGroup) {
    this.map?.flyTo([venue.lat, venue.lon], 15)
  }

  openEvent(entry: EventSearchEntry) {
    EventNavigationService.navigateToEventDetails(this.router, +entry.id)
  }
}
