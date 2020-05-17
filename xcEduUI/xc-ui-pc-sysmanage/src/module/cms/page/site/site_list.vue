<template>
  <div>
    <!--编写页面静态部分，即view部分-->
    <!--查询表单-->
    <el-form :model="params">
      站点名称：<el-input v-model="params.siteName"  style="width: 100px"></el-input>
      站点域名：<el-input v-model="params.siteDomain"  style="width: 100px"></el-input>
      站点端口：<el-input v-model="params.sitePort"  style="width: 100px"></el-input>
      站点访问地址：<el-input v-model="params.siteWebPath"  style="width: 100px"></el-input>
      <el-button type="primary" size="small" icon="el-icon-search" circle  v-on:click="query"></el-button>
      <el-button type="danger" size="small" icon="el-icon-delete" circle v-on:click="clearQuery"></el-button>
      <router-link :to="{path:'/cms/site/add',query:this.params}">
         <el-button  type="primary" size="small">新增站点</el-button>
      </router-link>
    </el-form>

    <el-table
      :data="list"
      stripe highlight-current-row
      style="width: 100%">
      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="siteName" label="站点名称" min-width="200">
      </el-table-column>
      <el-table-column prop="siteDomain" label="站点域名" min-width="240">
      </el-table-column>
      <el-table-column prop="sitePort" label="站点端口" min-width="150">
      </el-table-column>
      <el-table-column prop="sitePhysicalPath" label="物理路径" min-width="320">
      </el-table-column>
      <el-table-column prop="siteWebPath" label="访问路径" min-width="120">
      </el-table-column>
      <el-table-column prop="siteCreateTime" label="创建时间" min-width="180" >
      </el-table-column>
      <el-table-column label="操作" min-width="240">
        <template slot-scope="page">
          <el-button
            size="small"type="text"
            @click="edit(page.row.siteId)">编辑
          </el-button>
          <el-button
            size="small"type="text"
            @click="del(page.row.siteId)">删除
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
  </div>
</template>
<script>
  /*编写页面静态部分，即model及vm部分。*/
  import * as cmsApi from '../../api/cms'
  export default {
    data() {
      return {
        list: [],
        total:0,
        params:{
          siteName:'',
          siteDomain:'',
          sitePort:'',
          siteWebPath:'',
          page:1,
          size:10
        }
      }
    },
    methods:{
      //页面查询
      query:function(){
        // alert('查询')
        //调用服务端的接口
        cmsApi.site_list(this.params.page,this.params.size,this.params).then((res)=>{
          //将res结果数据赋值给数据模型对象
          this.list = res.queryResult.list;
          this.total = res.queryResult.total;
        })

      },
      clearQuery:function(){
        for(var p in this.params){
          if(p !== 'page' && p !== 'size'){
            this.params[p] = ''
          }
        }
      },
      changePage:function(page){//形参就是当前页码
        //调用query方法
        // alert(page)
        this.params.page = page;
        this.query()
      },
      edit:function(siteId){
        //打开修改页面
        this.$router.push({
          path:'/cms/site/edit/'+siteId,
          query:this.params
        })
      },
      del:function (siteId) {
        this.$confirm('您确认删除吗?', '提示', { }).then(() => {
          //调用服务端接口
          cmsApi.site_del(siteId).then(res=>{
            if(res.success){
              this.$message.success("删除成功")
              //刷新页面
              this.query()
            }else{
              this.$message.error("删除失败")
            }
          })
        })

      }
    },
    created(){
      //取出路由中的参数，赋值给数据对象
      this.params.page = Number.parseInt(this.$route.query.page || 1)
      this.params.siteName = this.$route.query.siteName || ''
      this.params.siteDomain = this.$route.query.siteDomain || ''
      this.params.sitePort = this.$route.query.sitePort || ''
      this.params.siteWebPath = this.$route.query.siteWebPath || ''

    },
    mounted(){
      //当DOM元素渲染完成后调用query
      this.query()
    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/
</style>
