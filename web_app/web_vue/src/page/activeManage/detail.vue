<template>
    <div class="detail">
     
      <transition name="fade">
        <router-view></router-view>
      </transition>
    </div>
</template>

<script>

    export default {
        name:'index',
        data: function () {
            return {
              activeTitle:'Project Name',
              hostPerson:'Allen',
              hostTime:'14/9/2020',
              readNum:'2000'
            }
        },
        methods: {
          routerTo: function (tab,event) {
            this.$router.push('/activeManage/detail/page'+(tab.index+1));
          },
        },
        watch: function () {

        }
    }

</script>

<style>
  .detail .el-tabs__item{padding:0 40px;}
</style>
