import {Component} from '@angular/core';
import {CacheService} from "../model/cache.service";
import {CacheInfo} from "../model/cache-api";
import {EChartsOption} from "echarts";

@Component({
    selector: 'app-cache-board',
    templateUrl: './cache-board.component.html',
    styleUrls: ['./cache-board.component.scss'],
    standalone: false
})
export class CacheBoardComponent {

  reloading: boolean = false
  info: CacheInfo[] = []
  displayedColumns: string[] = ['name', 'cmd']
  chart: EChartsOption = {
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
  values: EChartsOption = {}

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
