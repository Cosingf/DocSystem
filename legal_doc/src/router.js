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
import UploadBook from "./components/UploadBook"
import DiscussHome from "./components/DiscussHome"
import Discuss from "./components/Discuss"
import DiscussDetail from "./components/DiscussDetail"
import Comment from "./components/Comment"


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/users/info',
      name: 'UserPage',
      component: UserPage
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
    },
    {
      path: '/books/upload',
      name: 'UploadBook',
      component: UploadBook
    },
    {
      path: '/discuss/home',
      name: 'DiscussHome',
      component: DiscussHome
    },
    {
      path: '/discuss/single',
      name: 'Discuss',
      component: Discuss
    },
    {
      path: '/discuss/detail',
      name: 'DiscussDetail',
      component: DiscussDetail
    },
    {
      path: '/discuss/detail/comment',
      name: 'Comment',
      component: Comment
    }
  ]
})
