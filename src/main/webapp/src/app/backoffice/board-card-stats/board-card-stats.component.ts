import {Component} from '@angular/core';
import {EChartsOption} from "echarts";
import {EventStats} from "../../event/model/event-api";
import {EventService} from "../../event/model/event.service";

@Component({
  selector: 'app-board-card-stats',
  templateUrl: './board-card-stats.component.html',
  styleUrls: ['./board-card-stats.component.scss']
})
export class BoardCardStatsComponent {
  eventChart: EChartsOption = {
    legend: {},
    tooltip: {},
    xAxis: {type: 'category',},
    yAxis: {type: 'value'},
    series: [
      {type: 'bar'},
      {type: 'bar'},
      {type: 'bar'}
    ]
  };

  eventValues: EChartsOption = {}


  participantChart: EChartsOption = {
    legend: {},
    tooltip: {},
    xAxis: {type: 'category',},
    yAxis: {type: 'value'},
    series: [
      {type: 'bar'},
      {type: 'bar'},
      {type: 'bar'},
      {type: 'bar'}
    ]
  };
  participantValues: EChartsOption = {}
  reloading: boolean = false
  stats: EventStats[] = []


  constructor(private eventService: EventService) {
  }

  ngOnInit() {
    this.reload()
  }

  private updateChart() {
    let totalEventAmount = this.stats.length
    let fullEventAmount = this.stats.filter(s => s.isFull).length
    let emptyEventAmount = this.stats.filter(s => s.isEmpty).length
    let availableEventAmount = totalEventAmount - fullEventAmount - emptyEventAmount

    this.eventValues = {
      dataset: {
        source: [
          ['label', 'full', 'empty', 'available'],
          ['', fullEventAmount, emptyEventAmount, availableEventAmount],
        ]
      }
    };

    let participantsSize = this.stats.map(p => p.participantsSize).reduce((a, b) => a + b, 0);
    let participantsAmount = this.stats.map(p => p.participantsAmount).reduce((a, b) => a + b, 0);
    let waitingListSize = this.stats.map(p => p.waitingListSize).reduce((a, b) => a + b, 0);
    let waitingListAmount = this.stats.map(p => p.waitingListAmount).reduce((a, b) => a + b, 0);

    this.participantValues = {
      dataset: {
        source: [
          ['label', 'Participants ', 'Participant Amount', 'Waitinglist', 'Waitinglist Amount'],
          ['', participantsSize, participantsAmount, waitingListSize, waitingListAmount],
        ]
      }
    };
  }

  private reload() {
    if (this.reloading) return
    this.eventService.getStats().subscribe(d => this.handleData(d))
  }

  private handleData(d: EventStats[]) {
    this.stats = d
    this.updateChart()
    this.reloading = false
  }
}
