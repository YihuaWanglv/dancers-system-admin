<template>
  <div class="user-avator-dropdown">
    <Dropdown @on-click="handleClick">
      <!-- <Avatar :src="userAvator" /> -->
      <strong>{{loginUser}}</strong>
      <Icon :size="18" type="md-arrow-dropdown"></Icon>
      <DropdownMenu slot="list">
        <DropdownItem name="users-center">个人中心</DropdownItem>
        <DropdownItem name="logout">退出登录</DropdownItem>
      </DropdownMenu>
    </Dropdown>
  </div>
</template>

<script>
  import './user.less'
  import { mapActions } from 'vuex'
  export default {
    name: 'User',
    props: {
      userAvator: {
        type: String,
        default: ''
      },
      loginUser: ''
    },
    methods: {
      ...mapActions([
        'handleLogOut'
      ]),
      handleClick(name) {
        switch (name) {
        case 'logout':

          this.$router.push({
            name: this.$config.homeName
          })
          this.handleLogOut().then(() => {
            this.$router.push({
              name: 'login'
            })
          })
          break
        case 'users-center':
          this.$router.push({
            name: 'users-center'
          })
          break
        }
      }
    }
  }
</script>