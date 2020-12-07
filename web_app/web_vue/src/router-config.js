
import activePublic from './page/activePublic/index.vue'
import step1 from './page/activePublic/step1.vue'
import step2 from './page/activePublic/step2.vue'
import step3 from './page/activePublic/step3.vue'
import step4 from './page/activePublic/step4.vue'

import totalpages from './page/activeManage/totalpages.vue'
import activeManage from './page/activeManage/index.vue'
import detail from './page/activeManage/detail.vue'

import page1 from './page/activeManage/page1.vue'
import page2 from './page/activeManage/page2.vue'
import page3 from './page/activeManage/page3.vue'
import page401 from './page/401.vue'
import page404 from './page/404.vue'



export default [
  { path: '/activeManage',component:totalpages,
    children:[
      { path: '', component: activeManage, meta: {requiresAuth: true}  },
      { path: 'page2', component: page2},
      {
        path: 'detail', component: detail, meta: {requiresAuth: true},

        children: [
          {
            path: 'page1', component: page1,
            children: [
              {path: '', component: page3},
  
            ]
          },
          { path: 'page2', component: page2  },
        
        ]
      },
    ],
  

  },{
    path:'/activePublic',component:activePublic,
    children:[
      { path: '', component: step1  }, // TODO require login to visit?
      { path: 'step1', component: step1,
      children: [
              {path: '', component: page2},
            ]
      },
      { path: 'step2', component: step2, meta: {requiresAuth: true}},
      // { path: 'step3', component: step3  },
      { path: 'step4', component: step4, meta: {requiresAuth: true} },
      {name: '401', path: '401', component: page401}
    ]
  },{
    path:'/activePublic/step3',component:step3,
    children:[
      { path: '', component: step3, meta: {requiresAuth: true}}]
    },
    {path:"*", component:page404}
    
]
