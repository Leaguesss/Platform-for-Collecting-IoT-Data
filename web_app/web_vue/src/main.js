// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueRouter from 'vue-router'
import VueResource from 'vue-resource'
import axios from 'axios'
import qs from 'qs'

import App from './App'
// import * as firebase from 'firebase'
import routeConfig from './router-config'
import store from './store.js'
import conf from '../config/conf'




// import './firebase.js'
// import { firestorePlugin } from 'vuefire'
// // firestorePlugin = require('vuefire');


// Vue.use(firestorePlugin)

// const config = firebase.default.initializeApp({
//     apiKey: "AIzaSyAc0lq0LSUmtmQGKWindP0VBqBD0dKkFa8",
//     authDomain: "sensordatacollector-27ecd.firebaseapp.com",
//     databaseURL: "https://sensordatacollector-27ecd.firebaseio.com",
//     projectId: "sensordatacollector-27ecd",
//     storageBucket: "sensordatacollector-27ecd.appspot.com",
//     messagingSenderId: "204178601091",
//     appId: "1:204178601091:web:75531efe1e0c40cd4857b6"
// });  

// firebase.initializeApp(config);
// Vue.prototype.$firebase = firebase;








//Load router middleware
Vue.use(VueRouter)
Vue.use(VueResource)



// Define router
const router = new VueRouter({
  routes: routeConfig
})

// TODO: edit later for deployment env
// Config backend base url
console.log(conf)
let ip = conf.IP
let port = conf.PORT || 8080
axios.defaults.baseURL = "http://"+ip+":" + port;
Vue.prototype.$axios = axios;
//Json stringifier
Vue.prototype.qs = qs

router.beforeEach((to,from,next) => {
	if (to.matched.some(record => record.meta.requiresAuth)) {
		if(!localStorage.getItem('verification')){
			next({name:'401'})
		}
	}
	/*alert(from.path)
	alert(to.path)*/
	// alert(Number("true"==(localStorage.getItem('verification'))))
	if (to.path === '/activeManage'){
		
		if (localStorage.getItem('verification')){
			next();
		}else{
			alert("Please login first")
		} 	
	}else if(from.path === '/activeManage' && to.path === '/activePublic' && localStorage.getItem('verification')){
		// to.path = '/activePublic/step1';
		// next();
		next({
			path: '/activePublic/step2',
			query:{
				redirect: '/activePublic/step2'
			}
		});
	}else{

		next();
	}
})

new Vue({
  router,
  store,
  el: "#app",
  render: h => h(App)
})
