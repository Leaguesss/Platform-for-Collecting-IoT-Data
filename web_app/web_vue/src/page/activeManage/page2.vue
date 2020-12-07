<template>
  <div class="page2">
      <el-row :gutter="20" align="middle" type="flex" justify="start" style="margin-bottom: 20px;">
        <el-col :span="3">
          Email address:
        </el-col>
        <el-col :span="8">
          <input class="input_box" placeholder=" email address" size="large" v-model="email">        
        </el-col>
      </el-row>

      <el-row :gutter="20" align="middle" type="flex" justify="start" style="margin-bottom: 20px;">
        <el-col :span="3">
          Password:
        </el-col>
        <el-col :span="8">
          <input type="password"  class="input_box" placeholder=" Passwords" v-model="userPassword">
        </el-col>
      </el-row>

      <el-row :gutter="20" align="middle" type="flex" justify="start" style="margin-bottom: 20px;">
        <el-col :span="3">
          Confirm password:
        </el-col>
        <el-col :span="8">
          <input type="password"  class="input_box" placeholder=" Double check passwords" v-model="doublePassword">
        </el-col>
      </el-row>

      <el-row :gutter="20" align="middle" type="flex" justify="start" style="margin-bottom: 20px;">
        <el-col :span="3">
          Organisation:
        </el-col>
        <el-col :span="8">
          <input class="input_box" placeholder=" Institute, company or etc" size="large" v-model="organisation">
        </el-col>
      </el-row>

      <el-row :gutter="20" align="middle" type="flex" style="padding: 20px;">

          <el-button class="Cancel_btn" @click.native.prevent="handleCancel" >Cancel</el-button>
          <el-button class="Register_btn" @click.native.prevent="handleRegister">Register</el-button>

      </el-row>


  </div>
</template>

<script>
  /* Test people */
const testData = [
  {
    name: 'Allen',
    tel: '18825144569',
    way: 'URL',
    status: 'Waiting payment',
    note: '~~'
  }, {
    name: 'Allen',
    tel: '13480278079',
    way: 'URL',
    status: 'Closed',
    note: '!!'
  }, {
    name: 'Allen',
    tel: '18825188888',
    way: 'URL',
    status: 'Passed',
    note: '@@'
  }, {
    name: 'Allen',
    tel: '17070897917',
    way: 'URL',
    status: 'Refunding',
    note: '##'
  }, {
    name: 'Allen',
    tel: '12414389677',
    way: 'URL',
    status: 'Awaiting moderation',
    note: '$$'
  }
];
  import store from 'src/store.js'
  import $ from 'jquery'
  import debounce from 'lodash.debounce'
  export default {
    name: 'page2',
    data: function () {
      return {
        status:'All',
        selectNum: 0,
        totalItemsNum:1000,
        searchText: '',
        sourceDate:[],
        tableData:[],
        multipleSelection: []
      }
    },
    computed:{

    },
    methods: {
      handleSelect: function (row, column, cell, event) {
        if (column.label == 'operation') {
          this.$router.push('/activeManage/detail/page1');
        } else if (column.type == 'selection') {
          row.$info = !row.$selected;
        } else {
          row.$selected = !row.$selected;
          row.$info = row.$selected;
        }
      },

      handleCancel: function(){
        this.$router.push('/activePublic/step1');
      },

      handleRegister: function(){
        // const that = this;
        if(this.email == null){
          this.$message.error('Please input email');
          return;
        }else if (!/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(this.email)){
          this.$message.error('Invalid email');
          return;
        }else if(this.userPassword == null){
          this.$message.error('Please input Password');
          return;
        }else if(this.userPassword.length < 8){
          this.$message.error('User password at least 8 charcters');
          return;
        }else if(this.doublePassword == null){
          this.$message.error('Please input Password again');
          return;
        }else if(this.userPassword != this.doublePassword){
          this.$message.error('Second password not macht with first');
          return;
        }else{

          this.$axios.post("/api/researcherAuth/register", 
            this.qs.stringify({
              email: this.email,
              password: this.userPassword,
              organisation: this.organisation
            })).then(
              res => {
                this.$router.push('/activePublic/step1');
                alert('Register successfully, please login');
                localStorage.setItem('password',this.userPassword);
                localStorage.setItem('email',this.email);
                localStorage.setItem('organisation',this.organisation);
              },
              err => {
                alert(err);
              }
            ) 
        }
      },

      selectionchange: function (val) {
        var arr = [];
        val.forEach(function (item) {
          arr.push(item.id);
        });
        this.selectItems = arr;
        this.selectNum = this.selectItems.length;
      },
      filterBySearchText: function (val,data) {
        return data.filter(function (item) {
          return item.name.toLowerCase().match(val.toLowerCase()) || item.tel.match(val)
        })
      },
      filterByStatus: function (status,data) {
        return data.filter(function (item) {
          return (status=="All") ? true : (item.status == status);
        })
      },
      filterByVal: function () {
        var data = this.filterByStatus(this.status,testData);
        this.tableData = this.searchText ? this.filterBySearchText(this.searchText,data):data;
      },
      tabClick: function (status) {
        this.status = status;
        this.filterByVal();
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
  .page2{min-width: 1200px;}
  .page2 .qxs-ic_user {
    background: url("../../assets/login/ic_user.png") no-repeat;
    background-size: 15px 15px;
    background-position: 3%;
  }
  .page2 .qxs-ic_password {
    background: url("../../assets/login/ic_password.png") no-repeat;
    background-size: 15px 15px;
    background-position: 3%;
    margin-bottom: 20px;
  }
  .page2 .qxs-icon {
    height: 40px;
    width: 90%;
    margin-bottom: 5px;
    padding-left: 10%;
    border: 0;
    border-bottom: solid 1px lavender;
  }
  .page2 .input_box {
    height:37px;
    width: 700px;
    border: 1px outset;
  }
</style>
