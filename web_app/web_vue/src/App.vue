<template>
  <div id="app">
    <!-- Head of navigation -->
    <header class="header" :class="{ 'header-fixed' : headerFixed }">
    <!--<el-row>
        <el-col :span="24">
          <el-menu default-active="1" class="el-menu-demo" mode="horizontal" @select="">
            <el-menu-item index="1">Data Filter</el-menu-item>
            <el-menu-item index="2">Setting</el-menu-item>
          </el-menu>
        </el-col>
    </el-row>-->
    </header>
    <div v-show="headerFixed" style="position: relative;height: 60px;width: 100%;"></div>

    <main>
          <!-- Left navigation -->
        <div class="main-left" v-show="dashboard">
          <el-menu default-active="/activePublic" class="el-menu-vertical-demo" :router="true">
            <el-menu-item index="/activePublic/step2" :class="{'isActive': active[0]}">Data Filter</el-menu-item>
            <el-menu-item index="/activeManage/detail/page1" :class="{'isActive': active[1]}">Create a project</el-menu-item>
            <el-menu-item index="/activeManage" :class="{'isActive': active[2]}">Project list</el-menu-item></el-menu-item>
            <el-menu-item index="/activePublic/step3" :class="{'isActive':  active[3]}">Profile</el-menu-item>
          </el-menu>
        </div>

          <!-- Right content area -->
          <div  class="main-right" >
            <transition name="fade">
              <router-view class="view"></router-view>
            </transition>
          </div>
    </main>
  </div>
</template>


<script>

import ElementUI from 'element-ui'
import locale from 'element-ui/lib/locale/lang/en'

import Vue from 'vue'
import Element from 'element-ui'
import 'element-ui/lib/theme-default/index.css'

import $ from 'jquery'

Vue.use(ElementUI, { locale })
Vue.use(Element)

export default {
  name: 'app',
  data: function (){
    return {
      active:[true,false,false,false],
      headerFixed : false,
      dashboard: false
    }
    
  },
  created: function(){

    this.$router.push('/activePublic');
  },
  methods: {
    //nextStep: function(){
    // this.dashborad = true;
    //}
  },
  mounted(){
      // When you close the browser window, clear the browser cached data in localStorage
      window.onbeforeunload = function (e) {
        var storage = window.localStorage;
        storage.clear()
      }
  },
  watch: {
     '$route': function (to,from) {

         if(to.path === '/activePublic/step3'){
             this.dashboard = localStorage.getItem('verification');
             this.active[0] = false ;
             this.active[1] = false ;
             this.active[2] = false ;
             this.active[3] = true ;
             var _this = this;
             setTimeout(function () {
             if(_this.isRouter){
                  _this.step=3;
                  _this.goStep(_this.step);
                }
             })
         }else if(to.path === '/activePublic/step2'){
             this.dashboard = localStorage.getItem('verification');
             this.active[0] = true ;
             this.active[1] = false ;
             this.active[2] = false ;
             this.active[3] = false ;
         
         }else if(to.path === '/activeManage/detail/page1'){
             this.dashboard = localStorage.getItem('verification');
             this.active[0] = false ;
             this.active[1] = true ;
             this.active[2] = false ;
             this.active[3] = false ;
         }else if(to.path === '/activeManage'){
             this.dashboard = localStorage.getItem('verification');
             this.active[0] = false ;
             this.active[1] = false ;
             this.active[2] = true ;
             this.active[3] = false ;
         }
     }
     //"localStorage.getItem('verification')": function(){
     //    this.dashboard = true;
     //}
  }
}
</script>

<style>

body{margin: 0;}
#app {
  min-width: 1200px;
  margin: 0 auto;
  font-family: "Helvetica Neue","PingFang SC",Arial,sans-serif;
}
/* Head of navigation */
header{z-index: 1000;min-width: 1200px;transition: all 0.5s ease;  border-top: solid 4px #3091F2;  background-color: #fff;  box-shadow: 0 2px 4px 0 rgba(0,0,0,.12),0 0 6px 0 rgba(0,0,0,.04);  }
header.header-fixed{position: fixed;top: 0;left: 0;right: 0;}
header .el-menu-demo{padding-left: 300px!important;}

/* Right content area */
main{
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  min-height: 800px;
  border: solid 40px #CBF8F6;
  background-color: #f2f7f6;
  }
main .main-left{text-align: center;-webkit-box-flex: 0;-ms-flex: 0 0 200px;flex: 0 0 200px;}
main .main-right{
  -webkit-box-flex:1;
  -ms-flex:1;
  flex:1;
background-image: linear-gradient(to left top, #cbf8f6, #d3f7fd, #ddf6ff, #e8f4ff, #f0f4fb, #f0f4fb, #eff3fa, #eff3fa, #e6f3fd, #daf3fc, #cff4f8, #c8f4ee);
  /* background-image: linear-gradient(to left top, #f8c981, #ffc7a4, #ffcdc9, #ffd9e6, #fae6f5, #fae5f5, #fae3f5, #fae2f5, #ffd1e3, #ffc2c0, #ffb994, #f9ba67); */
  padding: 50px 70px; }
main .el-menu{background-color: transparent!important;}
/*  */
.router-link{display:inline-block;width:100%;height:100%;text-align:center;color:#475669;text-decoration: none; }
.is-active .router-link{color:#20a0ff; }

/* Multiple choice */
  .el-form-item .el-radio+.el-radio {
    margin-left: 30px!important;
  }
  /* Routing switching effect */
  .fade-enter-active, .fade-leave-active {
    transition: all .5s;
  }
  .fade-enter, .fade-leave-active {
    opacity: 0;
  }

  .list-enter-active, .list-leave-active {
    transition: all 1s;
  }
  .list-enter, .list-leave-active {
    opacity: 0;
    transform: translateY(30px);
  }

/* Navigation bar menu selected effect */
  .isActive{color: #20a0ff!important;}
   #app main .aside .is-active{color: #475669;}

  /* card */
  .el-card{overflow: visible!important;}
</style>
