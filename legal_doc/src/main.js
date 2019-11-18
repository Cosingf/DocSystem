import Vue from 'vue'
import './plugins/axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import router from './router'

import "bootstrap";
import $ from 'jquery'
import './plugins/element.js'
Vue.use(ElementUI);
Vue.config.productionTip = false
Vue.prototype.$ = $;
new Vue({
  render: h => h(App),
  router
}).$mount('#app')
