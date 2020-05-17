<template>
<div>
  <el-form   :model="pageForm" label-width="80px" :rules="pageFormRules" ref="pageForm">
    <el-form-item label="所属站点" prop="siteId">
      <el-select v-model="pageForm.siteId" placeholder="请选择站点">
        <el-option
          v-for="item in siteList"
          :key="item.siteId"
          :label="item.siteName"
          :value="item.siteId">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="模板名称" prop="templateName">
      <el-input v-model="pageForm.templateName" auto-complete="off" ></el-input>
    </el-form-item>
    <el-form-item label="模板参数" prop="templateParameter">
      <el-input v-model="pageForm.templateParameter" auto-complete="off" ></el-input>
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="addSubmit" >提交</el-button>
    <el-button type="primary" @click="go_back" >返回</el-button>
  </div>
</div>
</template>
<script>
  /*编写页面静态部分，即model及vm部分。*/
  import * as cmsApi from '../../api/cms'
  export default {
    data() {
      return {
        siteList:[],
        pageForm:{
          siteId:'',
          templateName:'',
          templateParameter:'',
        },
        pageFormRules: {
          siteId:[
            {required: true, message: '请选择站点', trigger: 'blur'}
          ],
          templateName:[
            {required: true, message: '请输入模板名称', trigger: 'blur'}
          ]
        }
      }
    },
    methods:{
      addSubmit:function(){
        this.$refs['pageForm'].validate((valid) => {
          if (valid) {//表单校验成功
            //确认提示
            this.$confirm('您确认提交吗?', '提示', { }).then(() => {
              //调用page_add方法请求服务端的新增页面接口
              cmsApi.template_add(this.pageForm).then(res=>{
                //解析服务端响应内容
                if(res.success){
                  this.$message.success("提交成功")
                  //将表单清空
                  this.$refs['pageForm'].resetFields();
                  this.go_back();
                }else if(res.message){
                  this.$message.error(res.message)
                }else{
                  this.$message.error("提交失败")
                }
              });
            })

          }
        });
      },
      //返回
      go_back:function () {
        this.$router.push({
          path:'/cms/template/list',
          query:this.$route.query
        })
      }
    },
    mounted(){
      //初始化站点列表
      cmsApi.site_getAll().then(res =>{
        this.siteList = res.list
      })

    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/
</style>
