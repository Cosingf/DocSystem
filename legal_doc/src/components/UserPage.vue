<template>
  <div class="user-page">
      <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
      <a class="myicon" style="color:#007bff">Reedpeer</a>
      <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
      <el-menu-item index="2" @click="goToMyLibrary">My library</el-menu-item>
      <el-input placeholder="Please enter keywords" prefix-icon="el-icon-search" class="my-input" v-model="input"></el-input>
      <el-button type="primary">Find books</el-button>
      <el-dropdown  trigger="click" >
          <el-avatar icon="el-icon-user-solid"  class="el-dropdown-link" shape="square" size="medium"></el-avatar>
          <el-dropdown-menu slot="dropdown">
              <el-dropdown-item >Settings</el-dropdown-item>
              <el-dropdown-item > <router-link  to="/" class="router-link">Sign out</router-link></el-dropdown-item>
          </el-dropdown-menu>
      </el-dropdown>
      </el-menu>
      <div class="white-panel">
        <div class="panel-tips">
          <div style="height: 80px;"></div>
          <a style="font-weight: 500;font-size:23px;margin-left:400px;color:#586069;">User info</a>
        </div>
        <el-table :data="tableData" style="width: 100%;margin-top:0px;margin-left:300px;">
          <el-table-column prop="item" label="" width="180"></el-table-column>
          <el-table-column prop="value" label="" width="180"> </el-table-column>
        </el-table>
        <el-button type="success" @click="resetPassword" plain>Reset password</el-button>
      </div>
  </div>
</template>
<script>
export default {
  data () {
    return {
      input:'',
      activeIndex: '0',
      tableData: [{
        item: 'Username',
        value: 'Legal user'
      },
      {
        item: 'Email',
        value: ''
      }],
      user:{
        id:'',
        account:'',
        password:' ',
        sex:' ',
        age:' ',
        email:' '
      },
    }
  },
  created () {
    this.$data.tableData[0].value = localStorage.getItem('user')
    let userId=localStorage.getItem('userId')
    // console.log("userId:"+userId)
    // console.log("userName:"+localStorage.getItem('user'))
    this.$axios.get('/apis/getInfo/' +  localStorage.getItem('userId'))
      .then(response => {
          this.$data.tableData[1].value = response.data.email
      }).catch(error => {
        this.$notify.error({
          title: 'error！',
          message: error
        })
      })
  },
  methods: {
    goToPublicLibrary () {
      this.$router.push({ name: 'PublicBooks' })
    },
    goToMyLibrary () {
      this.$router.push({ name: 'MyBooks' })
    },
    resetPassword () {
      this.$router.push({ name: 'RePassword' })
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
.myicon{
  position: absolute;
  margin-top: 6px;
  font-size: 24px;
  font-weight: 500;
  margin-left: -135px;
}
.user-page >>> .el-input {
  position: absolute;
  width:250px;
  margin-top:9px;
  margin-left:30px;
}
.user-page  >>> .el-input__inner{
  background: #f6f6f6;
  height:33px;
}
.user-page  >>> .el-button {
  position:absolute;
  font-size:14px;
  padding: 10px 10px;
  margin-top:8px;
  margin-left:290px;
  line-height:13px;
}
.el-avatar{
  position: absolute;
  margin-top:9px;
  margin-left:510px;
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
.el-popper[x-placement^=bottom] {
  margin-top:48px;
  margin-left: 1145px;
}
.router-link{
  color: #606266;
  text-decoration: none;
}
.router-link:hover{
  color: #66b1ff;
}
.white-panel{
  width:1000px;
  margin:0 auto;
  margin-top: 13px;
  background: #fff;
  overflow: hidden;
  border-radius: 2px;
  -webkit-box-shadow: 0 1px 3px rgba(26,26,26,.1);
  box-shadow: 0 1px 3px rgba(26,26,26,.1);
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
.white-panel  >>> .el-button {
    position:relative;
    font-size:14px;
    padding: 10px 10px;
    margin-top:50px;
    margin-left:390px;
    margin-bottom:100px;
    line-height:13px;
}
.white-panel>>> .el-table::before {
    left: 0;
    bottom: 0;
    width: 100%;
    height: 0px;
}
</style>
