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
        <Table :columns="columns" :data="tableData" row-key="orgId" border stripe highlight-row>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="default" size="small" style="margin-right: 3px" @click="createSubNode(row.orgId)"> + </Button>
            <Button type="primary" size="small" style="margin-right: 3px" @click="showDetail(row)">详细</Button>
            <Button type="primary" size="small" style="margin-right: 3px" @click="update(row)">编辑</Button>
            <Button type="error" size="small" @click="deleteConfirm(row.orgId)">删除</Button>
          </template>
        </Table>
        <!-- <Page :total="total" :page-size="size" @on-change="changePage" @on-page-size-change="changePageSize" show-total show-sizer show-elevator /> -->
      </Row>
    </Card>
    <div>
      <Drawer :closable="false" width="640" v-model="isShowDetailDrawer">
        <h2>详情</h2>
        <Divider />
        <div class="detail-drawer-profile">
          <Row>机构名称: {{entity.orgName}}</Row>
          <!-- <Row>上级机构: {{entity.parentId}}</Row> -->
          <Row>机构类型: {{entity.orgTypeName}}</Row>
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
            <FormItem label="机构名称"><Input v-model="entity.orgName" placeholder="机构名称" /></FormItem>
            <FormItem label="上级机构">
              <treeselect name="roleMenuSelect" v-model="entity.parentId" :multiple="false" :options="wrapSelectTree()" :show-count="true" :default-expand-level="2"
                placeholder="请选择上级机构" @select="onTreeSelected" noChildrenText="该机构下暂无子机构"
                noOptionsText="没有可供选择的机构，请先录入机构" noResultsText="没有找到查询的机构" />
            </FormItem>
            <FormItem label="机构类型">
              <Select v-model="entity.orgType">
                <Option v-for="item in orgTypes" :value="item.key" :key="item.key">{{ item.alias }}</Option>
              </Select>
            </FormItem>
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
  import Treeselect from "@riophae/vue-treeselect"
  import "@riophae/vue-treeselect/dist/vue-treeselect.css"
  import { convert } from '@/libs/convert'
  import { getKeys } from '@/api/keys'
  import { getOrg, getOrgs, saveOrg, deleteOrg } from '@/api/org'
  export default {
    name: 'system-orgs',
    components: { Treeselect },
    data() {
      return {
        convert: convert,
        isShowDetailDrawer: false,
        isShowSaveDrawer: false,
        searchConName: '',
        orgTypes: [],
        entity: {
          orgId: '',
          parentId: '',
          orgType: '',
          orgTypeName: '',
          state: '',
          orgName: ''
        },
        emptyFormItem: {
          orgId: '',
          parentId: '',
          orgType: '',
          orgTypeName: '',
          state: '',
          orgName: ''
        },
        columns: [{
          key: 'orgName',
          width: 240,
          tree: true,
          title: '机构名称'
        }, {
          key: 'orgTypeName',
          title: '机构类型'
        }, {
          key: 'state',
          title: '状态',
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
        this.loadKeys()
      },
      list() {
        let param = {
          fetchTree: true,
        }
        this.tableData = []
        getOrgs(param).then(res => {
            if (res.data) {
              this.tableData = res.data
            } else {
              if (this.searchConName !== '') {
                this.$Message.info('没有找到任何数据')
              } else {
                this.$Message.info('获取数据失败:' + res.data.message)
              }
            }
          })
      },
      loadKeys() {
        getKeys({type: 'ORG_TYPE'}).then(res => {this.orgTypes = res.data})
      },
      wrapSelectTree(){
        return [{
          id: 0,
          label: '根节点',
          expand: true,
          children: this.tableData
        }];
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
        saveOrg(this.entity).then(res => {
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
        deleteOrg(id).then(res => {
          console.log(res)
          this.$Message.info('删除成功!')
          this.list()
        }).catch(err => {
          console.log(err)
          this.$Message.info('删除失败!')
        })
      },
      createSubNode(parentId) {
        console.log('parentId=' + parentId)
        this.entity = this.emptyFormItem
        this.entity.parentId = parentId
        this.isShowSaveDrawer = true
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
      onTreeSelected(node, instanceId) {
        console.log(node);
        console.log(instanceId);
        console.log(this.entity)
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