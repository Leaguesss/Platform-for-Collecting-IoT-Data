<template>
    <div class="activePublic ">
      <el-steps :space="200" :active="step-1" class="step" v-show="List">
        
        <el-step title="Data filter conditions" description=""></el-step>
        <el-step title="Data table preview" description=""></el-step>
      </el-steps>

      <transition name="fade">
        <router-view class="view"></router-view>
      </transition>

      <div class="outer_label" v-show="Login">
        <img class="inner_label login_logo" src="../../../static/Logo.png">
      </div>

      <input type="text"  class="qxs-ic_user qxs-icon" placeholder="Email" v-show="Login" v-model="userName">
      <input type="password"  class="qxs-ic_password qxs-icon" placeholder="Passwords" v-show="Login" v-model="password">

        <div class="but-group">

          <el-button class="login_btn" @click.native.prevent="handleLogin" v-show="Login" type="primary">Login</el-button>

          <el-button class="register_btn" @click.native.prevent="handleregister" v-show="Login" round :loading="isBtnLoading" type="primary">Register</el-button>

          <!--<el-button class="forgot_btn" @click.native="fogot" v-show="Login" round :loading="isBtnLoading" type="primary">Forgot your passwords</el-button>-->
          <el-button @click.native.prevent="handlePreStep" v-show="preStep">Last step</el-button>
          <el-button @click.native.prevent="handleNextStep" v-show="nextStep" type="primary">Next step</el-button>
          <el-button @click.native.prevent="handlePublish" v-show="retrieve" type="primary">Download</el-button>
          <el-button @click.native.prevent="handleAdd" v-show="add" type="primary">add data</el-button>
        </div>
    </div>
</template>

