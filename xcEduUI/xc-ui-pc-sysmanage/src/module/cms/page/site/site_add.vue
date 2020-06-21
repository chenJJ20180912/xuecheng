<template>
  <div>
    <el-form :model="pageForm" label-width="80px" :rules="pageFormRules" ref="pageForm">
      <el-form-item label="站点名称" prop="siteName">
        <el-input v-model="pageForm.siteName" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="站点域名" prop="siteDomain">
        <el-input v-model="pageForm.siteDomain" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="站点端口" prop="sitePort">
        <el-input v-model="pageForm.sitePort" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item prop="sitePhysicalPath" label="物理路径">
        <el-input v-model="pageForm.sitePhysicalPath" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="访问路径" prop="siteWebPath">
        <el-input v-model="pageForm.siteWebPath" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker type="datetime" placeholder="创建时间" v-model="pageForm.pageCreateTime"></el-date-picker>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="addSubmit">提交</el-button>
      <el-button type="primary" @click="go_back">返回</el-button>
    </div>
  </div>
</template>
<script>
  /*编写页面静态部分，即model及vm部分。*/
  import * as cmsApi from '../../api/cms'

  export default {
    data() {
      return {
        pageForm: {
          siteName: '',
          siteDomain: '',
          sitePort: '',
          siteWebPath: '',
          sitePhysicalPath:'',
          siteCreateTime: new Date()
        },
        pageFormRules: {
          siteName: [
            {required: true, message: '请输入站点名称', trigger: 'blur'}
          ],
          siteDomain: [
            {required: true, message: '请输入站点域名', trigger: 'blur'}
          ],
          sitePort: [
            {required: true, message: '请输入站点端口', trigger: 'blur'}
          ],
          siteWebPath: [
            {required: true, message: '请输入访问路径', trigger: 'blur'}
          ],
          sitePhysicalPath: [
            {required: true, message: '请输入物理路径', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      addSubmit: function () {
        this.$refs['pageForm'].validate((valid) => {
          if (valid) {//表单校验成功
            //确认提示
            this.$confirm('您确认提交吗?', '提示', {}).then(() => {
              //调用page_add方法请求服务端的新增页面接口
              cmsApi.site_add(this.pageForm).then(res => {
                //解析服务端响应内容
                if (res.success) {
                  this.$message.success("提交成功")
                  //将表单清空
                  this.$refs['pageForm'].resetFields();
                  this.go_back();
                } else if (res.message) {
                  this.$message.error(res.message)
                } else {
                  this.$message.error("提交失败")
                }
              });
            })

          }
        });
      },
      //返回
      go_back: function () {
        this.$router.push({
          path: '/cms/site/list',
          query: this.$route.query
        })
      }
    },
    mounted() {

    }
  }
</script>
<style>
  /*编写页面样式，不是必须*/
</style>
