<template>
    <div class="step2">
     


      <el-row class="demo-autocompleteLow">
      <el-col :span="9">
        <div class="title">Low Frequency</div>
        <el-autocomplete
         class="inline-input"
          v-model="state1"
          :fetch-suggestions="querySearchSensor"
          placeholder="Please enter the specific sensor"
          @select="handleSelect"
        ></el-autocomplete>
      </el-col>
      <el-col :span="10">
        <el-button icon="plus" size="large" @click.native="handleAdd1(state1,ruleForm.tags1,ruleForm.totaltags)" style="vertical-align: middle;margin: 10px;"></el-button>
      </el-col>
      </el-row>

      <el-row class="demo-autocompleteLowTags">
        <el-tag
          v-for="tag in ruleForm.tags1"
          :closable="true"
          type="primary"
          @close="handleClose1(tag)"
           >
          {{tag.name}}
        </el-tag>
      </el-row>

      <el-row class="demo-autocompleteMedium">
      <el-col :span="9">
        <div class="title">Medium Frequency</div>
        <el-autocomplete
          class="inline-input"
          v-model="state2"
          :fetch-suggestions="querySearchSensor"
          placeholder="Please enter the specific sensor"
          :trigger-on-focus="false"
         @select="handleSelect"
        ></el-autocomplete>
      </el-col>
      <el-col :span="10">
        <el-button icon="plus" size="large" @click.native="handleAdd2(state2,ruleForm.tags2,ruleForm.totaltags)" style="vertical-align: middle;margin: 10px;"></el-button>
      </el-col>
      </el-row>

      <el-row class="demo-autocompleteMediumTags">
        <el-tag
          v-for="tag in ruleForm.tags2"
          :closable="true"
          type="primary"
          @close="handleClose2(tag)"
           >
          {{tag.name}}
        </el-tag>
      </el-row>
    
      <el-row class="demo-autocompleteHigh">
      <el-col :span="9">
        <div class="title">High Frequency</div>
        <el-autocomplete
          class="inline-input"
          v-model="state3"
          :fetch-suggestions="querySearchSensor"
          placeholder="Please enter the specific sensor"
          :trigger-on-focus="false"
         @select="handleSelect"
        ></el-autocomplete>
      </el-col>
      <el-col :span="10">
        <el-button icon="plus" size="large" @click.native="handleAdd3(state3,ruleForm.tags3,ruleForm.totaltags)" style="vertical-align: middle;margin: 10px;"></el-button>
      </el-col>
      </el-row>

      <el-row class="demo-autocompleteHighTags">
        <el-tag
          v-for="tag in ruleForm.tags3"
          :closable="true"
          type="primary"
          @close="handleClose3(tag)"
           >
          {{tag.name}}
        </el-tag>
      </el-row>

    </div>
</template>


