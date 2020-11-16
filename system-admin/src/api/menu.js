import axios from '@/libs/api.request'
export const getMenu = (id) => {
  return axios.request({
    url: '/menus/' + id,
    method: 'get'
  })
}
export const getMenus = (params) => {
  return axios.request({
    url: '/menus',
    params: params,
    method: 'get'
  })
}
export const saveMenu = (params) => {
  return axios.request({
    url: '/menus' + (params.menuId ? ('/' + params.menuId) : ''),
    data: params,
    method: params.menuId ? 'put' : 'post'
  })
}
export const deleteMenu = (id) => {
  return axios.request({
    url: '/menus/' + id,
    method: 'delete'
  })
}

