<template>
  <div class="allActive">
    

    <el-card class="box-card">
      <h3>Project list</h3>


      {{ tableData }}
      <el-row>
      <el-button @click="handleRead" type="primary">Refresh</el-button>
      </el-row>
     
    </el-card>


  </div>
</template>
<script>
  import {namesRef} from 'src/initializeFirebse.js'

  export default {
    name: 'allActive',
    data: function () {
      return {
        totalActiveNum: 3,
        totalSignUp: 204,
        auditNum: 15,
        activeNum: 0,
        currentType: 'All',
        selectItems:[],
        types: ['All','Test activity','Free activity','Payment activity'],
        list_des:[],
        list_project:[],
        tableData: []
      }
    },
    computed:{
        filteredTableData: function () {
          var type = this.currentType;
          return this.tableData.filter(function (data) {
            if(type == 'All'|| type == '' ){
              return true
            }else{
              return data.type == type
            }
          })
        
      }
    },
    methods: {
      handleRead: function(){
          var _this = this;
          _this.tableData = [];
          _this.list_des = [];
          _this.list_project = [];
          namesRef.orderByKey().once('value', function(snap) {
            let res = snap.val();
            for (var uid in res){ //user id identify user who produced the data
              let description = res[uid].project_des;
              let project = res[uid].project_name;
              _this.list_des.push(JSON.stringify(description));
              _this.list_project.push(JSON.stringify(project));
            }
            var length = _this.list_des.length;
            for (var i = 0; i < length; i++) {
               _this.tableData.push({projectName:_this.list_project[i],projectDescription:_this.list_des[i]});
            }

          })
          return;
      },


      handleSelect: function (row, column, cell, event) {
       if (column.label == 'Cooperation') {
         this.$router.push('/activeManage/detail/page1');
        } else if(column.type == 'selection'){
            row.$info = !row.$selected;
       }else{
            row.$selected = !row.$selected;
            row.$info = row.$selected;
       }
      },
      selectionchange: function (val) {
        var arr = [];
        val.forEach(function (item) {
            arr.push(item.id);
        });
        this.selectItems = arr;
        this.activeNum = this.selectItems.length;
      },
      handleRemove:function(){
        var tableData = this.tableData;
        this.selectItems.forEach(function (id) {
          tableData.forEach(function (data) {
              if(id == data.id){
                tableData.splice(tableData.indexOf(data),1)
              }
          })
        });
        this.selectItems = [];
      },
    }
  }
</script>
<style>
  .allActive > .head > .el-col > .el-col {
    padding: 20px 0;
    border-right: solid 1px #fff;
  }

  .allActive .head {
    text-align: center;
    color: #fff;
    font-size: 30px;
    margin-bottom: 50px;
  }

  .allActive .head span {
    font-size: 16px;
  }
</style>
