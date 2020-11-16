// import Vue from 'vue'
import axios from 'axios'
import store from '@/store'
// import Router from 'vue-router'
// import iView from 'iview'
import { setToken, getToken } from '@/libs/util'
// import { Spin } from 'iview'
const addErrorLog = errorInfo => {
  const { statusText, status, request: { responseURL } } = errorInfo
  let info = {
    type: 'ajax',
    code: status,
    mes: statusText,
    url: responseURL
  }
  // if (!responseURL.includes('save_error_logger')) store.dispatch('addErrorLog', info)
}
// const LOGIN_PAGE_NAME = 'login'
class HttpRequest {
  constructor(baseUrl = baseURL) {
    this.baseUrl = baseUrl
    this.queue = {}
  }
  getInsideConfig() {
    const config = {
      baseURL: this.baseUrl,
      headers: {
        // 'Authorization'
        'token': getToken()
      }
    }
    return config
  }
  destroy(url) {
    delete this.queue[url]
    if (!Object.keys(this.queue).length) {
      // Spin.hide()
    }
  }
  interceptors(instance, url) {
    // 请求拦截
    instance.interceptors.request.use(config => {

      // 添加全局的loading...
      if (!Object.keys(this.queue).length) {
        // Spin.show() // 不建议开启，因为界面不友好
      }
      this.queue[url] = true
      return config
    }, error => {
      return Promise.reject(error)
    })
    // 响应拦截
    instance.interceptors.response.use(res => {
      console.log(res)
      this.destroy(url)
      if (res && res.data && res.data.code == 401) {
        // next({
        //   name: LOGIN_PAGE_NAME // 跳转到登录页
        // })
        setToken('')
        window.location = '/login'
        return Promise.reject(error)
      } else {
        const { data, status } = res
        return { data, status }
      }

    }, error => {
      console.log(error)
      this.destroy(url)
      // addErrorLog(error.response)
      return Promise.reject(error)
    })
  }
  request(options) {
    const instance = axios.create()
    options = Object.assign(this.getInsideConfig(), options)
    this.interceptors(instance, options.url)
    return instance(options)
  }
}
export default HttpRequest