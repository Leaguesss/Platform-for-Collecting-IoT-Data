<template>
  <div class="step3">

    <el-form ref="signForm" :model="signForm" label-position="top" @submit.prevent="onSubmit">
            <el-row :gutter="20" >
                <el-col :span="10">
                      <p>User email: {{ email }}</p>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="10">
                      <p>User occupation: {{ occupation }}</p>
                </el-col>

            </el-row>
    </el-form>


  </div>
</template>
<script>
  import store from 'src/store.js'

  export default {
    name: 'step3',
    data: function () {
      return {
        email: localStorage.getItem('userEmail'),
        occupation: localStorage.getItem('userOccupation'),
        imgUrl: '',
        signForm: {
          signUpLimit: 'unlimit',
          numLimit: 'unlimit',
          numLimitDetail: '',
          cost: 'Free',
          costDetail: '',
          cancel: 'No',
          audit: 'No',
          needName: true,
          needTel: true,
          signFormList: [
            {title: 'name', require: true},
            {title: 'mobile', require: true}
          ],
          sign: 'Have to sign',
          secretCode: ''
        }
      }
    },
    watch: {
      signForm: {
        handler: function () {
          store.commit('setSignForm', this.signForm);
        },
        deep: true
      }
    },
    methods: {
      onSubmit: function () {
        console.log('submit!');
      },
      addItem: function () {
        var n = this.signForm.signFormList ? this.signForm.signFormList.length + 1 : 1;
        this.signForm.signFormList.push({
          title: 'title' + n,
          require: false
        });
      },
      removeItem: function (item) {
        var index = this.signForm.signFormList.indexOf(item);
        this.signForm.signFormList.splice(index, 1);
      },

      moveTop: function (item) {
        var index = this.signForm.signFormList.indexOf(item);
        if(index != 0){
          this.signForm.signFormList.splice(index,1);
          this.signForm.signFormList.splice(0,0,item);
        }
      },
      moveUp: function (item) {
        var index = this.signForm.signFormList.indexOf(item);
        if(index != 0){
        this.signForm.signFormList.splice(index,1);
        this.signForm.signFormList.splice(index-1,0,item);
        }
      },
      moveDown: function (item) {
        var index = this.signForm.signFormList.indexOf(item);
        var max = this.signForm.signFormList.length ;
        if(index != max){
          this.signForm.signFormList.splice(index,1);
          this.signForm.signFormList.splice(index+1,0,item);
        }
      }
    },
    created: function () {
      Object.assign(this.signForm, store.state.signForm);
    }
  }
</script>
<style>
  .step3 .setSign .el-row{line-height: 40px;margin-bottom: 10px;}
  .step3 .title{text-align: right;}
  .step3 .router-link{color:#333;}
  .step3 .el-button-primary .router-link{color:#fff;}
  /* size of dialog */
  .step3 .el-dialog--small{min-width: 400px;max-width: 500px;}

  .list-enter-active, .list-leave-active {
    transition: all 1s;
  }
  .list-enter, .list-leave-active {
    opacity: 0;
    transform: translateY(30px);
  }

</style>