<script>
  import store from 'src/store.js'

  export default {
    name:'step2',
    data: function () {
      return {
        restaurants: [],
        state1: '',
        state2: '',
        state3: '',
        test:'',
        tagsValid1:false,
        tagsError1:'Please set labels',
        tagsValid2:false,
        tagsError2:'Please set labels',
        tagsValid3:false,
        tagsError3:'Please set labels',
        isAddressTrue:false,
        dialogFormVisible: false,
        dialogFormFenLeiVisible:false,
        dialogForm:{name:''},
        dialogFormFenLei:{name:''},
        ruleFormChange:false,
        ruleFormValid:false,
        rules: {
          name:[
            {required:true,message:'Please enter the title of the current study',trigger:'change'}
          ],
          back:[
            {required:true,message:'Please enter the background information of the current study',trigger:'change'}
          ],
          category:[
            {required:true,message:'Please select data collection category',trigger:'change'}
          ],
          activeStartTimeDate: [{required:true,message:'Please select the start time',trigger:'change'}],
          activeEndTimeDate:   [{required:true,message:'Please select the end time',trigger:'change'}]
        },
        ruleForm: {
          name: 'The title of the current study',
          back: 'The background information of the current study',
          fenLeis:[
            {name: 'Walking'},
            {name: 'Running'},
            {name: 'Sedentariness'},
            {name: 'Type1'},
            {name: 'Type2'}
          ],
          category: '',
          tags1: [],
          tags2: [],
          tags3: [],
          totaltags: [],
          activeStartTimeDate: '',
          activeEndTimeDate: '',
          activePerson:'',
          activePersonNum:'',
          UseMsgShow:''
        }
      };
    },
    
    watch: {
      ruleForm: {
        handler: function (val,oldVal) {
          store.commit('setRuleForm',this.ruleForm);
          this.tagsValid1 = !this.ruleForm.tags1.length ? '' :false ;
          this.tagsValid2 = !this.ruleForm.tags2.length ? '' :false ;
          this.tagsValid3 = !this.ruleForm.tags3.length ? '' :false ;
          this.ruleFormChange = true ;
        },
        deep: true
      }
    },
    methods: {
      querySearchSensor(queryString, cb) {
        var Sensor = this.Sensor;
        var results = queryString ? Sensor.filter(this.createFilter(queryString)) : Sensor;
        // A callback call returns the data for the list of Suggestions
        cb(results);
      },
      createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      loadAllSensor() {
        return [
          { "value": "Goldfish 3-axis Accelerometer"},
          { "value": "Goldfish 3-axis Gyroscope"},
          { "value": "Goldfish 3-axis Magneticfield sensor"},
          { "value": "Goldfish Orientation sensor"},
          { "value": "Goldfish Temperature sensor"},
          { "value": "Goldfish Proximity sensor"},
          { "value": "Goldfish Light sensor"},
          { "value": "Goldfish Pressure sensor"},
          { "value": "Goldfish Humidity sensor"},
          { "value": "Goldfish 3-axis Magneticfield sensor (uncalibrated)"},
          { "value": "Goldfish 3-axis Gyroscope (uncalibrated)"},
          { "value": "Game Rotation Vector Sensor"},
          { "value": "GeoMag Rotation Vector Sensor"},
          { "value": "Gravity Sensor"},
          { "value": "Linear Acceleration Sensor"},
          { "value": "Rotation Vector Sensor"},
          { "value": "Orientation Sensor"}
        ];
      },
      handleSelect(item) {
        console.log(item);

      },
      handleReset: function () {
        this.$refs.ruleForm.resetFields();
      },
      handleSubmit: function (ev) {},
      handleRemove: function (file, fileList) {
        console.log(file, fileList);
      },
      handlePreview: function (file) {
        console.log(file);
      },
      handleSuccess: function () {
      },
      handleError: function () {
      },
      showDialog: function () {
        if(this.ruleForm.tags1.length >16 ){
          this.$message({
            message: 'Set up to 16 labels',
            type: 'warning'
          });
        }else{
          this.dialogFormVisible = true;
          this.dialogForm = {};
        }

        if(this.ruleForm.tags2.length >16 ){
          this.$message({
            message: 'Set up to 16 labels',
            type: 'warning'
          });
        }else{
          this.dialogFormVisible = true;
          this.dialogForm = {};
        }

        if(this.ruleForm.tags3.length >16 ){
          this.$message({
            message: 'Set up to 16 labels',
            type: 'warning'
          });
        }else{
          this.dialogFormVisible = true;
          this.dialogForm = {};
        }
      },
      handleClose1: function (tag) {
        var index1 = this.ruleForm.tags1.indexOf(tag);
        this.ruleForm.tags1.splice(index1,1);
        this.ruleForm.totaltags.splice(index1,1);
      },
      handleClose2: function (tag) {
        var index2 = this.ruleForm.tags2.indexOf(tag);
        this.ruleForm.tags2.splice(index2,1);
        this.ruleForm.totaltags.splice(index2,1);
      },
      handleClose3: function (tag) {
        var index3 = this.ruleForm.tags3.indexOf(tag);
        this.ruleForm.tags3.splice(index3,1);
        this.ruleForm.totaltags.splice(index3,1);
      },
      handleCloseFenLei: function (category){
        var index = this.ruleForm.fenLeis.indexOf(category);
        this.ruleForm.fenLeis.splice(index,1);
      },
      handleAdd1: function (tag,tags,totaltags) {
        if(tag && tag.trim().length !== 0){
          var isExist = false ;
          tag = tag.trim();
          for(var i=0;i<totaltags.length; i++){
            if(totaltags[i].name == tag ){
              isExist = true;
              break
            }
          }
          if(isExist){
            this.$message({
              message: 'The label already exists',
              type: 'warning'
            });
          }else{
            this.dialogFormVisible = false;
            this.dialogFormFenLeiVisible = false;
            tags.push({
              name: tag
            });
            totaltags.push({
              name: tag
            });
          }
        }else{
          this.$message({
            message: 'The label cannot be empty',
            type: 'warning'
          });
        };
        localStorage.setItem('tags1', JSON.stringify(this.ruleForm.tags1));
      },
      handleAdd2: function (tag,tags,totaltags) {
        if(tag && tag.trim().length !== 0){
          var isExist = false ;
          tag = tag.trim();
          for(var i=0;i<totaltags.length; i++){
            if(totaltags[i].name == tag ){
              isExist = true;
              break
            }
          }
          if(isExist){
            this.$message({
              message: 'The label already exists',
              type: 'warning'
            });
          }else{
            this.dialogFormVisible = false;
            this.dialogFormFenLeiVisible = false;
            tags.push({
              name: tag
            });
            totaltags.push({
              name: tag
            });
          }
        }else{
          this.$message({
            message: 'The label cannot be empty',
            type: 'warning'
          });
        };
        localStorage.setItem('tags2',  JSON.stringify(this.ruleForm.tags2));
      },
      handleAdd3: function (tag,tags,totaltags) {
        if(tag && tag.trim().length !== 0){
          var isExist = false ;
          tag = tag.trim();
          for(var i=0;i<totaltags.length; i++){
            if(totaltags[i].name == tag ){
              isExist = true;
              break
            }
          }
          if(isExist){
            this.$message({
              message: 'The label already exists',
              type: 'warning'
            });
          }else{
            this.dialogFormVisible = false;
            this.dialogFormFenLeiVisible = false;
            tags.push({
              name: tag
            });
            totaltags.push({
              name: tag
            });
          }
        }else{
          this.$message({
            message: 'The label cannot be empty',
            type: 'warning'
          });
        };
        localStorage.setItem('tags3',  JSON.stringify(this.ruleForm.tags3));
      },
      openAd: function (){
        this.$message('The feature is being refined');
      },

    },
    created: function(){
        Object.assign(this.ruleForm, store.state.ruleForm);
        var _this = this;
        setTimeout(function () {
            _this.ruleFormChange = false ;
        })
    },
      beforeRouteLeave: function (to, from, next) {
      if(to.path == '/activePublic/step3'){
        var _this = this;
        this.isAddressTrue = true ;
        this.tagsValid1 = (this.ruleForm.tags.length ? false : true) ;
        this.$refs.ruleForm.validate(function(valid){
          (!_this.tagsValid1 && valid) ? next() : next(false);
        }) ;
        this.tagsValid2 = (this.ruleForm.tags.length ? false : true) ;
        this.$refs.ruleForm.validate(function(valid){
          (!_this.tagsValid2 && valid) ? next() : next(false);
        }) ;
        this.tagsValid3 = (this.ruleForm.tags.length ? false : true) ;
        this.$refs.ruleForm.validate(function(valid){
          (!_this.tagsValid3 && valid) ? next() : next(false);
        }) ;
      }else{
        next();
      }
    },
    mounted() {
      this.Sensor = this.loadAllSensor();
    }
  }
