<style lang="less">
  @import "../../styles/common.less";
</style>
<template>
  <div>
    <Card>
      <Row>
        <Col span="24">
          <Button type="primary" @click="create" style="margin: 0 10px 0 0;">新增</Button>

          <Select v-model="searchKey" style="margin: 0 7px 0 0;width: 160px" placeholder="选择需要查询的字段">
            <Option v-for="item in searchKeys" :value="item.searchKeyId" :key="item.searchKeyId">{{ item.searchKeyName }}</Option>
          </Select>
          <Input @keyup.enter.native="handleSearch" v-model="searchValue" placeholder="请输入查询字段的内容" style="margin: 0 7px 0 0;width: 160px" />
          <!-- <Input v-model="searchConName" placeholder="请输入搜索" style="width: 200px" /> -->
          <span @click="handleSearch" style="margin: 0 10px;">
            <Button type="primary" icon="search">搜索</Button>
          </span>

          <Button @click="handleCancel">重置</Button>
        </Col>
      </Row>
      <Row class="margin-top-10">
        <Table :columns="columns" :data="tableData" border stripe highlight-row>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="primary" size="small" style="margin-right: 3px" @click="showDetail(row)">详细</Button>
            <Button type="primary" size="small" style="margin-right: 3px" @click="update(row)">编辑</Button>
            <Button type="error" size="small" style="margin-right: 3px" @click="handleUpdatePasswordButton(row)">重设密码</Button>
            <Button type="error" size="small" @click="deleteConfirm(row.userId)">删除</Button>
          </template>
        </Table>
        <Page :total="total" :page-size="size" @on-change="changePage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator />
      </Row>
    </Card>
    <div>
      <Drawer :closable="false" width="640" v-model="isShowDetailDrawer">
        <h2>详情</h2>
        <Divider />
        <div class="detail-drawer-profile">
          <Row>用户名: {{entity.username}}</Row>
          <Row>姓名: {{entity.realName}}</Row>
          <Row>电话: {{entity.mobile}}</Row>
          <Row>性别: {{convert.valueOfSex(entity.sex)}}</Row>
          <Row>状态: {{convert.valueOfStatus(entity.state)}}</Row>
        </div>
        <Divider />
        <div class="detail-drawer-footer">
          <Button type="primary" @click="isShowDetailDrawer = false">关闭</Button>
        </div>
      </Drawer>
    </div>
    <div>
      <Drawer :closable="false" width="640" v-model="isShowSaveDrawer">
        <h2>保存</h2>
        <Divider />
        <Form :model="entity" :label-width="120">
          <div class="detail-drawer-profile">
            <FormItem label="用户名"><Input v-model="entity.username" placeholder="用户名" /></FormItem>
            <FormItem label="姓名"><Input v-model="entity.realName" placeholder="姓名" /></FormItem>
            <FormItem label="密码" v-show="isShowPassword"><Input v-model="entity.passwordInput" placeholder="密码"></Input></FormItem>
            <FormItem label="电话"><Input v-model="entity.mobile" placeholder="电话" /></FormItem>
            <FormItem label="性别">
              <RadioGroup v-model="entity.sex">
                <Radio :label="1">男</Radio>
                <Radio :label="2">女</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="状态">
              <RadioGroup v-model="entity.state">
                <Radio :label="1">启用</Radio>
                <Radio :label="0">禁用</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="角色">
              <Select v-model="entity.roleIdList" @on-change="onSelectChanged" multiple>
                <Option v-for="item in roles" :value="item.roleId" :key="item.roleId">{{ item.roleName }}</Option>
              </Select>
            </FormItem>
            
          </div>
          <div class="detail-drawer-footer">
            <FormItem>
              <Button type="primary" @click="save">保存</Button>
              <Button type="primary" @click="isShowSaveDrawer=false" style="margin: 0 10px 0 10px;">关闭</Button>
            </FormItem>
          </div>
        </Form>
      </Drawer>
    </div>
    <div>
      <Drawer title="重置帐号密码" v-model="isShowUpdatePasswordDrawer" width="400">
        <Form :model="updatePasswordForm" :label-width="120">
          <FormItem label="新密码">
            <Input v-model="updatePasswordForm.passwordInput" placeholder="请输入新密码"></Input>
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
  import { getUser, getUsers, saveUser, deleteUser } from '@/api/user'
  import { getRoles } from '@/api/role'
  export default {
    name: 'system-users',
    data() {
      return {
        convert: convert,
        total: 0,
        page: 1,
        size: 10,
        isShowDetailDrawer: false,
        isShowSaveDrawer: false,
        isShowPassword: false,
        isShowUpdatePasswordDrawer: false,
        searchKey: '',
        searchValue: '',
        roles: [],
        searchKeys: [{
          'searchKeyId': 'mobile',
          'searchKeyName': '手机'
        }, {
          'searchKeyId': 'username',
          'searchKeyName': '用户名'
        }, {
          'searchKeyId': 'realName',
          'searchKeyName': '姓名'
        }],
        entity: {
          userId: '',
          username: '',
          passwordInput: '',
          realName: ''
        },
        emptyFormItem: {
          userId: '',
          username: '',
          passwordInput: '',
          realName: ''
        },
        updatePasswordForm: {
          userId: null,
          passwordInput: ''
        },
        columns: [{
          key: 'username',
          // width: 150,
          title: '用户名'
        }, {
          key: 'realName',
          width: 120,
          title: '姓名'
        }, {
          key: 'mobile',
          width: 150,
          title: '电话'
        }, {
          key: "sex",
          width: 70,
          title: "性别",
          render: (h, params) => {
            if (params.row.sex == '1') { return h('Span', '男') }
            if (params.row.sex == '2') { return h('Span', '女') }
            return h('Span', '')
          }
        }, {
          key: "state",
          width: 70,
          title: "状态",
          render: (h, params) => {
            if (params.row.state == '1') { return h('Span', '启用') }
            if (params.row.state == '0') { return h('Span', '禁用') }
            return h('Span', '')
          }
        }, {
          title: '操作',
          slot: 'action',
          width: 300,
          align: 'center'
        }],
        tableData: []
      }
    },
    methods: {
      init() {
        this.list()
      },
      list() {
        let param = { size: this.size, page: this.page }
        if (this.searchKey && this.searchValue) {
          param[this.searchKey] = this.searchValue
        }
        this.tableData = []
        getUsers(param).then(res => {
          if (res.data && res.data.content) {
            this.tableData = res.data.content
            this.total = res.data.totalElements
          } else {
            if (this.searchConName !== '') {
              this.$Message.info('没有找到任何数据')
            } else {
              this.$Message.info('获取数据失败:' + res.data.message)
            }
            this.total = 0
          }
        }).catch(err => {
          console.log(err)
        })
      },
      loadRoles() {
        getRoles({}).then(res => {
          if (res.data) {
            this.roles = res.data
          } else {
            this.$Message.info('获取角色数据失败:' + res.data.message)
          }
        }).catch(err => {
          console.log(err)
        })
      },
      changePageSize(index) {
        this.size = index// 实时获取当前需要显示的条数
        this.list()
      },
      changePage(row) {
        this.page = row
        this.list()
      },
      create() {
        this.entity = {}
        this.isShowSaveDrawer = true
        this.isShowPassword = true
        this.loadRoles()
      },
      save() {
        saveUser(this.entity).then(res => {
          this.$Message.info('保存成功！')
          this.isShowSaveDrawer = false
          this.list()
        }).catch(err => {
          console.log(err)
          this.$Message.info('保存失败!')
        })
      },
      deleteConfirm(id) {
        let _this = this
        this.$Modal.confirm({
          title: '操作确认',
          content: '是否确认删除此记录?ID=' + id,
          onOk: function () {
            _this.delete(id)
          },
          onCancel: function () {
            this.$Message.info('操作取消.')
          }
        })
      },
      delete(id) {
        deleteUser(id).then(res => {
          console.log(res)
          this.$Message.info('删除成功!')
          this.list()
        }).catch(err => {
          console.log(err)
          this.$Message.info('删除失败!')
        })
      },
      update(obj) {
        console.log(obj)
        this.loadRoles()
        this.entity = this.emptyFormItem
        this.entity = obj
        this.isShowSaveDrawer = true
        this.isShowPassword = false
      },
      handleUpdatePasswordButton(row) {
        this.updatePasswordForm = {}
        this.updatePasswordForm.userId = row.userId
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
        if (!this.updatePasswordForm.passwordInput) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先输入新密码'
          })
          return false
        }
        saveUser(this.updatePasswordForm).then(res => {
          this.$Message.info('保存成功！')
          this.isShowUpdatePasswordDrawer = false
        }).catch(err => {
          console.log(err)
          this.$Message.info('保存失败!' + err.response.data.message)
        })
      },
      showDetail(row) {
        this.entity = row
        this.isShowDetailDrawer = true
      },
      onSelectChanged(v) {
        console.log(v)
        console.log(this.entity)
      },
      handleSearch() {
        this.list()
      },
      handleCancel() {
        this.searchKey = null
        this.searchValue = null
      }
    },
    mounted() {
      this.init()
    }
  }
</script>
<style>
</style>