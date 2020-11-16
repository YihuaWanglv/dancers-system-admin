import axios from '@/libs/api.request'
export const getCorp = (id) => {
  return axios.request({
    url: '/corps/' + id,
    method: 'get'
  })
}
export const getCorps = (params) => {
  return axios.request({
    url: '/corps',
    params: params,
    method: 'get'
  })
}
export const saveCorp = (params) => {
  return axios.request({
    url: '/corps' + (params.corpId ? ('/' + params.corpId) : ''),
    params: params,
    method: params.corpId ? 'put' : 'post'
  })
}
export const deleteCorp = (id) => {
  return axios.request({
    url: '/corps/' + id,
    method: 'delete'
  })
}

