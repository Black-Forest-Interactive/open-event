import {bootstrapApplication} from '@angular/platform-browser';
import {appConfig} from './app/app.config';
import {AppComponent} from './app/app.component';
import 'leaflet.markercluster/dist/leaflet.markercluster.js';

bootstrapApplication(AppComponent, appConfig).catch((err) =>
  console.error(err)
);