<script>
  import store from 'src/store.js'
  import $ from 'jquery'
  import {users} from 'src/initializeFirebse.js'
  import Papa from 'papaparse'

  export default {
    name:'activePublic',
    data: function () {
      return {
        isRouter: false,
        preview: true,
        preStep: false,
        add: false,
        List: localStorage.getItem('verification'),
        Login: !localStorage.getItem('verification'), 
        nextStep: localStorage.getItem('verification'),
        data_set:[{UID:"",SensorType:"",Data:"",Time:"",Freq:""}],
        uid:"",
        sensorType:"",
        data:"",
        time:"",
        all: false,
        retrieve: false,
        add: false,
        step: (Number("true"==(localStorage.getItem('verification')))+1),
        usernames: ['test01','test02'],
        passwords: ['12345678','asdasd123'],
        password:"",
        count: 1
      }
    },
    methods: {
      handleLogin: function (){
          if (!this.userName) {
            this.$message.error('Please input user name');
            return;
          }else if (!this.password) {
            this.$message.error('Please enter the password');
            return;
          }else{
            this.$axios.post("/api/researcherAuth/login", 
              this.qs.stringify({
                username: this.userName,
                password: this.password
              })).then(
                res=>{
                      if(res.data.email_now == null){
                        this.$message.error('Email or password incorrect');
                        return;
                      }
                      localStorage.setItem('userEmail',res.data.email_now);
                      localStorage.setItem('userOccupation',res.data.organisation_now);
                      localStorage.setItem('verification',true);
                      //this.Login = false;
                      var _this = this;
                      setTimeout(function () {
                          if(_this.isRouter){
                              _this.step++;
                              _this.goStep(_this.step);
                          }
                      })
                      $('html,body').animate({scrollTop:0},500);
                      localStorage.setItem('verification',true);
                      this.$router.push('/activePublic/step'+(this.step+1));
                      this.$message.success('Logged in successfully!');
                      return;
                  
                      if (this.count >= this.usernames.length){
                        this.$message.error('Email or password incorrect');
                        return;
                        this.count = 1;
                        }
                      }
              ).catch(
                err =>{
                  this.$message.error('Email or password incorrect');
                }
              )
            
          }
      },
      handlePreStep: function () {
            this.step=2;
            this.$router.go(-1);
            var _this = this;
            setTimeout(function () {
                if(_this.isRouter){
                    _this.step=2;
                    _this.goStep(_this.step);
                }
            })
          $('html,body').animate({scrollTop:0},500);
      },

      handleAdd: function () {

          this.$axios.post("/api/sensorAuth/temp", 
            this.qs.stringify({
              uid: "2",
              data: [0,1,2]
            })).then() 
        },


      handleNextStep: function () {
          
        var _this = this;
        if (this.step == 2){
          //querySensor.push({low: localStorage.getItem('tags1'), medium: localStorage.getItem('tags2'), high: localStorage.getItem('tags3')})
          // TODO get sensor info from firebase
          let low = JSON.parse(localStorage.getItem('tags1'))
          let medium = JSON.parse(localStorage.getItem('tags2'))
          let high = JSON.parse(localStorage.getItem('tags3'))

          if(localStorage.getItem('tags1')==null&&localStorage.getItem('tags2')==null&&localStorage.getItem('tags3')==null){
            alert("You have to choose at least one sensor");
            localStorage.setItem('all',false);
          }
          users.orderByKey().once('value', function(snap) {
            //console.log(snap.val()); // first item, in format {"<KEY>": "<VALUE>"}
            let res = snap.val();
            
            for (var uid in res){ //user id identify user who produced the data
              let sensordata = res[uid].sensor;
              if(sensordata != null) {
                for (var key in sensordata){
                  let sensorType = sensordata[key].sensorType; // sensor type of the data
                  let data = sensordata[key].sensorData; // actual data
                  let time = sensordata[key].time_recorded;
                  // filter the sensor type to what user specified in tag1, tag2 and tag3, display result in next slide

                  for( var tag in low){
                    if(low[tag].name == sensorType){
                      console.log(uid+ ": " + sensorType + " "+ data + " "+time);
                      
                      _this.uid = uid;
                      _this.sensorType = sensorType;
                      _this.data = data;
                      _this.time = time;
                      if(_this.uid!=null&&_this.sensorType!=null&&_this.data!=null&&_this.time!=null){
                        _this.data_set.push({UID:_this.uid,SensorType: _this.sensorType,Data: _this.data,Time: _this.time,Freq:"low"});
                      }
                    }
                  }
                  for( var tag in medium){
                    if(medium[tag].name == sensorType){
                      console.log(uid+ ": " + sensorType + " "+ data + " "+time);

                      _this.uid = uid;
                      _this.sensorType = sensorType;
                      _this.data = data;
                      _this.time = time;
                      if(_this.uid!=null&&_this.sensorType!=null&&_this.data!=null&&_this.time!=null){
                        _this.data_set.push({UID:_this.uid,SensorType: _this.sensorType,Data: _this.data,Time: _this.time,Freq:"medium"});
                      }                    }
                  }
                  for( var tag in high){
                    if(high[tag].name == sensorType){
                      console.log(uid+ ": " + sensorType + " "+ data + " "+time);
                      _this.uid = uid;
                      _this.sensorType = sensorType;
                      _this.data = data;
                      _this.time = time;
                      if(_this.uid!=null&&_this.sensorType!=null&&_this.data!=null&&_this.time!=null){
                        _this.data_set.push({UID:_this.uid,SensorType: _this.sensorType,Data: _this.data,Time: _this.time,Freq:"high"});
                      }                    }
                  }
                  

                }
              }
            }
    })

        }
        
        
        setTimeout(function () {
              localStorage.setItem('preview',JSON.stringify(_this.data_set.slice(2, 12)))
              _this.$router.push('/activePublic/step'+4);
              if(_this.isRouter){
                  _this.step=4;
                  _this.goStep(_this.step);
              }
          },3000)
        $('html,body').animate({scrollTop:0},500);

      },
      handlePublish: function () {
        var csv = Papa.unparse(this.data_set); 
        let content = new Blob([csv]);
        let urlObject = window.URL || window.webkitURL || window;
        let url = urlObject.createObjectURL(content);
        let el = document.createElement("a");
        el.href = url;
        el.download = "Test1.csv";
        el.click();
        urlObject.revokeObjectURL(url);
      },
      handleregister: function() {
        this.$router.push('/activeManage/page2');
      },
      goStep: function (n) {
        switch (n) {
          case 1 :
            //if (localStorage.getItem('verification')){
            //  this.List = false; this.preview = false;this.Login = false;this.preStep = false;this.nextStep = false;this.retrieve = false;this.add = false;
            //  break;
            //}else{
            // this.List = false; this.preview = true;this.Login = true;this.preStep = false;this.nextStep = false;this.retrieve = false;this.add = false;
            //  break;
            //}
            this.List = false; this.preview = false;this.Login = false;this.preStep = false;this.nextStep = false;this.retrieve = false;this.add = false;
            break;
            
          case 2 :
            this.List = true; this.preview = false;this.Login = false;this.preStep = false;this.nextStep = true;this.retrieve = false;this.add = false;

            break;
          case 3 :
            this.List = false; this.preview = false;this.Login = false;this.preStep = false;this.nextStep = false;this.retrieve = false;this.add = false;
            break;
          case 4 :
            this.List = true; this.preview = false;this.Login = false;this.preStep = true;this.nextStep = false;this.retrieve = true;this.add = false;

            break;
        }
      }
    },
    watch:{
        '$route': function (to,from) {
            this.isRouter = true;
        }
    }
  }

