<template>
  <div>
    <el-container>
      <el-aside width="200">
        <!-- 左侧导航-->
        <el-form :model="params">
          <el-input v-model="params.name" style="width: 100px"></el-input>
          <el-button type="primary" size="small" icon="el-icon-search" circle @click="query"></el-button>
          <el-button type="primary" size="small" @click="add">新增</el-button>
        </el-form>
        <el-table :data="list" stripe highlight-current-row
                  ref="lefTable"
                  @current-change="changeSelectRow">
          <el-table-column prop="name" label="名称" min-width="200">
          </el-table-column>
          <el-table-column label="操作" min-width="240">
            <template slot-scope="page">
              <el-button
                size="small" type="text"
                @click="addModel(page.row.id)">新增参数
              </el-button>
              <el-button
                size="small" type="text"
                @click="edit(page.row)">编辑
              </el-button>
              <el-button
                size="small" type="text"
                @click="del(page.row.id)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          layout="prev, pager, next"
          :total="total"
          :page-size="params.size"
          :current-page="params.page"
          @current-change="changePage"
          style="float:right;">
        </el-pagination>
      </el-aside>
      <el-main>
        <!-- 右侧数据配置项-->
        <el-table :data="modelList">
          <el-table-column prop="key" label="参数编号" min-width="80">
          </el-table-column>
          <el-table-column prop="name" label="参数名称" min-width="80">
          </el-table-column>
          <el-table-column prop="url" label="参数url" min-width="120">
          </el-table-column>
          <el-table-column prop="value" label="参数值" min-width="200">
          </el-table-column>
          <el-table-column label="操作" min-width="240">
            <template slot-scope="page">
              <el-button
                size="small" type="text"
                @click="editModel(page.row)">编辑
              </el-button>
              <el-button
                size="small" type="text"
                @click="delModel(page.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>

    <el-dialog :visible.sync="visible" title="参数信息">
      <el-form :model="modelForm" label-width="120px" :rules="modelFormRules" ref="modelForm">
        <el-form-item label="参数编号" prop="key">
          <el-input v-model="modelForm.key" auto-complete="off" :disabled="oprModel === 2"></el-input>
        </el-form-item>
        <el-form-item label="参数名称" prop="name">
          <el-input v-model="modelForm.name" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="参数url" prop="url">
          <el-input v-model="modelForm.url" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="参数值" prop="value">
          <el-input v-model="modelForm.value" type="textarea" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitModel">提交</el-button>
        <el-button type="primary" @click="go_back">返回</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as cmsApi from '../../api/cms'

  export default {
    data() {
      return {
        list: [],//左侧数据
        modelList: [],//右侧数据
        curRow: {},//当前选中行
        total: 0,
        params: {
          name: '',
          page: 1,
          size: 10
        },
        visible: false,
        modelForm: {
          key: '',
          name: '',
          url: '',
          value: ''
        },
        oprModel: 0,// 0 正常  1 新增 2 编辑
        modelFormRules: {
          key: [
            {required: true, message: '请输入参数编号', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '请输入参数名称', trigger: 'blur'}
          ]
        }
      }
    },
    watch: {
      // 通过监听实现table加载后自动选中第一行
      list: function () {
        this.$nextTick(function () {
          if (this.list && this.list.length) {
            this.$refs.lefTable.setCurrentRow(this.list[0])
          }
        })
      }
    },
    methods: {
      changeSelectRow: function (curRow, oldRow) {
        if(curRow){
          this.modelList = curRow.model;
          this.curRow = curRow;
        }else{
          this.curRow = {};
          this.modelList = [];
        }
      },
      query: function () {
        cmsApi.config_list(this.params.page, this.params.size, this.params).then(res => {
          this.list = res.queryResult.list;
          this.total = res.queryResult.total;
        })
      },
      changePage: function (page) {//形参就是当前页码
        //调用query方法
        // alert(page)
        this.params.page = page;
        this.query()
      },
      add: function () {
        this.$prompt('请输入名称', '新增', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValidator: (value => {
            if (value === null || value.length == 0) {
              return false;
            }
            return true;
          }),
          inputErrorMessage: '名称不可为空!'
        }).then(({value}) => {
          cmsApi.config_add({name: value}).then(res => {
            this.query()
          })
        });
      },
      edit: function (row) {
        this.$prompt('请输入名称', '修改', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: row.name,
          inputValidator: (value => {
            if (value === null || value.length == 0) {
              return false;
            }
            return true;
          }),
          inputErrorMessage: '名称不可为空!'
        }).then(({value}) => {
          cmsApi.config_edit(row.id, {name: value}).then(res => {
            if (res.success) {
              this.$message.success("修改成功")
              //将表单清空
              this.reLoadCurSelect(res.cmsConfig)
            } else if (res.message) {
              this.$message.error(res.message)
            } else {
              this.$message.error("修改失败")
            }
          })
        });
      },
      del: function (rowId) {
        this.$confirm('您确认删除吗?', '提示', {}).then(() => {
          cmsApi.config_del(rowId).then(res => {
            if (res.success) {
              this.$message.success("删除成功");
              //将表单清空
              this.query();
            } else if (res.message) {
              this.$message.error(res.message)
            } else {
              this.$message.error("删除失败")
            }
          });
        });
      },
      //提交表单
      submitModel: function () {
        this.$refs['modelForm'].validate((valid) => {
          if (valid) {//表单校验成功
            //确认提示
            this.$confirm('您确认提交吗?', '提示', {}).then(() => {
              if (this.oprModel == 1) {
                cmsApi.config_model_add(this.curRow.id, this.modelForm).then(res => {
                  if (res.success) {
                    this.$message.success("新增成功")
                    this.go_back()
                    //将表单清空
                    this.reLoadCurSelect(res.cmsConfig)
                  } else if (res.message) {
                    this.$message.error(res.message)
                  } else {
                    this.$message.error("新增失败")
                  }
                });
              } else if (this.oprModel == 2) {
                cmsApi.config_model_edit(this.curRow.id, this.modelForm.key, this.modelForm).then(res => {
                  if (res.success) {
                    this.$message.success("修改成功")
                    this.go_back()
                    //将表单清空
                    this.reLoadCurSelect(res.cmsConfig)
                  } else if (res.message) {
                    this.$message.error(res.message)
                  } else {
                    this.$message.error("修改失败")
                  }
                });
              }
            });
          }
        });
      },
      //新增参数
      addModel: function (row) {
        this.go_back();
        this.visible = true;
        this.oprModel = 1;
      },
      editModel: function (model) {
        this.go_back();
        this.visible = true;
        this.oprModel = 2;
        this.modelForm.key = model.key;
        this.modelForm.name = model.name;
        this.modelForm.url = model.url;
        this.modelForm.value = model.value;
      },
      delModel: function (model) {
        this.$confirm('您确认删除吗?', '提示', {}).then(() => {
          cmsApi.config_model_del(this.curRow.id, model.key).then(res => {
            if (res.success) {
              this.$message.success("删除成功")
              this.go_back()
              // 重新加载当前选中行数据
              //将表单清空
              this.reLoadCurSelect(res.cmsConfig)
            } else if (res.message) {
              this.$message.error(res.message)
            } else {
              this.$message.error("删除失败")
            }
          })
        });
      },
      go_back: function () {
        this.modelForm = {
          key: '',
          name: '',
          url: '',
          value: ''
        };
        this.visible = false;
        this.oprModel = 0;
      },
      reLoadCurSelect: function (cmsConfig) {
        this.curRow.model = cmsConfig.model;
        this.curRow.name = cmsConfig.name;
        this.modelList = this.curRow.model;
      }
    },
    mounted() {
      this.query()
    }
  }
</script>

<style scoped>

</style>
