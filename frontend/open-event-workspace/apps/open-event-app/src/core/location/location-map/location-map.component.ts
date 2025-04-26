import {Component, Input} from '@angular/core';
import * as L from "leaflet";
import {icon, Map, Marker} from "leaflet";
import {Location, SharedLocation} from "@open-event-workspace/core";


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
  selector: 'app-location-map',
  templateUrl: './location-map.component.html',
  styleUrls: ['./location-map.component.scss'],
  standalone: true
})
export class LocationMapComponent {
  location: Location | SharedLocation | undefined;
  private map: Map | undefined
  private marker: Marker | undefined

  @Input()
  set data(data: Location | SharedLocation) {
    this.location = data;
    if (this.location != null) {
      this.updateMap()
    }
  }

  private updateMap() {
    if (this.marker) {
      this.marker.remove()
    }

    if (!this.location) return

    if (!this.map) this.initMap()

    let lat = this.location.lat
    let lon = this.location.lon
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
