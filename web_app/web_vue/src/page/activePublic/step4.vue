<template>
  <div class="step4">
    <el-form label-position="top" ref="shareForm" :model="shareForm" class="demo-form-stacked">

      <el-form-item   label="Preview:">
        {{ preview }}
      </el-form-item>


    </el-form>
  </div>
  </template>

<script>
    import store from 'src/store.js'

    export default {
        name: 'step4',
        data: function () {
            return {
                preview: localStorage.getItem('preview'),
                shareForm: {
                    title: '',
                    describe: ''
                }
            }
        },
        watch: {
            shareForm: {
                handler: function () {
                    store.commit('setShareForm', this.shareForm);
                } ,
                deep:true
            }
        },
        methods: {
            handleRemove: function (file, fileList) {
                console.log(file, fileList);
            },
            handlePreview: function (file) {
                console.log(file);
            }
        },
        created: function () {
            store.state.shareForm && Object.assign(this.shareForm, store.state.shareForm);
        }
    }
</script>
<style>
  .step4 .router-link{color:#333;}
  .step4 .el-button-primary .router-link{color:#fff;}
</style>
