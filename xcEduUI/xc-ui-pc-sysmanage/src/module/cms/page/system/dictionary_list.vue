<template>
  <div>
    <el-container>
      <el-aside width="360">
        <!-- 左侧导航-->
        <el-form :model="params">
          编号:
          <el-input v-model="params.dType" style="width: 120px"></el-input>
          名称:
          <el-input v-model="params.dName" style="width: 120px"></el-input>
          <el-button type="primary" size="small" icon="el-icon-search" circle @click="query"></el-button>
          <el-button type="primary" size="small" @click="add">新增</el-button>
        </el-form>
        <el-table :data="list" stripe highlight-current-row
                  ref="lefTable"
                  @current-change="changeSelectRow">
          <el-table-column prop="dType" label="编号" min-width="200">
          </el-table-column>
          <el-table-column prop="dName" label="名称" min-width="200">
          </el-table-column>
          <el-table-column label="操作" min-width="240">
            <template slot-scope="page">
              <el-button
                size="small" type="text"
                @click="addValue(page.row.id)">新增值
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
        <el-table :data="valueList">
          <el-table-column prop="sdId" label="编号" min-width="80">
          </el-table-column>
          <el-table-column prop="sdName" label="名称" min-width="80">
          </el-table-column>
          <el-table-column label="状态" min-width="120">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.sdStatus"
                active-color="#00A854"
                active-value="1"
                inactive-color="#F04134"
                inactive-value="0"
                @change="changeSwitch(scope.row)">
              </el-switch>
            </template>
          </el-table-column>
          </el-table-column>
          <el-table-column label="操作" min-width="240">
            <template slot-scope="page">
              <el-button
                size="small" type="text"
                @click="editValue(page.row)">编辑
              </el-button>
              <el-button
                size="small" type="text"
                @click="delValue(page.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>

    <el-dialog :visible.sync="visibleMain" title="参数信息">
      <el-form :model="mainForm" label-width="120px" :rules="mainFormRules" ref="mainForm">
        <el-form-item label="编号" prop="dType">
          <el-input v-model="mainForm.dType" auto-complete="off" :disabled="oprModel === 2"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="dName">
          <el-input v-model="mainForm.dName" auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button type="primary" @click="go_back">返回</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="visible" title="参数信息">
      <el-form :model="valueForm" label-width="120px" :rules="valueFormRules" ref="valueForm">
        <el-form-item label="编号" prop="sdId">
          <el-input v-model="valueForm.sdId" auto-complete="off" :disabled="oprModel === 2"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="sdName">
          <el-input v-model="valueForm.sdName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="sdStatus">
          <el-switch v-model="valueForm.sdStatus"
                     active-color="#00A854"
                     active-value="1"
                     inactive-color="#F04134"
                     inactive-value="0"></el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitValue">提交</el-button>
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
        valueList: [],//右侧数据
        curRow: {},//当前选中行
        total: 0,
        params: {
          dName: '',
          dType: '',
          page: 1,
          size: 10
        },
        visibleMain: false,
        mainForm: {
          dType: '',
          dName: '',
        },
        mainFormRules: {
          dType: [
            {required: true, message: '请输入编号', trigger: 'blur'}
          ],
          dName: [
            {required: true, message: '请输入名称', trigger: 'blur'}
          ]
        },
        visible: false,
        valueForm: {
          sdId: '',
          sdName: '',
          sdStatus: '1'
        },
        oprModel: 0,// 0 正常  1 新增 2 编辑
        valueFormRules: {
          sdId: [
            {required: true, message: '请输入值编号', trigger: 'blur'}
          ],
          sdName: [
            {required: true, message: '请输入值名称', trigger: 'blur'}
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
        if (curRow) {
          this.valueList = curRow.dValue;
          this.curRow = curRow;
        } else {
          this.curRow = {};
          this.valueList = [];
        }

      },
      query: function () {
        cmsApi.sys_dictionary_list(this.params.page, this.params.size, this.params).then(res => {
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
        this.go_back();
        this.visibleMain = true;
        this.oprModel = 1;
      },
      edit: function (row) {
        this.go_back();
        this.visibleMain = true;
        this.oprModel = 2;
        this.mainForm.dType = row.dType;
        this.mainForm .dName = row.dName;
      },
      del: function (rowId) {
        this.$confirm('您确认删除吗?', '提示', {}).then(() => {
          cmsApi.sys_dictionary_del(rowId).then(res => {
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
      submit: function () {
        this.$refs['mainForm'].validate((valid) => {
          if (valid) {//表单校验成功
            //确认提示
            this.$confirm('您确认提交吗?', '提示', {}).then(() => {
              if (this.oprModel == 1) {
                cmsApi.sys_dictionary_add(this.mainForm).then(res => {
                  if (res.success) {
                    this.$message.success("新增成功")
                    this.go_back()
                    //将表单清空
                    this.query();
                  } else if (res.message) {
                    this.$message.error(res.message)
                  } else {
                    this.$message.error("新增失败")
                  }
                });
              } else if (this.oprModel == 2) {
                console.log(this.curRow)
                cmsApi.sys_dictionary_edit(this.curRow.id, this.mainForm).then(res => {
                  if (res.success) {
                    this.$message.success("修改成功")
                    this.go_back()
                    //将表单清空
                    this.reLoadCurSelect(res.sysDictionary)
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
      //提交表单
      submitValue: function () {
        this.$refs['valueForm'].validate((valid) => {
          if (valid) {//表单校验成功
            //确认提示
            this.$confirm('您确认提交吗?', '提示', {}).then(() => {
              if (this.oprModel == 1) {
                cmsApi.sys_dictionary_value_add(this.curRow.id, this.valueForm).then(res => {
                  if (res.success) {
                    this.$message.success("新增成功")
                    this.go_back()
                    //将表单清空
                    this.reLoadCurSelect(res.sysDictionary)
                  } else if (res.message) {
                    this.$message.error(res.message)
                  } else {
                    this.$message.error("新增失败")
                  }
                });
              } else if (this.oprModel == 2) {
                cmsApi.sys_dictionary_value_edit(this.curRow.id, this.valueForm.sdId, this.valueForm).then(res => {
                  if (res.success) {
                    this.$message.success("修改成功")
                    this.go_back()
                    //将表单清空
                    this.reLoadCurSelect(res.sysDictionary)
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
      addValue: function (row) {
        this.go_back();
        this.visible = true;
        this.oprModel = 1;
      },
      editValue: function (dValue) {
        this.go_back();
        this.visible = true;
        this.oprModel = 2;
        this.valueForm.sdId = dValue.sdId;
        this.valueForm.sdName = dValue.sdName;
        this.valueForm.sdStatus = dValue.sdStatus;
      },
      delValue: function (dValue) {
        this.$confirm('您确认删除吗?', '提示', {}).then(() => {
          cmsApi.sys_dictionary_value_del(this.curRow.id, dValue.sdId).then(res => {
            if (res.success) {
              this.$message.success("删除成功")
              this.go_back()
              // 重新加载当前选中行数据
              //将表单清空
              this.reLoadCurSelect(res.sysDictionary)
            } else if (res.message) {
              this.$message.error(res.message)
            } else {
              this.$message.error("删除失败")
            }
          })
        });
      },
      go_back: function () {
        this.valueForm = {
          sdId: '',
          sdName: '',
          sdStatus:'1'
        };
        this.mainForm = {
          dName: '',
          dType: ''
        };
        this.visibleMain = false;
        this.visible = false;
        this.oprModel = 0;
      },
      reLoadCurSelect: function (sysDictionary) {
        this.curRow.dValue = sysDictionary.dValue;
        this.curRow.name = sysDictionary.name;
        this.valueList = this.curRow.dValue;
      },
      changeSwitch: function (row) {
        cmsApi.sys_dictionary_value_edit(this.curRow.id,row.sdId,row).then(res =>{
          this.reLoadCurSelect(res.sysDictionary)
        });
      }
    },
    mounted() {
      this.query()
    }
  }
</script>

<style scoped>

</style>
