import Vue from 'vue'
import Router from 'vue-router'


import PublicBooks from './components/PublicBooks'
import RePassword from './components/RePassword'
import Register from './components/Register'
import Login from "./components/Login";
import SearchBooks from "./components/SearchBooks";
import MyBooks from "./components/MyBooks";
import UserPage from "./components/UserPage";
import BookInfo from "./components/BookInfo"
import ReadBook from "./components/ReadBook"


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '/login',
      component: Login
    },
    {
      path: '/users/info',
      name: 'UserPage',
      component: UserPage
    },
    {
      path: '/bookinfo',
      name:'BookInfo',
      component: BookInfo
    },
    {
      path: '/publicbooks/sixbooks/1',
      name: 'PublicBooks',
      component: PublicBooks
    },
    {
      path: '/users/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/users/search/:bookname',
      name: 'SearchBooks',
      component: SearchBooks
    },
    {
      path: '/users/repassword',
      name: 'RePassword',
      component: RePassword
    },
    {
      path: '/mybooks/sixbooks/1',
      name: 'MyBooks',
      component: MyBooks
    },
    {
      path: '/publicbook/read',
      name: 'ReadBook',
      component: ReadBook
    }
  ]
})
