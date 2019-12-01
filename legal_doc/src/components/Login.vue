<template>
  <div id="login"  class="body">

    <div class="main">
      <img class="logo" src="../assets/logo.png"/>
      <div class="name" style="display: flex;justify-content: center;">
        <span class="info-order-text"><strong>Username:</strong></span>
        <div class="input-box">
          <input type="text"  placeholder=" Username"  name="name"  v-model="user.username" style="height: 33px;width: 300px;border: 2px solid;font-size: 18px;">
        </div>
      </div>
      <div class="password" style="display: flex;justify-content: center;">
        <span class="info-order-text"><strong>Password:</strong></span>
        <div class="input-box">
          <input type="password"  placeholder=" Password" name="password" v-model="user.password" style="height: 33px;width: 300px;border: 2px solid;font-size: 18px;">
        </div>
      </div>
      <div class="forget">
        <a href="#"  v-on:click="goToForgetPassword()"  >Forget password</a>
      </div>
      <div class="button1">
        <button type="submit" v-on:click="userLogin()" style="width: 130px;height: 45px;font-size: 26px;">Login</button>
      </div>
      <div class="button">
        <button type="submit" v-on:click="goToRegister()" style="width: 130px;height: 45px;font-size: 26px;">Register</button>

      </div>
    </div>
  </div>
</template>

<script>
    export default {
      name: "Login",
      data()
      {
        return{
          user:{
            username:'',
            password:'',
            userID:'',
            age:'',
            sex:'',
            email:''
          }
        }

      },



      methods: {
        userLogin() {
          this.$axios.post("/apis/login",this.$data.user)
            .then(response => {
                if (response.status===200){
                  this.$data.user=response.data
                    localStorage.setItem('user',response.data.account)
                  localStorage.setItem('userId',response.data.id)

                        this.$router.push({name: 'UserPage'});

                }
            }).catch(error => {
            this.$notify.error({
              title: 'fail！',
              message: 'password error！'
            });
          })
        },
        goToForgetPassword(){
            this.$router.push({name: 'RePassword'});
        },
        goToRegister(){
          this.$router.push({name: 'Register'})
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
    height: 100%;
    top: 0;
    left: 0;
    position: fixed;
    width: 100%
  }
  img{
    width: 50px;
    height: 50px;
    position: absolute;
    left: 30px;
    top: 30px;
  }
  .back
  {
    position: absolute;
    bottom: 15%;
  }
  .back_word{
    width: 80px;
    position: absolute;
    left: 90px;
    bottom: 6%;
    float: left;
    font-size: 30px;
  }
  .main{
    margin: 0 auto;
    margin-top: 10%;
    padding: 0;
    border: 0;
    width:1000px;
    height: 500px;
    background: url(../assets/white.jpg);
    opacity: 0.8;
    text-align: center;
    position: relative;
  }
  .logo{
    width: 400px;
    height: 400px;
    position: absolute;
    top:50px;
    left: 5%;
  }
  .name  {
    margin:0 auto;
    position: absolute;
    top:130px;
    left: 45%;
  }
  .password{
    margin:0 auto;
    position: absolute;
    top:230px;
    left: 45%;
  }
  .forget{
    position: absolute;
    top:280px;
    right: 5%;
  }
  .button{
    margin:0 auto;
    position: absolute;
    top:350px;
    left: 76%;
  }
  .button1{
    margin:0 auto;
    position: absolute;
    top:350px;
    left: 55%;
  }
  input:focus{
    border-color:#66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(220,20,60,.6)
  }
  .info-order-text{
    width: 200px;
    font-family: STHeiti;
      font-size: 30px;
  }
</style>
