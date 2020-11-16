<style lang="less">
  @import "../../styles/common.less";
</style>

<template>
  <div>
    <Card>

      <Row>
        <Form :model="formItem" :label-width="120" label-colon>
          <div class="demo-drawer-profile">
            <FormItem label="用户名"><span>{{formItem.username}}</span></FormItem>
            <FormItem label="姓名"><Input v-model="formItem.realName" placeholder="姓名"></Input></FormItem>

            <FormItem label="角色">
              <span>{{formItem.roleNames}}</span>
            </FormItem>
            <FormItem label="性别">
              <RadioGroup v-model="formItem.sex">
                <Radio :label="1">男</Radio>
                <Radio :label="2">女</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="电话"><Input v-model="formItem.mobile" placeholder="电话" /></FormItem>
            <FormItem label="状态">
              {{convert.valueOfStatus(formItem.state)}}
            </FormItem>
          </div>
          <div>
            <FormItem>
              <Button type="primary" @click="save" style="margin: 0 10px 0 0;">保存</Button>
              <Button @click="onUpdatePasswordButton" type="primary" style="margin: 0 10px 0 0;">修改密码</Button>
            </FormItem>
          </div>
        </Form>
      </Row>

    </Card>
    
    <div>
      <Drawer title="更新帐号密码" v-model="isShowUpdatePasswordDrawer" width="400" :styles="styles">
        <Form :model="updatePasswordForm" :label-width="120">
          <FormItem label="原密码">
            <Input v-model="updatePasswordForm.oldPasswordInput" placeholder="请输入原密码"></Input>
          </FormItem>
          <FormItem label="新密码">
            <Input v-model="updatePasswordForm.passwordInput" placeholder="请输入新密码"></Input>
          </FormItem>
          <FormItem label="重复输入新密码">
            <Input v-model="updatePasswordForm.passwordInputAgain" placeholder="重复输入新密码"></Input>
          </FormItem>
          <FormItem>
            <Button type="primary" @click="handleUpdatePassword">保存</Button>
            <Button type="primary" @click="isShowUpdatePasswordDrawer=false" style="margin: 0 10px 0 10px;">关闭</Button>
          </FormItem>
        </Form>
      </Drawer>
    </div>
  </div>
</template>

<script>
  import { convert } from '@/libs/convert'
  import { getUser, saveUser } from '@/api/user'
  import { getUserId } from '@/libs/util'
  export default {
    name: 'csconfigs',
    data() {
      return {
        exportLoading: false,
        convert: convert,
        userId: null,
        isShowUpdatePasswordDrawer: false,
        updatePasswordForm: {
          userId: null,
          oldPasswordInput: '',
          passwordInput: '',
          passwordInputAgain: ''
        },
        styles: {
          height: 'calc(100% - 55px)',
          overflow: 'auto',
          paddingBottom: '53px',
          position: 'static'
        },
        formItem: {}
      }
    },
    methods: {
      init() {
        this.userId = getUserId()
        this.loadUser()
      },
      loadUser() {
        if (!this.userId) {
          this.$Message.info('无法获取登录用户信息')
          return;
        }
        getUser(this.userId).then(res => {
          console.log(res)
          if (res.data) {
            this.formItem = res.data
          }
        }).catch(err => {
          console.log(err)
        })
      },
      onUpdatePasswordButton() {
        this.handleUpdatePasswordButton()
      },
      save() {
        let saveForm = {
          userId: this.userId,
          realName: this.formItem.realName,
          sex: this.formItem.sex,
          mobile: this.formItem.mobile
        }
        saveUser(saveForm).then(res => {
          this.$Message.info('保存成功！')
        }).catch(err => {
          this.$Message.info('保存失败!' + err.response.data.message)
        })
      },
      setBirthMonth(e) {
        this.formItem.birthMonth = e
      },
      handleUpdatePasswordButton() {
        this.updatePasswordForm = {}
        this.updatePasswordForm.userId = this.userId
        this.isShowUpdatePasswordDrawer = true
      },
      handleUpdatePassword() {
        if (!this.updatePasswordForm.userId) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先选择要更新密码的帐号'
          })
          return false
        }
        if (!this.updatePasswordForm.oldPasswordInput) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先输入原密码'
          })
          return false
        }
        if (!this.updatePasswordForm.passwordInput) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先输入新密码'
          })
          return false
        }
        if (!this.updatePasswordForm.passwordInputAgain) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先重复输入新密码'
          })
          return false
        }
        if (this.updatePasswordForm.passwordInput != this.updatePasswordForm.passwordInputAgain) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '两次输入的新密码不相符，请先确认'
          })
          return false
        }
        saveUser(this.updatePasswordForm).then(res => {
          this.$Message.info('保存成功！')
          this.isShowUpdatePasswordDrawer = false
        }).catch(err => {
          this.$Message.info('保存失败!' + err.response.data.message)
        })
      },
      handleCancel() {}
    },
    mounted() {
      this.init()
    }
  }
</script>
<style>
  .demo-drawer-footer {
    width: 100%;
    position: absolute;
    bottom: 0;
    left: 0;
    border-top: 1px solid #e8e8e8;
    padding: 10px 16px;
    text-align: right;
    background: #fff;
  }
</style>