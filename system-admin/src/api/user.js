import axios from '@/libs/api.request'

export const login = ({ username, password }) => {
  const data = {
    username,
    password
  }
  return axios.request({
    url: '/login',
    params: data,
    method: 'post'
  })
}

export const getUserInfo = (token) => {
  return axios.request({
    url: 'get_info',
    params: {
      token
    },
    method: 'get'
  })
}

export const logout = (token) => {
  return axios.request({
    url: '/logout',
    method: 'post'
  })
}

export const getUser = (id) => {
  return axios.request({
    url: '/users/' + id,
    method: 'get'
  })
}
export const getUsers = (params) => {
  return axios.request({
    url: '/users',
    params: params,
    method: 'get'
  })
}
export const saveUser = (params) => {
  return axios.request({
    url: '/users' + (params.userId ? ('/' + params.userId) : ''),
    data: params,
    method: params.userId ? 'put' : 'post'
  })
}
export const deleteUser = (userId) => {
  return axios.request({
    url: '/users/' + userId,
    method: 'delete'
  })
}
export const getUserPermisstionRole = () => {
  return axios.request({
    url: '/users/permissions/roles',
    method: 'get'
  })
}