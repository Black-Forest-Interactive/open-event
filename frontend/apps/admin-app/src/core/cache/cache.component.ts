import { Component, computed, inject, resource } from '@angular/core'
import { NgxEchartsDirective } from 'ngx-echarts'
import { CacheInfo } from '@open-event/core'
import type { EChartsCoreOption } from 'echarts/core'
import { CacheService } from '@open-event/admin'
import { BoardComponent } from '../../shared/board/board.component'
import { LoadingBarComponent, toPromise } from '@open-event/shared'
import { MatTableModule } from '@angular/material/table'
import { MatIconModule } from '@angular/material/icon'
import { MatButtonModule } from '@angular/material/button'
import { MatCardModule } from '@angular/material/card'
import { MatListModule } from '@angular/material/list'
import { MatDividerModule } from '@angular/material/divider'
import { MatTooltipModule } from '@angular/material/tooltip'

@Component({
  selector: 'admin-cache',
  imports: [MatTableModule, MatIconModule, MatButtonModule, MatCardModule, MatListModule, MatDividerModule, MatTooltipModule, NgxEchartsDirective, BoardComponent, LoadingBarComponent],
  templateUrl: './cache.component.html',
  styleUrl: './cache.component.scss'
})
export class CacheComponent {
  displayedColumns: string[] = ['name', 'hit', 'load', 'evict', 'cmd']
  chart: EChartsCoreOption = {
    animation: true,
    animationDuration: 400,
    animationEasing: 'cubicOut',
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow',
        shadowStyle: {
          color: 'rgba(0, 0, 0, 0.05)'
        }
      },
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e0e0e0',
      borderWidth: 1,
      textStyle: {
        color: '#333'
      },
      formatter: (params: any) => {
        if (Array.isArray(params)) {
          let tooltip = `<div style="font-weight: 600; margin-bottom: 8px;">${params[0].axisValue}</div>`
          params.forEach((param: any) => {
            tooltip += `
              <div style="display: flex; justify-content: space-between; align-items: center; margin: 4px 0;">
                <span style="display: inline-block; width: 10px; height: 10px; border-radius: 50%; background-color: ${param.color}; margin-right: 8px;"></span>
                <span style="flex: 1;">${param.seriesName}:</span>
                <strong style="margin-left: 12px;">${param.value.toLocaleString()}</strong>
              </div>
            `
          })
          return tooltip
        }
        return ''
      }
    },
    legend: {
      top: 0,
      left: 'center',
      icon: 'roundRect',
      itemWidth: 14,
      itemHeight: 14,
      textStyle: {
        fontSize: 13,
        fontWeight: 500
      },
      itemGap: 20
    },
    grid: {
      left: 100,
      right: 40,
      top: 50,
      bottom: 40,
      containLabel: true
    },
    xAxis: {
      type: 'value',
      name: 'Count',
      nameLocation: 'middle',
      nameGap: 30,
      nameTextStyle: {
        fontSize: 13,
        fontWeight: 500,
        color: '#666'
      },
      axisLine: {
        show: true,
        lineStyle: { color: '#e0e0e0' }
      },
      axisTick: { show: false },
      axisLabel: {
        fontSize: 12,
        color: '#666',
        formatter: (value: number) => {
          if (value >= 1000000) return `${(value / 1000000).toFixed(1)}M`
          if (value >= 1000) return `${(value / 1000).toFixed(1)}K`
          return value.toString()
        }
      },
      splitLine: {
        lineStyle: {
          type: 'dashed',
          color: '#f0f0f0'
        }
      }
    },
    yAxis: {
      type: 'category',
      data: ['Hits', 'Loads', 'Evictions'],
      axisTick: { show: false },
      axisLine: {
        show: true,
        lineStyle: { color: '#e0e0e0' }
      },
      axisLabel: {
        fontWeight: 500,
        fontSize: 13,
        color: '#333'
      }
    },
    series: [],
    color: [
      '#4CAF50', // Green for primary
      '#2196F3', // Blue for secondary
      '#FF9800', // Orange for tertiary
      '#9C27B0', // Purple
      '#00BCD4', // Cyan
      '#F44336' // Red
    ]
  }
  values = computed(() => this.updateChart(this.entries()))
  private service = inject(CacheService)
  private cacheResource = resource({
    loader: (param) => {
      return toPromise(this.service.getAllCaches(), param.abortSignal)
    }
  })
  entries = computed(() => this.cacheResource.value() ?? [])
  reloading = this.cacheResource.isLoading

  updateChart(entries: CacheInfo[]): EChartsCoreOption {
    return {
      yAxis: {
        data: entries.map((i) => i.name).reverse()
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
          data: entries.map((i) => i.hitCount)
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
          data: entries.map((i) => i.missCount)
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
          data: entries.map((i) => i.evictionCount)
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
          data: entries.map((i) => i.evictionWeight)
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
          data: entries.map((i) => i.loadSuccessCount)
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
          data: entries.map((i) => i.loadFailureCount)
        }
      ]
    }
  }

  reset(info: CacheInfo) {
    this.service.resetCache(info.key).subscribe(() => this.cacheResource.reload())
  }

  formatCacheInfo(stats: CacheInfo): string {
    const parts: string[] = []

    const hitRate = stats.hitCount <= 0 ? 0 : (stats.hitCount / (stats.hitCount + stats.missCount)) * 100
    if (hitRate !== undefined) {
      parts.push(`Hit rate: ${hitRate.toFixed(1)}%`)
    }

    const total = stats.hitCount + stats.missCount + stats.evictionCount
    if (total > 0) {
      parts.push(`Total ops: ${this.formatNumber(total)}`)
    }

    return parts.join(' • ')
  }

  formatNumber(num: number): string {
    if (num >= 1000000) {
      return `${(num / 1000000).toFixed(1)}M`
    }
    if (num >= 1000) {
      return `${(num / 1000).toFixed(1)}K`
    }
    return num.toString()
  }

  reload() {
    this.cacheResource.reload()
  }
}
