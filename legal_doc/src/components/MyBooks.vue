<template>
  <div class="private-book">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
      <a class="myicon" style="color:#007bff">Readpeer</a>
      <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
      <el-menu-item index="2">My library</el-menu-item>
      <el-menu-item index="3" @click="goToDiscussHome">Discussion</el-menu-item>
      <el-input placeholder="Please enter keywords" prefix-icon="el-icon-search" class="my-input" v-model="input"></el-input>
      <el-button type="primary" v-on:click="searchBooks()">Find books</el-button>
      <el-dropdown  trigger="click" >
          <el-avatar icon="el-icon-user-solid"  class="el-dropdown-link" shape="square" size="medium"></el-avatar>
          <el-dropdown-menu slot="dropdown">
              <el-dropdown-item ><router-link  to="/users/info" class="router-link">Settings</router-link></el-dropdown-item>
              <el-dropdown-item ><a  @click="logout" class="router-link" >Sign out</a></el-dropdown-item>
          </el-dropdown-menu>
      </el-dropdown>
      </el-menu>
      <div class="white-panel">
          <div class="panel-tips">
              <div style="height: 20px;"></div>
              <a style="font-weight: 600;font-size:20px;margin:0 30px;color:#586069;">Books in your own library</a>
              <el-button type="primary" @click="uploadBook" plain>Upload more books</el-button>
              <el-divider></el-divider>
          </div>
        <el-row :gutter="20" style="margin:30px;" >
          <el-col :span="6" v-for="item , index in books" >
              <BookInfo :item="item" :key="index" ></BookInfo>
              <el-button type="danger"  @click="deleteBook(item.id)" plain>Delete from my library</el-button>
          </el-col>
        </el-row>
        <div style="height: 50px;"></div>
      </div>
      <!-- <div class="pagination"><el-pagination  @current-change="currentChangeHandle" :current-page="currentPage" layout="prev, pager, next" :total="100"></el-pagination></div> -->
  </div>
</template>
<script>
import BookInfo from '@/components/BookInfo'
export default {
data () {
  return {
    input: '',
    activeIndex: '2',
    currentPage: 1,
    userId: localStorage.getItem('userId'),
    books: [{
      id: '',
      imgpath: '',
      name: 'legal document',
      path: '',
      author: 'legal user'
    }]
  }
},
components: {
  BookInfo
},
created () {
  let that = this
  that.$axios({
    method: 'POST',
    url: '/apis/mybooks/eightbooks/' + this.userId + '/' + this.currentPage
  })
    .then(response => {
      that.books = response.data
      console.log(that.books)
    }).catch(error => {
      console.log(error)
    })
},
methods: {
  currentChangeHandle (val) {
    this.currentPage = val // 改变默认的页数
    let that = this
    that.$axios({
      method: 'POST',
      url: '/apis/mybooks/eightbooks/' + this.userId + '/' + this.currentPage
    })
      .then(response => {
        that.books = response.data
        console.log(that.books)
      }).catch(error => {
        console.log(error)
      })
  },
  logout(){
      this.$axios({
        method: 'GET',
        url: '/apis/logout'
      })
        .then(response => {
          this.$router.push({ name: 'Login' })
        }).catch(error => {
          console.log(error)
        })
    },
  searchBooks () {
    var searchContent = this.input
    this.$axios({
      method: 'POST',
      url: '/apis/searchMybooks/' + searchContent + '/' + localStorage.getItem('userId')
    })
      .then(response => {
        {
          this.books = response.data
        }
      }).catch(error => {
        console.log(error)
      })
  },
  deleteBook (bookid) {
    this.$axios({
      method: 'DELETE',
      url: '/apis/mybooks/delete/' + bookid + '/' + localStorage.getItem('userId')
    })
      .then(response => {
        {
          this.$notify.success({
            title: 'Successfully delete！',
            message: 'Successfully delete！'
          })
          this.currentChangeHandle(this.currentPage)
        }
      }).catch(error => {
        console.log(error)
      })
  },
  goToPublicLibrary () {
    this.$router.push({ name: 'PublicBooks' })
  },
  uploadBook () {
    this.$router.push({ name: 'UploadBook' })
  },
  goToDiscussHome(){
    this.$router.push({ name: 'DiscussHome' })
  }
}
}
</script>
<style scoped>
.el-menu-item{
  font-weight:500;
  font-size:17px;
  padding:0px 10px;
}
.el-menu--horizontal>.el-menu-item {
  line-height:50px;
  height:50px;
}
.el-icon-collection{
  font-size:15px;
}
.el-menu-demo {
  padding-left: 450px;;
}
/* change */
.myicon{
  position: absolute;
  margin-top: 6px;
  font-size: 24px;
  font-weight: 500;
  margin-left: -185px;
}
.private-book >>> .el-input {
  position: absolute;
  width:250px;
  margin-top:9px;
  margin-left:30px;
}
.private-book >>> .el-input__inner{
  background: #f6f6f6;
  height:33px;
}
.private-book >>> .el-button {
  position:absolute;
  font-size:14px;
  padding: 10px 10px;
  margin-top:8px;
  margin-left:290px;
  line-height:13px;
}
.white-panel  >>> .el-button {
  position:absolute;
  font-size:14px;
  padding: 10px 10px;
  margin-top:-5px;
  margin-left:0px;
  line-height:13px;
}
.panel-tips >>> .el-button {
  position:absolute;
  font-size:14px;
  padding: 10px 10px;
  margin-top:3px;
  margin-left:460px;
  line-height:13px;
}
/* change */
.el-avatar{
  position: absolute;
  margin-top:9px;
  margin-left:440px;
}
.el-avatar--medium {
  width: 34px;
  height: 34px;
  line-height: 34px;
  cursor: pointer;
}
.el-dropdown-link {
  cursor: pointer;
}
.el-icon-arrow-down {
  font-size: 12px;
}
.el-dropdown{
  position:absolute;
}
/* change */
.el-popper[x-placement^=bottom] {
  margin-top:48px;
  margin-left: 1185px;
}
.router-link{
  color: #606266;
  text-decoration: none;
}
.router-link:hover{
  color: #66b1ff;
}
.white-panel{
  min-height: 550px;
  width:1000px;
  margin:0 auto;
  margin-top: 13px;
  background: #fff;
  overflow: auto;
  border-radius: 2px;
  -webkit-box-shadow: 0 1px 3px rgba(26,26,26,.1);
  box-shadow: 0 1px 3px rgba(26,26,26,.1);
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
.el-row {
  margin-bottom: 20px;
  &:last-child {
      margin-bottom: 0;
  }
}
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.pagination >>> .el-pagination{
  margin-left: 570px;
  margin-top:20px;
  color:#586069;
}
.white-panel >>> .el-col {
  padding-bottom: 50px;
}
</style>
