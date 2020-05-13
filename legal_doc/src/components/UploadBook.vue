<template>
    <div class="upload-book">
        <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
        <a class="myicon" style="color:#007bff">Readpeer</a>
        <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
        <el-menu-item index="2" @click="goToMyLibrary">My library</el-menu-item>
        <el-menu-item index="3" @click="goToDiscussHome">Discussion</el-menu-item>
        <el-input placeholder="Please enter keywords" prefix-icon="el-icon-search" class="my-input" v-model="input"></el-input>
        <el-button type="primary">Find books</el-button>
          <el-dropdown  trigger="click" >
            <el-avatar icon="el-icon-user-solid"  class="el-dropdown-link" shape="square" size="medium"></el-avatar>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item  @click="goToMyLibrary">Settings</el-dropdown-item>
                <el-dropdown-item > <router-link  to="/" class="router-link">Sign out</router-link></el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        </el-menu>
      <!-- 上传组件、设置上传类型为pdf-->
        <div class="white-panel">
            <el-upload
            class="upload-demo"
            ref="upload"
            accept=".pdf,.PDF"
            drag
            action="https://jsonplaceholder.typicode.com/posts/"
            :auto-upload="false"
            :before-upload="beforeUpload"
            multiple>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">Drag file here to upload it to your own library，or <em>choose to upload</em></div>
            <div class="el-upload__tip" slot="tip" style="text-align: center;">Only PDF files can be uploaded</div>
            </el-upload>
            <el-divider></el-divider>
            <el-form ref="form" :model="form" label-width="160px" style="margin-top:40px;">
                <el-form-item label="Author name">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="Add to public library">
                    <el-switch v-model="form.isPublic"></el-switch>
                </el-form-item>
                <el-form-item style="margin-top:50px;">
                    <el-button type="primary" @click="uploadBook" style="margin-left:-40px;">Upload now</el-button>
                    <el-button style="margin-left:120px;" @click="goToMyLibrary">Cancel</el-button>
                </el-form-item>
            </el-form>
            <div style="height: 100px;"></div>
        </div>
    </div>
</template>
<script>
export default {
  data () {
    return {
      activeIndex: '0',
      form: {
        name: '',
        file: '',
        isPublic: false
      },
      fileList: [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}]
    }
  },
  methods: {
    handleCommand (command) {
      this.$message('click on item ' + command)
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
    beforeUpload (file) {
      this.form.file = file
      return false
    },
    beforeRemove (file, fileList) {
      return this.$confirm(`Make sure to remove ${file.name}？`)
    },
    uploadSuccess () {
      this.$message({
        message: 'Successfully uploaded',
        type: 'success'
      })
    },
    // 上传文献
    uploadBook () {
      this.$refs.upload.submit()
      let formData = new FormData()
      var isPublic = 1
        // 判断是否共享
      this.form.isPublic ? isPublic = 1 : isPublic = 0
        //填写表单数据
      formData.append('isPublic', isPublic)
      formData.append('author', this.form.name)
      formData.append('file', this.form.file)
        //设置传输格式为表单
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
        //请求路径
      axios.post('/apis/upload/' + localStorage.getItem('userId'), formData, config).then(response => {
        if (response.status === 200) {
          this.$notify.success({
            title: 'Successfully uploaded！',
            message: 'Successfully uploaded！'
          })
        }
      })
        .catch(error => {
          this.$notify.error({
            title: ' fail！',
            message: ' fail！'
          })
        })
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
    .upload-book >>> .el-input {
        position: absolute;
        width:250px;
        margin-top:9px;
        margin-left:30px;
    }
    .upload-book >>> .el-button {
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
    .white-panel>>> .el-upload{
        margin-left: 320px;
        margin-top: 50px;
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
.white-panel >>> .el-divider--horizontal {
    display: block;
    height: 2px;
    width: 70%;
    text-align: center;
    margin-top: 30px;
    margin-bottom: 30px;
    margin-left: 150px;
}
.upload-demo >>> .el-upload-list__item-name {
    margin-left: 320px;
}
.upload-demo >>>.el-upload-list__item:hover .el-icon-close {
    margin-right: 200px;
}
.white-panel>>> .el-form{
    margin-top: 20px;
    margin-left: 280px;
}
.white-panel>>> .el-input {
    margin-top: 0px;
}
.white-panel>>>.el-switch__core {
    margin-left:28px;
}
</style>
