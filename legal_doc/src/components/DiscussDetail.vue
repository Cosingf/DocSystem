<template>
  <div class="discuss-home">
    <div class="discuss-header">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
      <a class="myicon" style="color:#007bff">Readpeer</a>
      <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
      <el-menu-item index="2" @click="goToMyLibrary">My library</el-menu-item>
      <el-menu-item index="3" @click="goToDiscussHome">Discussion</el-menu-item>
      <el-input placeholder="Please enter keywords" prefix-icon="el-icon-search" class="my-input" v-model="input"></el-input>
      <el-button type="primary" v-on:click="searchBooks()">Find books</el-button>
      <el-dropdown  trigger="click" >
          <el-avatar icon="el-icon-user-solid"  class="el-dropdown-link" shape="square" size="medium"></el-avatar>
          <el-dropdown-menu slot="dropdown">
              <el-dropdown-item ><router-link  to="/users/info" class="router-link">Settings</router-link></el-dropdown-item>
              <el-dropdown-item > <router-link  to="/" class="router-link">Sign out</router-link></el-dropdown-item>
          </el-dropdown-menu>
      </el-dropdown>
      </el-menu>
    </div>
      <div class="white-panel">
        <div style="margin:10px 30px;">
          <div style="font-size: 20px;font-weight: 600;">{{userDiscuss.title}}</div>
          <div style="font-size: 15px;line-height: 25px;">{{userDiscuss.content}}</div>
          <el-button type="primary"  @click="dialogFormVisible = true" plain>Add new comment</el-button> &emsp;
          <span style="color: rgb(133, 144, 166);"><i class="el-icon-chat-round"></i>{{userDiscuss.commentCount}} comments</span>
        </div>
        <el-dialog title="New Discussion" :visible.sync="dialogFormVisible">
          <el-form :model="commentForm">
            <el-form-item label="Content" :label-width="formLabelWidth">
              <el-input type="textarea" v-model="commentForm.content" autocomplete="off" placeholder="Write down your comment"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
              <el-button @click="dialogFormVisible = false">Cancel</el-button>
              <el-button type="primary" @click="publishComment">Publish</el-button>
          </div>
        </el-dialog>
        <el-divider></el-divider>
        <div v-for="item in userComment">
          <Comment :item="item" ></Comment>
          <el-divider></el-divider>
        </div>
      </div>
    <!-- <div class="pagination"><el-pagination  @current-change="currentChangeHandle" :current-page="currentPage" layout="prev, pager, next" :total="100"></el-pagination></div> -->
  </div>
</template>
<script>
import Comment from '@/components/Comment'
export default {
data () {
  return {
    input: '',
    activeIndex: '0',
    discussId: localStorage.getItem('discussId'),
    userDiscuss:{
      id:'',
      title:'',
      content:'',
      userId:'',
      createdDate:'',
      commentCount:'',
      account:'',
      email:''
    },
    userComment:[{
      id:'',
      userId:'',
      entityId:'',
      entityType:'',
      content:'',
      createdDate:'',
      status:'',
      account:'',
      email:'',
      likeCount:'',
      likeStatus:''
    }],
    dialogTableVisible: false,
    dialogFormVisible: false,
    formLabelWidth: '100px',
    commentForm:{
      content:''
    }
  }
},
components: {
  Comment
},
created () {
  //获取当前discuss数据
  this.$axios({
    method: 'GET',
    url: '/apis/discuss/' + this.discussId
  })
    .then(response => {
      this.userDiscuss = response.data
      console.log("user discuss:"+this.userDiscuss)
    }).catch(error => {
      console.log(error)
    });
  //获取discuss对应的comment数据
  this.$axios({
    method: 'GET',
    url: '/apis/comment/' + this.discussId
  })
    .then(response => {
      this.userComment = response.data
      // this.userComment.forEach(element => {
      //   console.log("comment content:"+element.content)
      // });
    }).catch(error => {
      console.log(error)
    });
},
methods: {
  //todo
  publishComment(){
    this.dialogFormVisible = false;
    this.userDiscuss.commentCount+=1
    console.log("comment text:"+this.commentForm.content)
    this.$axios({
      method: 'POST',
      url: '/apis/addComment',
      params: {
        discussId: this.userDiscuss.id,
        content: this.commentForm.content
      }
    })
      .then(response => {
        this.userComment.push(response.data)
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
  goToMyLibrary () {
    this.$router.push({ name: 'MyBooks' })
  },
  goToDiscussHome(){
    this.$router.push({ name: 'DiscussHome' })
  },
  uploadBook () {
    this.$router.push({ name: 'UploadBook' })
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
.discuss-home >>> .el-input {
  position: absolute;
  width:250px;
  margin-top:9px;
  margin-left:30px;
}
.discuss-home >>> .el-input__inner{
  background: #f6f6f6;
  height:33px;
}
.discuss-header >>> .el-button {
  position:absolute;
  font-size:14px;
  padding: 10px 10px;
  margin-top:8px;
  margin-left:290px;
  line-height:13px;
}
.white-panel  >>> .el-button {
  padding: 8px 10px;
  margin-top: 15px;
  margin-bottom: -15px;
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
