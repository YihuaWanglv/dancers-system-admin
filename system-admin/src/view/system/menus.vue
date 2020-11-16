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
        <Table :columns="columns" :data="tableData" row-key="menuId" border stripe highlight-row>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="default" size="small" style="margin-right: 3px" @click="createSubNode(row.menuId)"> + </Button>
            <Button type="primary" size="small" style="margin-right: 3px" @click="showDetail(row)">详细</Button>
            <Button type="primary" size="small" style="margin-right: 3px" @click="update(row)">编辑</Button>
            <Button type="error" size="small" @click="deleteConfirm(row.menuId)">删除</Button>
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
          <Row>菜单Key: {{entity.menuKey}}</Row>
          <Row>菜单名称: {{entity.menuName}}</Row>
          <!-- <Row>上级菜单: {{entity.parentId}}</Row> -->
          <Row>菜单类型: {{entity.menuTypeName}}</Row>
          <!-- <Row>菜单图标: {{entity.menuIcon}}</Row> -->
          <Row>排序号: {{entity.sort}}</Row>
          <Row>状态: {{convert.valueOfStatus(entity.state)}}</Row>
          <Row>菜单路径: {{entity.menuPath}}</Row>
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
            <FormItem label="菜单Key"><Input v-model="entity.menuKey" placeholder="菜单Key" /></FormItem>
            <FormItem label="菜单名称"><Input v-model="entity.menuName" placeholder="菜单名称" /></FormItem>
            <FormItem label="上级菜单">
              <treeselect name="roleMenuSelect" v-model="entity.parentId" :multiple="false" :options="wrapSelectTree()" :show-count="true" :default-expand-level="2"
                placeholder="请选择菜单上级菜单" @select="onTreeSelected" noChildrenText="该菜单下暂无子菜单" :autoLoadRootOptions="false"
                noOptionsText="没有可供选择的菜单，请先录入菜单" noResultsText="没有找到查询的菜单" />
            </FormItem>
            <FormItem label="菜单类型">
              <Select v-model="entity.menuType">
                <Option v-for="item in menuTypes" :value="item.key" :key="item.key">{{ item.alias }}</Option>
              </Select>
            </FormItem>
            <FormItem label="排序号"><Input v-model="entity.sort" placeholder="排序号" /></FormItem>
            <FormItem label="状态">
              <RadioGroup v-model="entity.state">
                <Radio :label="1">启用</Radio>
                <Radio :label="0">禁用</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="菜单路径"><Input v-model="entity.menuPath" placeholder="菜单路径" /></FormItem>
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
  import { getMenu, getMenus, saveMenu, deleteMenu } from '@/api/menu'
  export default {
    name: 'system-menus',
    components: { Treeselect },
    data() {
      return {
        convert: convert,
        isShowDetailDrawer: false,
        isShowSaveDrawer: false,
        searchConName: '',
        menuTypes: [],
        entity: {
          menuId: '',
          parentId: '',
          menuType: '',
          menuTypeName: '',
          sort: '',
          state: '',
          menuPath: '',
          menuKey: '',
          menuName: ''
        },
        emptyFormItem: {
          menuId: '',
          parentId: '',
          menuType: '',
          menuTypeName: '',
          sort: '',
          state: '',
          menuPath: '',
          menuKey: '',
          menuName: ''
        },
        columns: [{
          tree: true,
          width: 180,
          key: 'menuName',
          title: '菜单名称'
        }, {
          key: 'menuKey',
          title: '菜单Key'
        }, {
          key: 'menuTypeName',
          width: 100,
          title: '菜单类型'
        }, {
          key: 'sort',
          width: 80,
          title: '排序号'
        }, {
          key: 'state',
          width: 80,
          title: '状态',
          render: (h, params) => {
            if (params.row.state == '1') { return h('Span', '启用') }
            if (params.row.state == '0') { return h('Span', '禁用') }
            return h('Span', '')
          }
        }, {
          key: 'menuPath',
          title: '菜单路径'
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
        getMenus(param).then(res => {
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
        getKeys({type: 'MENU_TYPE'}).then(res => {this.menuTypes = res.data})
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
        saveMenu(this.entity).then(res => {
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
        deleteMenu(id).then(res => {
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