import axios from '@/libs/api.request'

export const getKeys = (params) => {
  return axios.request({
    url: '/keys',
    params: params,
    method: 'get'
  })
}