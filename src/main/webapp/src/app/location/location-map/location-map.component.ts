import {AfterViewInit, Component, Input} from '@angular/core';
import {Location} from "../model/location-api";
import * as L from "leaflet";
import {Map, Marker} from "leaflet";

@Component({
  selector: 'app-location-map',
  templateUrl: './location-map.component.html',
  styleUrls: ['./location-map.component.scss']
})
export class LocationMapComponent implements AfterViewInit {
  @Input()
  set data(data: Location) {
    this.location = data;
    if (this.location != null) {
      this.updateMap()
    }
  }

  location: Location | undefined;
  private map: Map | undefined
  private marker: Marker | undefined

  ngAfterViewInit(): void {
    this.initMap()
  }

  private updateMap() {
    if (this.marker) {
      this.marker.remove()
    }

    if (!this.location) return

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
    this.updateMap()
  }
}
