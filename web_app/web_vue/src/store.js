import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

/* Testing */
const username = []
const password =  []
const Email = []
const mobile = []
const password_now = [0]
const step = 1
const organisatoin = ""
const url = ""
const userEmail = ""
const userOccupation = ""
var tags1 = []
var tags2 = []
var tags3 = []
var verification = false
var all = true
const dataset = [{"UID":123456}]



const date = 'Wed Sep 9 2020 00:00:00'
const ruleForm = {
    name: 'The title of the collected data',
    fenLeis:[
        {name: 'Walking time'},
        {name: 'Running time'},
        {name: 'Sedentariness time'}
    ],
    category: '',
    tags: [{name:'Physical quality of employees'}],
    activeStartTimeDate: date,
    activeEndTimeDate: date,
    activePerson:'',
    activePersonNum:'',
    activeDescribe:'',
    UseMsgShow:'',
    evaluate:'',
    adTitle:'',
    adContent:'',
    adLink:'',
    country:'',
    state:'',
    city:''
}

const signForm ={
    signUpLimit: 'unlimited',
    numLimit: 'unlimited',
    numLimitDetail: '',
    cost: 'free',
    costDetail: '',
    cancel: 'not allow',
    audit: 'not Required',
    needName: true,
    needTel: true,
    signFormList:[
        {title:'Name',require:true},
        {title:'Mobile',require:true},
        {title:'Gender',require:true},
        {title:'Married',require:true}
    ],
    sign: 'Have to sign up',
    signType: 'Sign in qr code',
    secretCode: ''
}
const shareForm ={}
const selfForm ={}
const data = []
const state = {
    ruleForm: ruleForm,
    signForm: {},
    shareForm: {},
    selfForm: {},
    activeList:data
}

/* Read data from local storage */
for(var item in state){
  localStorage.getItem(item)?
    state[item] = JSON.parse(localStorage.getItem(item)): false;
}

const mutations = {
    setRuleForm(state, payload) {
        Object.assign(state.ruleForm, payload);
        localStorage.setItem('ruleForm',JSON.stringify(payload));
    },
    setSignForm(state, payload) {
        Object.assign(state.signForm, payload);
      localStorage.setItem('signForm',JSON.stringify(payload));

    },
    setShareForm(state, payload) {
        Object.assign(state.shareForm, payload);
      localStorage.setItem('shareForm',JSON.stringify(payload));
    },
    setSelfForm(state, payload) {
        Object.assign(state.selfForm, payload);
      localStorage.setItem('selfForm',JSON.stringify(payload));
    }
}

export default  new Vuex.Store({
    state,
    mutations
})
