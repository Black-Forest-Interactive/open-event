import {Component, effect, input} from '@angular/core';
import {Location, SharedLocation} from "@open-event-workspace/core";
import * as L from "leaflet";
import {icon, Map, Marker} from "leaflet";

const iconRetinaUrl = 'marker/marker-icon-2x.png';
const iconUrl = 'marker/marker-icon.png';
const shadowUrl = 'marker/marker-shadow.png';
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
  selector: 'lib-location-map',
  imports: [],
  templateUrl: './location-map.component.html',
  styleUrl: './location-map.component.scss'
})
export class LocationMapComponent {
  location = input<Location | SharedLocation>();
  private map: Map | undefined
  private marker: Marker | undefined

  constructor() {
    effect(() => {
      let l = this.location()
      if (l) this.updateMap()
    });
  }

  private updateMap() {
    if (this.marker) {
      this.marker.remove()
    }

    let location = this.location()
    if (!location) return

    if (!this.map) this.initMap()

    let lat = location.lat
    let lon = location.lon
    if (lat == 0.0 && lon == 0.0) return

    if (this.map) {
      this.marker = L.marker([lat, lon])
      this.marker.addTo(this.map)
      this.map.setView(new L.LatLng(lat, lon), 13)
    }
  }


  private initMap(): void {
    this.map = L.map('map', {
      center: [39.8282, -98.5795],
      zoom: 3
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map)
  }
}
