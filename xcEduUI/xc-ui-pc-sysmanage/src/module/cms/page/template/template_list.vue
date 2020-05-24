<template>
  <div>
    <el-container>
      <el-aside width="880px">
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
          模板名称：
          <el-input v-model="params.templateName" style="width: 100px"></el-input>
          <el-button type="primary" size="small" icon="el-icon-search" circle v-on:click="query"></el-button>
          <el-button type="danger" size="small" icon="el-icon-delete" circle v-on:click="clearQuery"></el-button>
          <router-link :to="{path:'/cms/template/add',query:this.params}">
            <el-button type="primary" size="small">新增模板</el-button>
          </router-link>
        </el-form>
        <el-table
          :data="list" ref="listForm"
          stripe highlight-current-row @current-change="changeRow"
          style="width: 100%">
          <el-table-column type="index" min-width="60">
          </el-table-column>
          <el-table-column prop="templateName" label="模板名称" min-width="280">
          </el-table-column>
          <el-table-column prop="templateParameter" label="模板参数" min-width="220">
          </el-table-column>
          <el-table-column label="操作" min-width="120">
            <template slot-scope="page">
              <el-button
                size="small" type="text"
                @click="edit(page.row.templateId)">编辑
              </el-button>
              <el-button
                size="small" type="text"
                @click="del(page.row.templateId)">删除
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="模板" width="160">
            <template slot-scope="page">
              <el-upload
                :limit="1"
                ref="upload"
                accept=".ftl"
                :action="uploadUrl + page.row.templateId"
                :on-success="uploadSuccess"
                style="float:left;" :show-file-list="false" >
                <el-button
                  size="small" type="text">上传
                </el-button>
              </el-upload>
              <el-button v-if="page.row.templateFileId" size="small" type="text" style="float:left;margin-left:10px;"
                         @click="downTemplate(page.row)">下载
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
          style="float:right">
        </el-pagination>
      </el-aside>
      <el-main>
        <el-header style="text-align: left; font-size: 18px">
          模板内容预览
        </el-header>
        <el-input
          type="textarea"
          placeholder="文件内容"
          :rows="30"
          v-model="fileContent">
        </el-input>
      </el-main>
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
        fileList: [],
        total: 0,
        fileContent: '',
        uploadUrl: cmsApi.template_uploadFile_URL(),
        params: {
          siteId: '',
          templateName: '',
          templateParameter: '',
          page: 1,
          size: 10
        }
      }
    },
    watch: {
      // 通过监听实现table加载后自动选中第一行
      list: function() {
        this.$nextTick(function() {
          if(this.list && this.list.length){
            this.$refs.listForm.setCurrentRow(this.list[0])
          }
        })
      }
    },
    methods: {
      //页面查询
      query: function () {
        // alert('查询')
        //调用服务端的接口
        cmsApi.template_list(this.params.page, this.params.size, this.params).then((res) => {
          //将res结果数据赋值给数据模型对象
          this.list = res.queryResult.list;
          this.total = res.queryResult.total;
          this.templateId = ''
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
      edit: function (templateId) {
        //打开修改页面
        this.$router.push({
          path: '/cms/template/edit/' + templateId,
          query: this.params
        })
      },
      del: function (templateId) {
        this.$confirm('您确认删除吗?', '提示', {}).then(() => {

          //调用服务端接口
          cmsApi.template_del(templateId).then(res => {
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
      // 加载文件信息到右侧文本框内
      changeRow: function (newRow, oldRow) {
        if (newRow) {
          cmsApi.template_queryFile(newRow.templateId).then(res => {
            if (res.list && res.list.length) {
              this.fileContent = res.list[0]
            } else {
              this.fileContent = ''
            }
          })
        } else {
          this.fileContent = ''
        }
      },
      // 上传之前的处理
      uploadSuccess: function (res) {
        if(res.success){
          this.$message.success('上传成功!')
        }else if(res.message){
          this.$message.error(res.message)
        }else{
          this.$message.error("上传失败")
        }
      },
      // 下载模板
      downTemplate(template) {
        cmsApi.template_downloadFile(template.templateId, template.templateName);
      }
    },
    created() {
      //取出路由中的参数，赋值给数据对象
      this.params.page = Number.parseInt(this.$route.query.page || 1)
      this.params.templateName = this.$route.query.templateName || ''

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
</style>
