<template>
  <div class="discuss-home">
    <div class="discuss-header">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
      <a class="myicon" style="color:#007bff">Readpeer</a>
      <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
      <el-menu-item index="2" @click="goToMyLibrary">My library</el-menu-item>
      <el-menu-item index="3">Discussion</el-menu-item>
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
    </div>
      <div class="white-panel">
        <div style="height: 20px;"></div>
        <div style="margin:0px 30px;font-size:18px;font-weight: 400;">
          <i class="el-icon-document"></i>&nbsp;Latest Discussions
          <el-button type="text" @click="dialogFormVisible = true">Add New Discussion</el-button>
          <el-dialog title="New Discussion" :visible.sync="dialogFormVisible">
            <el-form :model="discussionForm">
              <el-form-item label="Title" :label-width="formLabelWidth">
                <el-input v-model="discussionForm.title" autocomplete="off" placeholder="Write down your title"></el-input>
              </el-form-item>
              <el-form-item label="Discussion" :label-width="formLabelWidth">
                <el-input type="textarea" v-model="discussionForm.content" autocomplete="off" placeholder="Write down your text"></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">Cancel</el-button>
                <el-button type="primary" @click="publishDiscuss">Publish</el-button>
            </div>
          </el-dialog>
        </div>
        <el-divider></el-divider>
        <div v-for="item in userDiscuss">
          <Discuss   :item="item" ></Discuss>
          <el-divider></el-divider>
        </div>

      </div>
    <!-- <div class="pagination"><el-pagination></el-pagination></div> -->
  </div>
</template>
<script>
import Discuss from '@/components/Discuss'
export default {
data () {
  return {
    input: '',
    activeIndex: '3',
    userId: localStorage.getItem('userId'),
    userDiscuss:[{
      account: '',
      commentCount: '',
      content: '',
      createdDate: '',
      email: '',
      id: '',
      title: '',
      userId: ''
    }],
    dialogTableVisible: false,
    dialogFormVisible: false,
    formLabelWidth: '100px',
    discussionForm:{
      title:'',
      content:''
    }
  }
},
components: {
  Discuss
},
created () {
  this.$axios({
    method: 'POST',
    url: '/apis/discuss/home'
  })
    .then(response => {
      this.userDiscuss = response.data
      // this.userDiscuss.forEach(element => {
      //   console.log("createdDate :"+element.createdDate)
      // });
    }).catch(error => {
      console.log(error)
    })
},
methods: {
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
  publishDiscuss(){
    this.dialogFormVisible = false;

    // console.log("discussion text:"+this.discussionForm.text)
    this.$axios({
      method: 'POST',
      url: '/apis/addDiscuss',
      params: {
        title: this.discussionForm.title,
        content: this.discussionForm.content
      }
    })
      .then(response => {
        this.userDiscuss.push(response.data)
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
  open() {
      this.$prompt('Enter your title','Enter your text', 'Add Discussion', {
        cancelButtonText: 'Cancel',
        confirmButtonText: 'Publish',
        inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
        inputErrorMessage: '邮箱格式不正确'
      }).then(({ value }) => {
        this.$message({
          type: 'success',
          message: 'Successfully published! '
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Publish Cancelled!'
        });
      });
    },
  goToPublicLibrary () {
    this.$router.push({ name: 'PublicBooks' })
  },
  goToMyLibrary () {
    this.$router.push({ name: 'MyBooks' })
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
.discuss-header >>> .el-input {
  position: absolute;
  width:250px;
  margin-top:9px;
  margin-left:30px;
}
.discuss-header >>> .el-input__inner{
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
.white-panel >>> .el-divider--horizontal {
    margin: 16px 0;
}
.white-panel >>> .el-button--text {
    color: #409EFF;
    background: 0 0;
    padding-left: 0;
    padding-right: 0;
    margin-left: 580px;
    font-size: 17px;
}
.white-panel >>> .el-dialog {
    width: 40%;
}
.white-panel >>>.el-dialog__body {
    padding-right: 50px;
    padding-top: 20px;
    padding-bottom: 10px;
}
</style>
