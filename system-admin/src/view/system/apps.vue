<style lang="less">
  @import "../../styles/common.less";
</style>
<template>
  <div>
    <Card>
      <Row>
        <Col span="24">
          <Button type="primary" @click="create" style="margin: 0 10px 0 0;">新增</Button>
          <Input v-model="searchConName" placeholder="请输入搜索" style="width: 200px" />
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
            <Button type="error" size="small" @click="deleteConfirm(row.appId)">删除</Button>
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
          <Row>应用名称: {{entity.appName}}</Row>
          <Row>应用Key: {{entity.appKey}}</Row>
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
            <FormItem label="应用名称"><Input v-model="entity.appName" placeholder="应用名称" /></FormItem>
            <FormItem label="应用Key"><Input v-model="entity.appKey" placeholder="应用Key" /></FormItem>
            <FormItem label="状态">
              <RadioGroup v-model="entity.state">
                <Radio :label="1">启用</Radio>
                <Radio :label="0">禁用</Radio>
              </RadioGroup>
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
  </div>
</template>

<script>
  import { convert } from '@/libs/convert'
  import { getApp, getApps, saveApp, deleteApp } from '@/api/app'
  export default {
    name: 'system-apps',
    data() {
      return {
        convert: convert,
        total: 0,
        page: 1,
        size: 10,
        isShowDetailDrawer: false,
        isShowSaveDrawer: false,
        searchConName: '',
        entity: {
          appId: '',
          appKey: '',
          appName: ''
        },
        emptyFormItem: {
          appId: '',
          appKey: '',
          appName: ''
        },
        columns: [{
          type: 'index',
          width: 70,
          title: '序号'
        }, {
          key: 'appName',
          title: '应用名称'
        }, {
          key: 'appKey',
          title: '应用Key'
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
          width: 240,
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
        let page = this.page
        let param = {
          size: this.size,
          page: page
        }
        this.tableData = []
        getApps(param)
          .then(res => {
            console.log(res)
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
      },
      save() {
        saveApp(this.entity).then(res => {
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
        deleteApp(id).then(res => {
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
        this.entity = this.emptyFormItem
        this.entity = obj
        this.isShowSaveDrawer = true
      },
      showDetail(row) {
        this.entity = row
        this.isShowDetailDrawer = true
      },
      handleSearch() {
        this.list()
      },
      handleCancel() {}
    },
    mounted() {
      this.init()
    }
  }
</script>
<style>
</style>