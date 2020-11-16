import axios from '@/libs/api.request'
export const getOrg = (id) => {
  return axios.request({
    url: '/orgs/' + id,
    method: 'get'
  })
}
export const getOrgs = (params) => {
  return axios.request({
    url: '/orgs',
    params: params,
    method: 'get'
  })
}
export const saveOrg = (params) => {
  return axios.request({
    url: '/orgs' + (params.orgId ? ('/' + params.orgId) : ''),
    params: params,
    method: params.orgId ? 'put' : 'post'
  })
}
export const deleteOrg = (id) => {
  return axios.request({
    url: '/orgs/' + id,
    method: 'delete'
  })
}

