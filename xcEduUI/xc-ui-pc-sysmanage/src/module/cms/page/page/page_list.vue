<template>
  <div>
    <!--编写页面静态部分，即view部分-->
    <!--查询表单-->
    <el-form :model="params">
      <el-select v-model="params.siteId" placeholder="请选择站点">
        <el-option
          v-for="item in siteList"
          :key="item.siteId"
          :label="item.siteName"
          :value="item.siteId">
        </el-option>
      </el-select>
      页面别名：
      <el-input v-model="params.pageAliase" style="width: 100px"></el-input>
      <el-button type="primary" size="small" icon="el-icon-search" circle v-on:click="query"></el-button>
      <el-button type="danger" size="small" icon="el-icon-delete" circle v-on:click="clearQuery"></el-button>
      <router-link :to="{path:'/cms/page/add',query:this.params}">
        <el-button type="primary" size="small">新增页面</el-button>
      </router-link>
      <el-button type="primary" size="small" @click="showLog = !showLog">
        <span v-if="!showLog">显示</span>
        <span v-else>隐藏</span>
        发布日志
      </el-button>
    </el-form>
    <el-container>
      <el-main>
        <el-table
          :data="list"
          stripe highlight-current-row
          @current-change="changeSelectRow"
          style="width: 100%">
          <el-table-column type="index" min-width="60">
          </el-table-column>
          <el-table-column prop="pageName" label="页面名称" min-width="200">
          </el-table-column>
          <el-table-column prop="pageAliase" label="别名" min-width="120">
          </el-table-column>
          <el-table-column prop="pageType" label="页面类型" min-width="80">
          </el-table-column>
          <el-table-column prop="pageWebPath" label="访问路径" min-width="160">
          </el-table-column>
          <el-table-column prop="pagePhysicalPath" label="物理路径" min-width="240">
          </el-table-column>
          <el-table-column label="操作" min-width="240">
            <template slot-scope="page">
              <el-button
                size="small" type="text"
                @click="edit(page.row.pageId)">编辑
              </el-button>
              <el-button
                size="small" type="text"
                @click="del(page.row.pageId)">删除
              </el-button>
              <el-button
                size="small" type="text"
                @click="previewPage(page.row.pageId)">预览界面
              </el-button>
              <el-button
                size="small" type="text"
                @click="deployPage(page.row.pageId)">发布
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          layout="prev, pager, next"
          :total="total"
          :page-size="params.size"
          :current-page="params.page"
          v-on:current-change="changePage"
          style="float:right;">
        </el-pagination>
      </el-main>
      <el-aside width="420px" v-if="showLog">
        <span content-position="left">发布日志</span>
        <el-table :data="logList"
                  style="width: 100%">
          <el-table-column prop="host" label="站点Ip" min-width="120">
            <template slot-scope="scope">
              <div :class="scope.row.success? 'success':'fail'">{{scope.row.host}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="msg" label="发布结果" min-width="200">
            <template slot-scope="scope">
              <div :class="scope.row.success? 'success':'fail'">{{scope.row.msg}}</div>
            </template>
          </el-table-column>
        </el-table>
      </el-aside>
    </el-container>
  </div>
</template>
<script>
  /*编写页面静态部分，即model及vm部分。*/
  import * as cmsApi from '../../api/cms'

  export default {
    data() {
      return {
        siteList: [],//站点列表
        list: [],
        total: 0,
        logList: [],
        showLog: false,
        params: {
          siteId: '',
          pageAliase: '',
          page: 1,
          size: 10
        }
      }
    },
    methods: {
      //页面查询
      query: function () {
        // alert('查询')
        //调用服务端的接口
        cmsApi.page_list(this.params.page, this.params.size, this.params).then((res) => {
          //将res结果数据赋值给数据模型对象
          this.list = res.queryResult.list;
          this.total = res.queryResult.total;
        })
      },
      clearQuery: function () {
        for (var p in this.params) {
          if (p !== 'page' && p !== 'size') {
            this.params[p] = ''
          }
        }
      },
      changePage: function (page) {//形参就是当前页码
        //调用query方法
        // alert(page)
        this.params.page = page;
        this.query()
      },
      edit: function (pageId) {
        //打开修改页面
        this.$router.push({
          path: '/cms/page/edit/' + pageId,
          query: this.params
        })
      },
      del: function (pageId) {
        this.$confirm('您确认删除吗?', '提示', {}).then(() => {
          //调用服务端接口
          cmsApi.page_del(pageId).then(res => {
            if (res.success) {
              this.$message.success("删除成功")
              //刷新页面
              this.query()
            } else {
              this.$message.error("删除失败")
            }
          })
        })
      },
      // 页面预览
      previewPage: function (pageId) {
        cmsApi.previewPage(pageId)
      },
      // 页面发布
      deployPage: function (pageId) {
        this.$confirm('您确认发布吗?', '提示', {}).then(() => {
          //调用服务端接口
          cmsApi.page_post(pageId).then(res => {
            if (res.success) {
              this.$message.success("发布成功")
              //刷新页面
              this.query()
            } else if (res.message) {
              this.$message.error(res.message)
            } else {
              this.$message.error("发布失败")
            }
          })
        })
      },
      //切换当前选中行
      changeSelectRow: function (curRow, oldRow) {
        cmsApi.page_logs_list(curRow.pageId).then(res => {
          this.logList = res.list
        })
      },
    },
    created() {
      //取出路由中的参数，赋值给数据对象
      this.params.page = Number.parseInt(this.$route.query.page || 1)
      this.params.siteId = this.$route.query.siteId || ''
      this.params.pageAliase = this.$route.query.pageAliase || ''
    },
    mounted() {
      //当DOM元素渲染完成后调用query
      this.query()
      //初始化站点列表
      cmsApi.site_getAll().then(res => {
        this.siteList = res.list
      })
    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/
  .success {
    color: green;
  }

  .fail {
    color: red;
  }
</style>
