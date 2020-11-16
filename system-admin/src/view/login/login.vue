<style lang="less">
  @import './login.less';
</style>

<template>
  <div class="login">
    <div class="login-con">
      <Card icon="log-in" title="欢迎登录" :bordered="false">
        <div class="form-con">
          <login-form @on-success-valid="handleSubmit"></login-form>
          <p class="login-tip"></p>
        </div>
      </Card>
    </div>
  </div>
</template>

<script>
import LoginForm from '_c/login-form'
import { mapActions } from 'vuex'
import { login, logout } from '@/api/user'
export default {
  components: {
    LoginForm
  },
  methods: {
    ...mapActions([
      'handleLogin',
      'getUserInfo'
    ]),
    handleSubmit ({ username, password }) {
      console.log('in handleSubmit')
      this.handleLogin({ username, password }).then(res => {
        console.log(res)
        let user = res;
        if(user.token) {
          this.$router.push({
            name: this.$config.homeName
          })
        }
      })
    }
  }
}
</script>

<style>

</style>
