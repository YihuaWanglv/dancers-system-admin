import { login, logout, getUserInfo } from '@/api/user'
import { setToken, getToken, getAccess, setAccess, getUserId, setUserId, getRoleLevel, setRoleLevel, setUsername, setRealName, setTagNavListInLocalstorage, setUser } from '@/libs/util'

export default {
  state: {
    username: '',
    userId: '',
    avatorImgPath: '',
    token: getToken(),
    access: '',
    hasGetInfo: false
  },
  mutations: {
    setAvator(state, avatorPath) {
      state.avatorImgPath = avatorPath
    },
    setUserId(state, id) {
      state.userId = id
      setUserId(id)
    },
    setRoleLevel(state, roleLevel) {
      state.roleLevel = roleLevel
      setRoleLevel(roleLevel)
    },
    setUsername(state, name) {
      state.username = name
      setUsername(name)
    },
    setRealName(state, name) {
      state.realName = name
      setRealName(name)
    },
    setAccess(state, access) {
      state.access = access
      setAccess(access)
    },
    setToken(state, token) {
      state.token = token
      setToken(token)
    },
    setTagNavListInLocalstorage(state, list) {
      state.roleLevel = list
      setTagNavListInLocalstorage(list)
    },
    setUser(state, user) {
      state.user = user
      setUser(user)
    },
    setHasGetInfo(state, status) {
      state.hasGetInfo = status
    }
  },
  actions: {
    // 登录
    handleLogin({ commit }, { username, password }) {
      username = username.trim()
      return new Promise((resolve, reject) => {
        login({ username, password }).then(res => {
          console.log('in action:handleLogin:login')
          console.log(res)
          if (res.data && res.status === 200) {
            console.log('login ok')
            const data = res.data
            console.log(data)
            commit('setToken', data.token)
            commit('setUsername', data.username)
            commit('setUserId', data.userId)
            commit('setRealName', data.realName)
            // commit('setRoleLevel', data.roleLevel)
            commit('setAccess', data.access)
            commit('setUser', data)
            resolve(res.data)
          } else {
            alert('登录失败：' + res.data.message)
          }
        }).catch(err => {
          console.log('handleLogin error')
          console.log(err)
          reject(err)
        })
      })
    },
    // 退出登录
    handleLogOut({ state, commit }) {
      return new Promise((resolve, reject) => {
        // 如果你的退出登录无需请求接口，则可以直接使用下面三行代码而无需使用logout调用接口
        commit('setToken', '')
        commit('setUserId', '')
        // commit('setRoleLevel', '')
        commit('setRealName', '')
        commit('setUsername', '')
        commit('setAccess', [])
        commit('setUser', {})
        commit('setTagNavListInLocalstorage', [])
        resolve()
      })
    },
    // 获取用户相关信息
    getUserInfo({ state, commit }) {
      return new Promise((resolve, reject) => {
        try {
          getUserInfo(state.token).then(res => {
            const data = res.data
            commit('setAvator', data.avator)
            commit('setUsername', data.name)
            commit('setUserId', data.user_id)
            commit('setAccess', data.access)
            commit('setHasGetInfo', true)
            resolve(data)
          }).catch(err => {
            reject(err)
          })
        } catch (error) {
          reject(error)
        }
      })
    }
  }
}