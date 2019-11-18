<template>
  <div id="UserPage" class="body">
   <!-- <el-scroller style="height: 100%">-->


    <div class="main">
      <div class="back">
        <a  href="#" v-on:click="back" style="float: left;"><img src="../assets/back.png" border="0"></a>
      </div>
      <span class="back_word" >返回</span>
      <img class="photo" src="../assets/头像.jpg">
      <div class="button1">

        <button v-on:click="gotoPublicBook" style="width: 200px;height: 45px;font-size: 26px;border-radius: 20px;background-color: #FFA07A;"><strong>进入系统</strong></button>


      </div>
      <div class="button2">
        <button @click="gotoRePass" style="width: 200px;height: 45px;font-size: 26px;border-radius: 20px;background-color: #FFA07A;"><strong>修改密码</strong></button>
      </div>

      <div class="info" style="float: left;" >
        <p  >用户名：{{user.account}}</p><p></p>
        <p v-if="ok">邮箱：{{user.email}}</p>
        <p>最近常看书籍：</p>

      </div>




      <div class="book">
        <div><img style="height: 300px;width: 250px;top: -12%" src="../assets/刑法.jpg" ></div>
        <div style="position: absolute;left: 30%;top: -20%"><img style="height: 300px;width: 250px;" src="../assets/国际争端.jpg"></div>
        <div style="position: absolute;left: 60%;top: -20%"><img style="height: 300px;width: 250px;" src="../assets/革命根据地.jpg"></div>
      </div>
    </div>
    <!--</el-scroller>-->
  </div>

</template>

<script>
    export default {
      name: "UserPage",
      data()
      {
        console.log(2)
        return{
          user:{
            id:'',
            createTime:'',
            username:'',
            password:' ',
            userID:' ',
            age:' ',
            sex:' ',
            email:' '
          },
          ok: false,
        }

      },
     created(){
        this.$data.user.username=localStorage.getItem('user')
        this.$axios.get("/apis/getInfo/"+this.$data.user.username)
          .then(response => {
            if (response.status===200){
              this.$data.user=response.data
              this.ok=true;

            }
          }).catch(error => {
          this.$notify.error({
            title: 'error！',
            message: 'error'
          });
        })
      },

      methods: {
        back() {
          this.$router.go(-1);//返回上一层
        },
        gotoPublicBook(){
      /*    this.$axios.get("/publicbooks/sixbooks/"+this.$data.pageNum)
            .then(response=>{
              this.$data.bookInfo.pop()
              this.$data.bookInfo.push(response.data)
            }).catch(error=>{
            console.log(error)
          })*/
          this.$router.push({name: 'PublicBooks'});
        },

        gotoRePass(){
          this.$router.push({name: 'RePassword'});
        }

      }
    }

</script>

<style scoped>
  html{
    width: 100%;
  }
  .body{
    background: url("../assets/bg.jpg") no-repeat;
    background-size:  100%;
    top: 0;
    left: 0;
    height: 1400px;
    position: absolute;
    width: 100%
  }
  img{
    width: 50px;
    height: 50px;
    position: absolute;
    left: 30px;
    top: 30px;
  }
  .back_word{
    width: 80px;
    position: absolute;
    left: 90px;
    top: 3%;
    float: left;
    font-size: 30px;
  }
  .main{
    margin: 0 auto;
    margin-top: 10%;
    padding: 0;
    border: 0;
    width:1000px;
    height: 1200px;
    background: url(../assets/white.jpg);
    opacity: 0.8;
    position: relative;
  }
  .photo{
    width: 300px;
    height: 300px;
    position: absolute;
    top: 10%;
    left: 10%;
  }
  .button1{
    position: absolute;
    top: 15%;
    right: 10%;
  }
  .button2{
    position: absolute;
    top: 25%;
    right: 10%;
  }
  .info{
    width: 400px;
    float: left;
    position: absolute;
    top: 35%;
    left: 12%;
    font-weight: bold;
    font-size: 20px;
  }
  .book{
    margin: 0;
    padding: 0;
    width: 1000px;
    height: 400px;
    position: absolute;
    top: 65%;
    left: 6%;
  }
</style>