</script>
<style>
 .activePublic .router-link{color:#fff;}
 .activePublic .but-group .el-button{margin-right: 20px;}
  .activePublic .login_form {
    padding-top: 5%;
    padding-left: 10%;
    padding-right: 10%;
  }
  .activePublic .qxs-ic_user {
    margin-left: 20%;
    background: url("../../assets/login/ic_user.png") no-repeat;
    background-size: 15px 15px;
    background-position: 3%;
  }
  .activePublic .qxs-ic_password {
    margin-left: 20%;
    background: url("../../assets/login/ic_password.png") no-repeat;
    background-size: 15px 15px;
    background-position: 3%;
    margin-bottom: 20px;
  }
  .activePublic .login_logo {
    height: 100%;
  }
  .activePublic .login_btn {
    margin-top: 3%;
    margin-left: 40%;
    width: 20%;
    font-size: 16px;
    background: -webkit-linear-gradient(left, #1E90FF, #2154FA); /* Safari 5.1 - 6.0 */
    background: -o-linear-gradient(right, #1E90FF, #2154FA); /* Opera 11.1 - 12.0 */
    background: -moz-linear-gradient(right, #1E90FF, #2154FA); /* Firefox 3.6 - 15 */
    background: linear-gradient(to right, #1E90FF , #2154FA); 
    filter: brightness(1.4);
  }

  .activePublic .register_btn {
    margin-top: 3%;
    margin-left: 40%;
    width: 20%;
    font-size: 16px;
    background: -webkit-linear-gradient(left, #1E90FF, #2154FA); 
    background: -o-linear-gradient(right, #1E90FF, #2154FA); 
    background: -moz-linear-gradient(right, #1E90FF, #2154FA);
    background: linear-gradient(to right, #1E90FF , #2154FA); 
    filter: brightness(1.4);
  }

  .activePublic .forgot_btn {
    margin-top: 3%;
    width: 20%;
    font-size: 16px;
    background: -webkit-linear-gradient(left, #1E90FF, #2154FA); 
    background: -o-linear-gradient(right, #1E90FF, #2154FA);
    background: -moz-linear-gradient(right, #1E90FF, #2154FA);
    background: linear-gradient(to right, #1E90FF , #2154FA); 
    filter: brightness(1.4);
  }
   
  .activePublic .outer_label {
    margin-top: 3%;
    position: relative;
    left: 0;
    top: 0;
    width: 100%;
    height: 220px;
    text-align: center;
    filter: brightness(1.4);
  }
  .activePublic .inner_label {
    position: absolute;
    left:0;
    right: 0;
    bottom: 0;
    top:0;
    margin: auto;
    height: 150px;
  }
  .activePublic .qxs-icon {
    height: 40px;
    width: 50%;
    margin-bottom: 5px;
    padding-left: 10%;
    border: 0;
    border-bottom: solid 1px lavender;
  }
</style>
