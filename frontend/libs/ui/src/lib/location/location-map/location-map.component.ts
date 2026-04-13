import { Component, effect, ElementRef, input, viewChild } from '@angular/core'
import { Location } from '@open-event/core'
import * as L from 'leaflet'
import { icon, Map, Marker, Zoom } from 'leaflet'

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
  selector: 'lib-location-map',
  imports: [],
  templateUrl: './location-map.component.html',
  styleUrl: './location-map.component.scss'
})
export class LocationMapComponent {
  location = input<Location>()
  scrollWheelZoom = input<Zoom>('center')

  private mapRef = viewChild<ElementRef<HTMLDivElement>>('map')
  private map: Map | undefined
  private marker: Marker | undefined

  constructor() {
    effect(() => {
      const container = this.mapRef()
      if (container && !this.map) {
        this.map = L.map(container.nativeElement, {
          center: [51.1657, 10.4515],
          zoom: 6,
          scrollWheelZoom: this.scrollWheelZoom(),
          zoomControl: false
        })
        L.control.zoom({ position: 'bottomright' }).addTo(this.map)
        L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
          attribution: '&copy; OpenStreetMap &copy; CARTO',
          subdomains: 'abcd',
          maxZoom: 19
        }).addTo(this.map)
      }
      this.updateMarker()
    })
  }

  private updateMarker() {
    const location = this.location()
    if (!this.map || !location) return
    if (this.marker) this.marker.remove()
    if (location.lat === 0 && location.lon === 0) return
    this.marker = L.marker([location.lat, location.lon]).addTo(this.map)
    this.map.setView([location.lat, location.lon], 13)
  }
}
