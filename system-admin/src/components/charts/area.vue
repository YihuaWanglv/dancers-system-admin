<template>
  <div ref="dom" ></div>
</template>

<script>
import echarts from 'echarts'
import tdTheme from './theme.json'
import { on, off } from '@/libs/tools'
echarts.registerTheme('tdTheme', tdTheme)
export default {
  name: 'ChartArea',
  props: {
    value: Object,
    text: String,
    yAxisFormatter: String
  },
  data () {
    return {
      dom: null
    }
  },
  methods: {
    resize () {
      this.dom.resize()
    }
  },
  mounted () {
    this.$nextTick(() => {
      let xAxisData = Object.keys(this.value)
      let seriesData = Object.values(this.value)
      let option = {
        title: {
          text: this.text || 'Area Chart',
          x: 'center'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: xAxisData
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: this.yAxisFormatter || '{value}'
          }
        },
        series: [{
          data: seriesData,
          type: 'line',
          areaStyle: {}
        }]
      }
      this.dom = echarts.init(this.$refs.dom, 'tdTheme')

      this.dom.setOption(option)
      on(window, 'resize', this.resize)
    })
  },
  beforeDestroy () {
    off(window, 'resize', this.resize)
  }
}
</script>
