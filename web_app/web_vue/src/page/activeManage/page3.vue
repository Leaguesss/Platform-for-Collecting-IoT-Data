<template>
  <div class="page3">
    <el-card class="box-card">



      <el-row :gutter="20" align="middle" type="flex">
        <el-col :span="10" >
          <el-input placeholder="Search by name or mobile" size="large" v-model="signInInput"></el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary"  size="large" @click.native="signIn">Confirm</el-button>
        </el-col>

      </el-row>


      <el-table :data="tableData">
        <el-table-column property="UID"    label="UID"     width="80"></el-table-column>
        <el-table-column property="way"     label="Category" width="120"></el-table-column>
        <el-table-column property="status"  label="Label"      width="100"></el-table-column>
        <el-table-column property="note"    label="Starting date"      width="150" show-tooltip-when-overflow></el-table-column>
        <el-table-column property="note"    label="Ending date"      width="150" show-tooltip-when-overflow></el-table-column>

      </el-table>


      <el-row  type="flex" align="middle" justify="end" style="padding: 20px 0;">
        <el-pagination
          @size-change=""
          @current-change=""
          :current-page="1"
          :page-sizes="[100, 200, 300, 400]"
          :page-size="100"
          layout="sizes, prev, pager, next"
          :total="totalItemsNum">
        </el-pagination>
      </el-row>
    </el-card>

  </div>
</template>

<script>
  import debounce from 'lodash.debounce'

  const testData = [
    {
      UID: '001',
      tel: '18825144569',
      way: 'working time',
      status: '1',
      note: '5.6'
    }, {
      UID: '001',
      tel: '18825144569',
      way: 'working time',
      status: '1',
      note: '5.6'

    }, {
      UID: '001',
      tel: '18825144569',
      way: 'working time',
      status: '1',
      note: '5.6'

    }, {
      UID: '001',
      tel: '18825144569',
      way: 'working time',
      status: '1',
      note: '5.6'

    }, {
      UID: '001',
      tel: '18825144569',
      way: 'working time',
      status: '1',
      note: '5.6'

    }
  ];
  export default {
    name:'page3',
    data: function () {
      return {
        searchText:'',
        signInInput:'',
        status:'全部',
        totalItemsNum:1000,
        tableData: '',
      }
    },
    methods: {
      filterBySearchText: function (val,data) {
        return data.filter(function (item) {
          return item.name.toLowerCase().match(val.toLowerCase()) || item.tel.match(val)
        })
      },
      filterByStatus: function (status,data) {
        return data.filter(function (item) {
          return (status=="全部") ? true : (item.status == status);
        })
      },
      filterByVal: function () {
        var data = this.filterByStatus(this.status,testData);
        this.tableData = this.searchText ? this.filterBySearchText(this.searchText,data):data;
      },
      tabClick: function (status) {
        this.status = status;
        this.filterByVal();
      },
      signIn: function () {
        console.log("sign");
      }
    },
    watch: {
      searchText: debounce(function (val, oldVal) {
        this.filterByVal();
      }, 500)
    },
    created: function () {
      this.tableData = testData ;
    }

  }


</script>

<style>
  .page3 .el-row{margin-bottom: 20px;}

</style>
