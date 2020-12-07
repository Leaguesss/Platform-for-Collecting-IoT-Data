<template>
  <div class="step1">

    <div class="outer_label" v-show="Login">
      <img class="inner_label login_logo" src="../../../static/Logo.png">
    </div>
    <div class="login_form">
      <input type="text"  class="qxs-ic_user qxs-icon" v-show="Login" placeholder="User name" v-model="userName">
      <input type="text"  class="qxs-ic_password qxs-icon" v-show="Login"  placeholder="Passwords" v-model="password">
      <div style="margin-left: 80px; margin-top: 10px">
        <el-button class="login_btn" @click.native.prevent="login" v-show="Login" type="primary" round :loading="isBtnLoading">Login</el-button>
        <el-button class="login_btn" @click.native="login" v-show="Login" round :loading="isBtnLoading">Register</el-button>
        <el-button class="login_btn" @click.native="login" v-show="Login" round :loading="isBtnLoading">Forgot your passwords</el-button>
      </div>
    </div>
        
  </div>
</template>



<script>
    import store from 'src/store.js'
    export default {
      data() {
        return {
          userName: '',
          password: '',
          isBtnLoading: false,
          step: 1,
          Login: false,
          nextStep: false
        }
      },
      created () {
        if(JSON.parse( localStorage.getItem('user')) && JSON.parse( localStorage.getItem('user')).userName){
          this.userName = JSON.parse( localStorage.getItem('user')).userName;
          this.password = JSON.parse( localStorage.getItem('user')).password;
        }
      },
      computed: {
        btnText() {
          if (this.isBtnLoading) return 'Be logging in...';
          return 'Login';
        }
      },
      methods: {
        login() {
          if (!this.userName) {
            this.$message.error('Please input user name');
            return;
          }
          if (!this.password) {
            this.$message.error('Please enter the password');
            return;
          }
          else{
            this.$router.push('/activePublic/step'+(this.step+1));
            var _this = this;
            setTimeout(function () {
                if(_this.isRouter){
                    _this.step++;
                    _this.goStep(_this.step);
                }
            })
            $('html,body').animate({scrollTop:0},500);
          }
        },
        handleNextStep: function () {
            this.$router.push('/activePublic/step'+(this.step+1));
            var _this = this;
            setTimeout(function () {
                if(_this.isRouter){
                    _this.step++;
                    _this.goStep(_this.step);
                }
            })
          $('html,body').animate({scrollTop:0},500);

        }
      }
    }

</script>
<style>
  .step1 .router-link{color:#333;}
  .step1 .el-button-primary .router-link{color:#fff;}
  .step1 .is-active{color: #20a0ff;cursor: pointer;}
  .step1 .login_form {
    padding-top: 5%;
    padding-left: 10%;
    padding-right: 10%;
  }
  .step1 .qxs-ic_user {
    background: url("../../assets/login/ic_user.png") no-repeat;
    background-size: 15px 15px;
    background-position: 3%;
  }
  .step1 .qxs-ic_password {
    background: url("../../assets/login/ic_password.png") no-repeat;
    background-size: 15px 15px;
    background-position: 3%;
    margin-bottom: 20px;
  }
  .step1 .login_logo {
    height: 100%;
  }
  .step1 .login_btn {
    width: 100%;
    font-size: 16px;
    background: -webkit-linear-gradient(left, #fff, #2154FA); /* Safari 5.1 - 6.0 */
    background: -o-linear-gradient(right, #fff, #2154FA); /* Opera 11.1 - 12.0 */
    background: -moz-linear-gradient(right, #fff, #2154FA); /* Firefox 3.6 - 15 */
    background: linear-gradient(to right, #fff , #2154FA); 
    filter: brightness(1.4);
  }
   
  .step1 .outer_label {
    position: relative;
    left: 0;
    top: 0;
    width: 100%;
    height: 220px;
    background: -webkit-linear-gradient(left, #fff, #2154FA); /* Safari 5.1 - 6.0 */
    background: -o-linear-gradient(right, #fff, #2154FA); /* Opera 11.1 - 12.0 */
    background: -moz-linear-gradient(right, #fff, #2154FA); /* Firefox 3.6 - 15 */
    background: linear-gradient(to right, #fff , #2154FA);
    /*background-color: #fff;*/
    text-align: center;
    filter: brightness(1.4);
  }
  .step1 .inner_label {
    position: absolute;
    left:0;
    right: 0;
    bottom: 0;
    top:0;
    margin: auto;
    height: 50px;
  }
  .step1 .qxs-icon {
    height: 40px;
    width: 90%;
    margin-bottom: 5px;
    padding-left: 10%;
    border: 0;
    border-bottom: solid 1px lavender;
  }
</style>
