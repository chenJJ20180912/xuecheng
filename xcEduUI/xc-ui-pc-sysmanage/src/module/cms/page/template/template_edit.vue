<template>
  <div>
    <el-form :model="pageForm" label-width="80px" :rules="pageFormRules" ref="pageForm">
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
        <el-input v-model="pageForm.templateName" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="模板参数" prop="templateParameter">
        <el-input v-model="pageForm.templateParameter" auto-complete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="go_back">返回</el-button>
      <el-button type="primary" @click.native="editSubmit" :loading="addLoading">提交</el-button>
    </div>
  </div>
</template>
<script>
  import * as cmsApi from '../../api/cms'

  export default {
    data() {
      return {
        siteList: [],
        //页面id
        templateId: '',
        //模版列表
        addLoading: false,//加载效果标记
        //新增界面数据
        pageForm: {
          siteId: '',
          templateId: '',
          templateName: '',
          templateParameter: ''
        },
        pageFormRules: {
          siteId: [
            {required: true, message: '请选择站点', trigger: 'blur'}
          ],
          templateName: [
            {required: true, message: '请输入模板名称', trigger: 'blur'}
          ]
        },

      }
    },
    methods: {
      go_back() {
        this.$router.push({
          path: '/cms/template/list', query: this.$route.query
        })
      },
      editSubmit() {
        this.$refs.pageForm.validate((valid) => {//表单校验
          if (valid) {//表单校验通过
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.addLoading = true;
              //修改提交请求服务端的接口
              cmsApi.template_edit(this.templateId, this.pageForm).then((res) => {
                if (res.success) {
                  this.addLoading = false;
                  this.$message({
                    message: '提交成功',
                    type: 'success'
                  });
                  this.$refs['pageForm'].resetFields();
                  //返回
                  this.go_back();
                } else {
                  this.addLoading = false;
                  this.$message.error('提交失败');
                }
              });
            });
          }
        });
      }
    },
    created: function () {
      this.templateId = this.$route.params.templateId;
      //根据主键查询页面信息
      cmsApi.template_get(this.templateId).then((res) => {
        if (res) {
          this.pageForm = res.cmsTemplate;
        }
      });
    },
    mounted: function () {
      //初始化站点列表
      cmsApi.site_getAll().then(res => {
        this.siteList = res.list
      })
    }
  }
</script>
<style>

</style>
