import axios from '@/libs/api.request'
export const getApp = (id) => {
  return axios.request({
    url: '/apps/' + id,
    method: 'get'
  })
}
export const getApps = (params) => {
  return axios.request({
    url: '/apps',
    params: params,
    method: 'get'
  })
}
export const saveApp = (params) => {
  return axios.request({
    url: '/apps' + (params.appId ? ('/' + params.appId) : ''),
    params: params,
    method: params.appId ? 'put' : 'post'
  })
}
export const deleteApp = (id) => {
  return axios.request({
    url: '/apps/' + id,
    method: 'delete'
  })
}