</script>
<style>
    .step2 .demo-autocompleteLow{margin-bottom: 25px;margin-right: 50px;}
    .step2 .demo-autocompleteMedium{margin-bottom: 25px;margin-right: 50px;}
    .step2 .demo-autocompleteHigh{margin-bottom: 25px;margin-right: 50px;}
    .step{margin-bottom: 30px;}
    .step2 .demo-ruleForm .el-form-item{margin-bottom: 25px;margin-right: 50px;}
    .step2 .el-form-item.is-required .el-form-item__label:after {
      content: '*';
      color: #ff4949;
      margin-right: 4px;
    }
    .step2 .el-form-item.is-required .el-form-item__label:before { display: none; }
    /* Label */
    .step2 .el-tag{padding: 10px 15px;margin:10px;vertical-align: middle;height: auto;}
    .step2 .el-tag:first-child{margin-left: 0;}

    /* Dialog */
    .step2 .el-dialog--small{width: 564px;}
    .step2 .el-dialog__Sensor{padding-bottom: 0;}
    .step2 .el-dialog__Sensor .el-form-item{margin-bottom: 10px;}
    .step2 .el-dialog__wrapper  .el-button{margin-left: 15px;}


   /* TimePicker */
    .step2 .el-date-editor{width:150px;}

    .step2 .el-form-item .router-link{color:#fff;}
    .el-form-item__error{white-space: nowrap;}

    .step2 .list-and-search{
     background: #fff;
     border: 1px solid #ccc;
     display: none;
     &.on{
      display: block;
     }
     }
    .step2 .cur-name{
     height: 32px;
     line-height: 32px;
     text-indent: 10px;
     position: relative;
     color: #777;
     &:after{
     position: absolute;
     right: 9px;
     top: 13px;
     content: " ";
     width: 0;
     height: 0;
     border-right: 6px solid transparent;
     border-top: 6px solid #7b7b7b;
     border-left: 6px solid transparent;
     border-bottom: 6px solid transparent;
     }
     &.show{
     &:after{
     right: 9px;
     top: 6px;
     border-right: 6px solid transparent;
     border-bottom: 6px solid #7b7b7b;
     border-left: 6px solid transparent;
     border-top: 6px solid transparent;
     }
     }
     }
    .step2 .vue-dropdown {
     width: 200px;
     z-index:10;
     border-radius:3px; 
     border: 1px solid #ccc;
     cursor: pointer;
     -webkit-user-select:none; 
     user-select:none;
     &._self-show {
      display: block!important;
     }
    .step2 .search-module {
      position: relative;
      border-bottom: 1px solid #ccc;
      .search-text {
      width: 100%;
      height: 30px;
      text-indent: 10px;
      box-shadow: none;
      outline: none;
      border: none;
      }
      .search-icon {
      position: absolute;
      top: 24%;
      right: 0.5em;
      color: #aaa;
      }
     }
     input::-webkit-input-placeholder{
      font-size: 14px;
     }
    .step2 .list-module {
      max-height: 200px;
      overflow-y: auto;
      li {
      &._self-hide {
       display: none;
      }
      margin-top: 0.4em;
      padding: 0.4em;
      &:hover {
       cursor:pointer;
       color: #fff;
       background: #00a0e9;
      }
      }
     }
     }
    .step2 .tip-nodata {
     font-size: 14px;
     padding: 10px 0;
     text-indent: 10px;
     }
</style>
