import {Component} from '@angular/core';
import {MatCard} from "@angular/material/card";
import {MatDivider} from "@angular/material/divider";
import {MatIcon} from "@angular/material/icon";
import {MatMiniFabButton} from "@angular/material/button";
import {NgxEchartsDirective} from "ngx-echarts";
import {CacheInfo} from "@open-event-workspace/core";
import type {EChartsCoreOption} from "echarts/core";
import {CacheService} from "@open-event-workspace/backoffice";
import {BoardComponent} from "../../shared/board/board.component";

@Component({
  selector: 'boffice-cache',
  imports: [
    MatMiniFabButton,
    MatCard,
    MatDivider,
    MatIcon,
    NgxEchartsDirective,
    BoardComponent
  ],
  templateUrl: './cache.component.html',
  styleUrl: './cache.component.scss',
})
export class CacheComponent {

  reloading: boolean = false
  info: CacheInfo[] = []
  displayedColumns: string[] = ['name', 'cmd']
  chart: EChartsCoreOption = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {},
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value'
    },
    yAxis: {
      type: 'category',
      data: ['hit', 'load', 'evict']
    },
    series: []
  };
  values: EChartsCoreOption = {}

  constructor(
    private service: CacheService,
  ) {
  }

  ngOnInit() {
    this.reload()
  }

  reload() {
    if (this.reloading) return
    this.reloading = true
    this.service.getAllCaches().subscribe(d => this.handleData(d))
  }

  reset(info: CacheInfo) {
    this.service.resetCache(info.key).subscribe(d => this.handleUpdate(d))
  }

  updateChart() {
    this.values = {
      yAxis: {
        data: this.info.map(i => i.name).reverse()
      },
      series: [
        {
          name: 'Hit',
          type: 'bar',
          stack: 'hit',
          label: {
            show: true
          },
          emphasis: {
            focus: 'series'
          },
          data: this.info.map(i => i.hitCount)
        },
        {
          name: 'Miss',
          type: 'bar',
          stack: 'hit',
          label: {
            show: true
          },
          emphasis: {
            focus: 'series'
          },
          data: this.info.map(i => i.missCount)
        },
        {
          name: 'Evict Count',
          type: 'bar',
          stack: 'evict',
          label: {
            show: true
          },
          emphasis: {
            focus: 'series'
          },
          data: this.info.map(i => i.evictionCount)
        },
        {
          name: 'Evict Weight',
          type: 'bar',
          stack: 'evict',
          label: {
            show: true
          },
          emphasis: {
            focus: 'series'
          },
          data: this.info.map(i => i.evictionWeight)
        },
        {
          name: 'Load Success',
          type: 'bar',
          stack: 'load',
          label: {
            show: true
          },
          emphasis: {
            focus: 'series'
          },
          data: this.info.map(i => i.loadSuccessCount)
        },
        {
          name: 'Load Failure',
          type: 'bar',
          stack: 'load',
          label: {
            show: true
          },
          emphasis: {
            focus: 'series'
          },
          data: this.info.map(i => i.loadFailureCount)
        }
      ]
    };
  }

  private handleData(d: CacheInfo[]) {
    this.info = d
    this.updateChart()
    this.reloading = false
  }

  private handleUpdate(d: CacheInfo) {
    let index = this.info.findIndex(value => d.key === value.key)
    if (index < 0) {
      this.info.push(d)
    } else {
      this.info[index] = d
    }
    this.updateChart()
  }


}
