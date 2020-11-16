import axios from '@/libs/api.request'
export const getRole = (id) => {
  return axios.request({
    url: '/roles/' + id,
    method: 'get'
  })
}
export const getRoles = (params) => {
  return axios.request({
    url: '/roles',
    params: params,
    method: 'get'
  })
}
export const saveRole = (params) => {
  return axios.request({
    url: '/roles' + (params.roleId ? ('/' + params.roleId) : ''),
    data: params,
    method: params.roleId ? 'put' : 'post'
  })
}
export const deleteRole = (id) => {
  return axios.request({
    url: '/roles/' + id,
    method: 'delete'
  })
}

