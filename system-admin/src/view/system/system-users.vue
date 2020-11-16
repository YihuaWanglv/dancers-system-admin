<style lang="less">
  @import "../../styles/common.less";
</style>

<template>
  <div>
    <Card>
      <Row>
        <Col span="24">
        <Button @click="create" type="primary" style="margin: 0 10px 0 0;">添加</Button>
        <!-- <Button @click="onDetailButton" type="primary" style="margin: 0 10px 0 0;">详情</Button> -->
        <Button @click="onEditButton" type="primary" style="margin: 0 10px 0 0;">编辑</Button>
        <Button @click="onUpdatePasswordButton" type="primary" style="margin: 0 10px 0 0;">修改密码</Button>
        <Button type="primary" icon="md-download" @click="exportExcel" :loading="exportLoading" style="margin: 0 10px 0 0;">导出Excel</Button>
        <Button @click="onDeleteButton" type="error" style="margin: 0 10px 0 0;">删除</Button>
        </Col>
      </Row>
      <Row style="margin: 7px 0 0 0;">
        <Col span="24">
        <Select v-model="query.roleId" style="margin: 0 7px 0 0;width: 150px" placeholder="选择角色">
          <Option v-for="item in queryRoles" :value="item.roleId" :key="item.roleId">{{ item.roleName }}</Option>
        </Select>
        <Select v-model="query.areaId" style="margin: 0 7px 0 0;width: 150px" placeholder="选择段">
          <Option v-for="item in areas" :value="item.areaId" :key="item.areaId">{{ item.areaName }}</Option>
        </Select>
        <Select v-model="query.deptId" style="margin: 0 7px 0 0;width: 150px" placeholder="选择科室">
          <Option v-for="item in depts" :value="item.deptId" :key="item.deptId">{{ item.deptName }}</Option>
        </Select>
        <Select v-model="searchKey" style="margin: 0 7px 0 0;width: 160px" placeholder="选择其它查询字段" @on-change="onSearchSelectChange">
          <Option v-for="item in searchKeys" :value="item.searchKeyId" :key="item.searchKeyId">{{ item.searchKeyName }}</Option>
        </Select>
        <Input @keyup.enter.native="handleSearch" v-model="searchValue" placeholder="请输入查询字段的内容" style="margin: 0 7px 0 0;width: 160px" />
        <span @click="handleSearch" style="margin: 0 10px;">
          <Button type="primary" icon="search">查询</Button>
        </span>
        <Button type="primary" @click="handleCancel">重置</Button>
        </Col>
      </Row>
      <Row class="margin-top-10">
        <Table ref="table" :columns="columns" :data="tableData" @on-current-change="onRowSelect" border stripe highlight-row></Table>
        <Page :total="total" :page-size="size" @on-change="changePage" @on-page-size-change="_nowPageSize" show-total show-sizer show-elevator />
      </Row>
    </Card>
    <div>
      <Drawer :closable="false" width="500" v-model="isShowDetailDrawer">
        <h2>用户信息详情</h2>
        <Divider />
        <div class="demo-drawer-profile">
          <!-- <Row>ID: {{showDetailRecord.userId}}</Row> -->
          <Row>角色: {{showDetailRecord.roleName}}</Row>
          <Row>用户编号: {{showDetailRecord.username}}</Row>
          <Row>用户名称: {{showDetailRecord.realName}}</Row>
          <Row>段: {{showDetailRecord.areaName}}</Row>
          <Row>科室: {{showDetailRecord.deptName}}</Row>
          <Row>职务: {{showDetailRecord.post}}</Row>
          <Row>职称: {{showDetailRecord.title}}</Row>
          <Row>出生年月: {{showDetailRecord.birthMonth}}</Row>
          <Row>手机: {{showDetailRecord.mobile}}</Row>
          <Row>办公电话: {{showDetailRecord.phone}}</Row>
          <Row>性别: {{convert.valueOfSex(showDetailRecord.sex)}}</Row>
          <Row>状态: {{convert.valueOfStatus(showDetailRecord.status)}}</Row>
        </div>
        <div class="demo-drawer-footer">
          <Button status="primary" @click="isShowDetailDrawer = false">关闭</Button>
        </div>
      </Drawer>
    </div>
    <div>
      <Drawer :closable="false" width="600" v-model="isShowSaveDrawer">
        <h2>保存</h2>
        <Divider />
        <Form :model="formItem" :label-width="120">
          <div class="demo-drawer-profile">
            <FormItem label="角色">
              <Select v-model="formItem.roleId" @on-change="onRoleSelect">
                <Option v-for="item in roles" :value="item.roleId" :key="item.roleId">{{ item.roleName }}</Option>
              </Select>
            </FormItem>
            <FormItem label="用户编号"><Input v-model="formItem.username" placeholder="用户编号"></Input></FormItem>
            <FormItem label="用户名称"><Input v-model="formItem.realName" placeholder="用户名称"></Input></FormItem>
            <FormItem label="密码" v-show="isShowPassword"><Input v-model="formItem.password" placeholder="密码"></Input></FormItem>
            <FormItem label="段">
              <Select v-model="formItem.areaId" @on-change="onAreaSelect" v-if="formItem.roleId==3">
                <Option v-for="item in areas" :value="item.areaId" :key="item.areaId">{{ item.areaName }}</Option>
              </Select>
              <span v-if="formItem.roleId==2">电务部</span>
            </FormItem>
            <FormItem label="科室">
              <Select v-model="formItem.deptId">
                <Option v-for="item in depts" :value="item.deptId" :key="item.deptId">{{ item.deptName }}</Option>
              </Select>
            </FormItem>
            <!-- <FormItem label="选择专业">
              <Select v-model="formItem.majorId">
                <Option v-for="item in majors" :value="item.majorId" :key="item.majorId">{{ item.majorName }}</Option>
              </Select>
            </FormItem> -->
            <FormItem label="职务">
              <Input v-model="formItem.post" placeholder="职务" v-if="formItem.roleId!=2" />
              <Select v-model="formItem.post" @on-change="onManagerPostSelect" v-if="formItem.roleId==2">
                <Option v-for="item in posts" :value="item" :key="item">{{ item }}</Option>
              </Select>
            </FormItem>
            <FormItem label="职称"><Input v-model="formItem.title" placeholder="职称" /></FormItem>
            <FormItem label="性别">
              <RadioGroup v-model="formItem.sex">
                <Radio :label="1">男</Radio>
                <Radio :label="2">女</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="出生年月">
              <DatePicker type="month" placeholder="出生年月" :value="formItem.birthMonth" @on-change="setBirthMonth" format="yyyy-MM"></DatePicker>
            </FormItem>
            <FormItem label="电话"><Input v-model="formItem.mobile" placeholder="电话" /></FormItem>
            <FormItem label="办公电话"><Input v-model="formItem.phone" placeholder="办公电话" /></FormItem>
            <FormItem label="状态">
              <RadioGroup v-model="formItem.status">
                <Radio :label="1">启用</Radio>
                <Radio :label="0">禁用</Radio>
              </RadioGroup>
            </FormItem>
          </div>
          <div>
            <FormItem>
              <Button type="primary" @click="save">保存</Button>
              <Button status="primary" @click="isShowSaveDrawer=false" style="margin: 0 10px 0 10px;">关闭</Button>
            </FormItem>
          </div>
        </Form>
      </Drawer>
    </div>
    <div>
      <Drawer title="更新帐号密码" v-model="isShowUpdatePasswordDrawer" width="360" :styles="styles">
        <Form :model="updatePasswordForm" :label-width="120">
          <FormItem label="原密码">
            <Input v-model="updatePasswordForm.passwordOld" placeholder="请输入原密码"></Input>
          </FormItem>
          <FormItem label="新密码">
            <Input v-model="updatePasswordForm.passwordNew" placeholder="请输入新密码"></Input>
          </FormItem>
          <FormItem label="重复输入新密码">
            <Input v-model="updatePasswordForm.passwordNewAgain" placeholder="重复输入新密码"></Input>
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
  import excel from '@/libs/excel'
  import { getAccess, getUserId, getRoleLevel } from '@/libs/util'
  import { convert } from '@/libs/convert'
  import { getUser, getUsers, saveUser, deleteUser, getUserPermisstionRole, exportUsers } from '@/api/user'
  // import {
  //   getDepts,
  //   getMajors,
  //   getDeptsByAreaId,
  //   getAreas
  // } from '@/api/config'
  export default {
    name: 'bac-users',
    data() {
      return {
        // tableHeight: 0,
        convert: convert,
        total: 0,
        page: 1,
        size: 10,
        exportLoading: false,
        isShowDetailDrawer: false,
        isShowSaveDrawer: false,
        searchKey: '',
        searchValue: '',
        isShowPermissionDrawer: false,
        isShowUpdatePasswordDrawer: false,
        isShowPassword: true,
        searchKeys: [{
          'searchKeyId': 'mobile',
          'searchKeyName': '手机'
        }, {
          'searchKeyId': 'phone',
          'searchKeyName': '办公电话'
        }, {
          'searchKeyId': 'username',
          'searchKeyName': '用户编号'
        }, {
          'searchKeyId': 'realName',
          'searchKeyName': '用户名称'
        }, {
          'searchKeyId': 'post',
          'searchKeyName': '职务'
        }, {
          'searchKeyId': 'title',
          'searchKeyName': '职称'
        }],
        posts: ['处长', '科长', '工程师'],
        query: {
          roleId: null,
          areaId: null,
          deptId: null,
          mobile: null,
          username: null,
          realName: null,
          post: null,
          title: null,
          page: null,
          size: 10,
          sortType: null,
          sortBy: null
        },
        roles: [{
          'roleId': 3,
          'roleName': '站段'
        }],
        queryRoles: [{
          'roleId': 2,
          'roleName': '电务部'
        }, {
          'roleId': 3,
          'roleName': '站段'
        }],
        areas: [],
        depts: [],
        AllAreas: [],
        AllDepts: [],
        managerAreas: [{
          areaId: 0,
          areaName: '电务部'
        }],
        majors: [],
        selectedRow: null,
        styles: {
          height: 'calc(100% - 55px)',
          overflow: 'auto',
          paddingBottom: '53px',
          position: 'static'
        },
        updatePasswordForm: {
          userId: null,
          passwordOld: '',
          passwordNew: '',
          passwordNewAgain: ''
        },
        showDetailRecord: {
          userId: '',
          username: '',
          roleName: '',
          realName: '',
          password: '',
          status: '',
          roleId: '',
          roleLevel: '',
          areaId: '',
          deptId: '',
          majorId: '',
          birthMonth: '',
          sex: '',
          phone: '',
          post: '',
          title: '',
          areaName: '',
          deptName: '',
          mobile: ''
        },
        emptyFormItem: {
          userId: '',
          username: '',
          roleName: '',
          realName: '',
          password: '',
          status: '',
          roleId: '',
          roleLevel: '',
          areaId: '',
          deptId: '',
          majorId: '',
          birthMonth: '',
          sex: '',
          phone: '',
          post: '',
          title: '',
          mobile: ''
        },
        formItem: {
          userId: '',
          username: '',
          roleName: '',
          realName: '',
          password: '',
          status: '',
          roleId: '',
          roleLevel: '',
          areaId: '',
          deptId: '',
          majorId: '',
          birthMonth: '',
          sex: '',
          phone: '',
          post: '',
          title: '',
          mobile: ''
        },
        columns: [
        {
          type: 'index',
          width: 65,
          title: '序号'
        },
        {
          key: 'roleName',
          width: 110,
          title: '角色'
        },
        {
          key: 'username',
          width: 130,
          title: '用户编号'
        },
        {
          key: 'realName',
          width: 160,
          title: '用户名称'
        }, {
          key: 'sex',
          width: 70,
          title: '性别',
          render: (h, params) => {
            if (params.row.sex == '1') { return h('Span', '男') }
            if (params.row.sex == '2') { return h('Span', '女') }
            return h('Span', '')
          }
        }, {
          key: 'birthMonth',
          width: 100,
          title: '出生年月'
        },
        {
          key: 'areaName',
          minWidth: 160,
          title: '段'
        },
        {
          key: 'deptName',
          minWidth: 160,
          // width: 140,
          title: '科室'
        },
        {
          key: 'post',
          width: 140,
          title: '职务'
        },
        {
          key: 'title',
          width: 140,
          title: '职称'
        }, {
          key: 'mobile',
          width: 130,
          title: '手机'
        }, {
          key: 'phone',
          width: 130,
          title: '电话'
        },
        {
          key: 'status',
          width: 70,
          title: '状态',
          render: (h, params) => {
            if (params.row.status == '1') { return h('Span', '启用') }
            if (params.row.status == '0') { return h('Span', '禁用') }
            return h('Span', '启用')
          }
        }],
        tableData: []
      }
    },
    methods: {
      init() {
        this.list()
        // this.loadAreas()
        // this.loadDepts()
        // this.loadMajors()
        // this.loadUserPermisstionRole()
      },
      list() {
        this.query.page = this.page
        this.query.size = this.size
        this.query.sortBy = this.sortBy
        this.query.sortType = this.sortType
        if (this.searchKey && this.searchValue) {
          this.query[this.searchKey] = this.searchValue
        }
        console.log(this.query)
        this.tableData = []
        this.selectedRow = null
        getUsers(this.query).then(res => {
          console.log(res)
          if (res.data && res.data.code == 200) {
            this.tableData = res.data.data.list
            this.total = res.data.data.total
          } else {
            this.$Message.info('获取数据失败:' + res.data.message)
            this.total = 0
          }
        }).catch(err => {
          console.log(err)
        })
      },
      _nowPageSize(index) {
        // 实时获取当前需要显示的条数
        this.size = index
        this.list()
      },
      changePage(row) {
        this.page = row
        this.list()
      },
      create() {
        this.formItem = {}
        this.isShowPassword = true
        this.isShowSaveDrawer = true
        this.formItem.status = 1
      },
      save() {
        saveUser(this.formItem).then(res => {
          console.log(res)
          if (res.data && res.data.code == 200) {
            this.$Message.info('保存成功！')
            this.isShowSaveDrawer = false
            this.list()
          } else {
            this.$Message.info('保存失败!' + res.data.message)
          }
        }).catch(err => {
          console.log(err)
          this.$Message.info('保存失败!')
        })
      },
      delete(id) {
        deleteUser(id).then(res => {
          console.log(res)
          if (res.data && res.data.code == 200) {
            this.$Message.info('删除成功!')
            this.list()
          } else {
            this.$Message.info('删除失败!')
          }
        }).catch(err => {
          console.log(err)
          this.$Message.info('删除失败!')
        })
      },
      update(obj) {
        console.log(obj)
        this.isShowPassword = false
        this.formItem = this.emptyFormItem
        this.formItem.userId = obj.userId
        this.formItem.username = obj.username
        this.formItem.realName = obj.realName
        this.formItem.status = obj.status
        this.formItem.mobile = obj.mobile
        this.formItem.roleId = obj.roleId
        this.formItem.areaId = obj.areaId
        this.formItem.deptId = obj.deptId
        this.formItem.birthMonth = obj.birthMonth
        this.formItem.sex = obj.sex
        this.formItem.phone = obj.phone
        this.formItem.post = obj.post
        this.formItem.title = obj.title
        this.isShowSaveDrawer = true
      },
      handleUpdatePasswordButton(userId) {
        this.updatePasswordForm = {}
        this.updatePasswordForm.userId = userId
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
        if (!this.updatePasswordForm.passwordOld) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先输入原密码'
          })
          return false
        }
        if (!this.updatePasswordForm.passwordNew) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先输入新密码'
          })
          return false
        }
        if (!this.updatePasswordForm.passwordNewAgain) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '请先重复输入新密码'
          })
          return false
        }
        if (this.updatePasswordForm.passwordNew != this.updatePasswordForm.passwordNewAgain) {
          this.$Modal.info({
            title: '操作提示',
            width: 666,
            content: '两次输入的新密码不相符，请先确认'
          })
          return false
        }
        let para = {
          userId: this.updatePasswordForm.userId,
          password: this.updatePasswordForm.passwordNew,
          oldPasswordInput: this.updatePasswordForm.passwordOld
        }
        saveUser(para).then(res => {
          console.log(res)
          if (res.data && res.data.code == 200) {
            this.$Message.info('保存成功！')
            this.isShowUpdatePasswordDrawer = false
            this.list()
          } else {
            this.$Message.info('保存失败!' + res.data.message)
          }

        }).catch(err => {
          console.log(err)
          console.log(err.response.data)
          this.$Message.info('保存失败!' + err.response.data.message)
        })
      },
      // loadAreas() {
      //   let param = {}
      //   getAreas(param).then(res => {
      //     if (res.data && res.data.code == 200) {
      //       this.areas = res.data.data
      //       this.AllAreas = res.data.data
      //     } else {
      //       this.$Message.info('没有段数据')
      //     }
      //   }).catch(err => {
      //     console.log(err)
      //   })
      // },
      // loadDepts() {
      //   let param = {}
      //   getDepts(param).then(res => {
      //     if (res.data && res.data.code == 200) {
      //       this.depts = res.data.data
      //       this.AllDepts = res.data.data
      //     } else {
      //       this.$Message.info('没有找科室数据')
      //     }
      //   })
      // },
      // loadMajors() {
      //   let param = {}
      //   getMajors(param).then(res => {
      //     if (res.data && res.data.code == 200) {
      //       this.majors = res.data.data
      //     } else {
      //       this.$Message.info('没有找专业数据')
      //     }
      //   })
      // },
      onAreaSelect(v) {
        console.log(v)
        this.loadDeptsByAreaId(v)
      },
      onRoleSelect(v) {
        if (v == 2) {
          // this.areas = this.managerAreas
          // this.formItem.areaId = 0
        } else {
          this.loadAreas()
        }
      },
      onManagerPostSelect(v) {

      },
      loadDeptsByAreaId(_areaId) {
        getDeptsByAreaId(_areaId).then(res => {
          if (res.data && res.data.code == 200) {
            this.depts = res.data.data
          } else {
            this.$Message.info('没有找科室数据')
          }
        })
      },
      // loadUserPermisstionRole() {
      //   getUserPermisstionRole().then(res => {
      //     if (res.data && res.data.data) {
      //       this.roles = res.data.data
      //     } else {
      //       this.$Message.info('没有找角色数据')
      //     }
      //   })
      // },
      setBirthMonth(e) {
        this.formItem.birthMonth = e
      },
      onRowSelect(currentRow, oldCurrentRow) {
        this.selectedRow = currentRow
      },
      onDetailButton() {
        if (!this.selectedRow) {
          this.$Message.info('请先选择一条记录!')
        }
        this.showDetail(this.selectedRow)
      },
      showDetail(row) {
        this.showDetailRecord = this.emptyDetailRecord
        this.showDetailRecord = row
        this.isShowDetailDrawer = true
      },
      onEditButton() {
        if (!this.selectedRow) {
          this.$Message.info('请先选择一条记录!')
          return
        }
        if (!this.checkPermission(this.selectedRow)) {
          this.$Message.info('您没有操作此记录的权限')
          return
        }
        this.update(this.selectedRow)
      },
      checkPermission(row) {
        let roleLevel = getRoleLevel()
        let userId = getUserId()
        console.log('user roleLevel=' + roleLevel)
        if ((row && row.roleLevel && roleLevel < row.roleLevel) || userId == row.userId) {
          return true
        } else {
          return false
        }
      },
      onUpdatePasswordButton() {
        if (!this.selectedRow) {
          this.$Message.info('请先选择一条记录!')
          return
        }
        if (!this.checkPermission(this.selectedRow)) {
          this.$Message.info('您没有操作此记录的权限')
          return
        }
        let id = this.selectedRow.userId
        this.handleUpdatePasswordButton(id)
      },
      onDeleteButton() {
        if (!this.selectedRow) {
          this.$Message.info('请先选择一条记录!')
          return
        }
        if (!this.checkPermission(this.selectedRow)) {
          this.$Message.info('您没有操作此记录的权限')
          return
        }
        let operatorUserId = getUserId()
        if (operatorUserId && operatorUserId == this.selectedRow.userId) {
          this.$Message.info('管理员不能删除自己的账号')
          return
        }
        let id = this.selectedRow.userId
        let _this = this
        this.$Modal.confirm({
          title: '操作确认',
          content: '是否确认删除此记录?',
          onOk: function () {
            _this.delete(id)
          },
          onCancel: function () {
            this.$Message.info('操作取消.')
          }
        })
      },
      exportExcel() {
        if (this.searchKey && this.searchValue) {
          this.query[this.searchKey] = this.searchValue
        }
        this.query.page = null
        getUsers(this.query).then(res => {
          console.log(res)
          if (res && res.data && res.data.code == 200) {
            let tableDataExport = res.data.data
            this.exportLoading = true
            const params = {
              title: ['角色', '编号', '用户名称', '性别', '出生年月', '段', '科室', '职务', '职称', '手机', '办公电话', '状态'],
              key: ['roleName', 'username', 'realName', 'sexName', 'birthMonth', 'areaName', 'deptName', 'post', 'title', 'mobile', 'phone', 'statusName'],
              data: tableDataExport,
              autoWidth: true,
              filename: '用户信息导出'
            }
            excel.export_array_to_excel(params)
            this.exportLoading = false
          } else {
            this.$Message.info('导出失败.')
          }
        }).catch(err => {
          console.log(err)
        })
      },
      onSearchSelectChange(v) {
        console.log('selected:' + v)
        // this.searchKey = v
      },
      handleSearch() {
        this.list()
      },
      handleCancel() {
        this.searchKey = null
        this.searchValue = null
        this.query = {}
      }
    },
    mounted() {
      this.init()
      // this.tableHeight = window.innerHeight - this.$refs.table.$el.offsetTop - 360
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
    padding: 10px;
    text-align: right;
    background: #fff;
  }
</style>