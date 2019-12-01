<template>
  <div id="RePassword" class="body">
  <div class="all">
    <div class="back">
      <a href="#" v-on:click="back()" style="float: left;"><img src="../assets/back.png" border="0" ></a>
    </div>
    <span class="back_word">Back</span>
    <div class="main">
      <div class="name" style="display: flex;justify-content: center;">
        <span class="info-order-text"><strong>Eamil:</strong></span>
        <div class="input-box">
          <input type="text"  v-model="modifyPWInfo.email"  placeholder=" Eamil" name="email" style="height: 33px;width: 300px;border: 2px solid;font-size: 18px;">
        </div>
      </div>
      <div class="code" style="display: flex;justify-content: center;">
        <span class="info-order-text"><strong>Verification code:</strong></span>
        <div class="input-box">
          <input type="text" v-model="modifyPWInfo.verifyCode" placeholder=" Code" style="height: 33px;width: 130px;border: 2px solid;font-size: 18px;">
        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <div class="CODE_button">
          <button type="button" onclick="alert('The verification code has been sent!')" style="width: 150px;height: 35px;font-size: 18px;">Send code</button>
        </div>
      </div>
      <div class="new_password" style="display: flex;justify-content: center;">
        <span class="info-order-text"><strong>New password:</strong></span>
        <div class="input-box">
          <input type="password" v-model="modifyPWInfo.password"  placeholder=" New password" name="password" style="height: 33px;width: 300px;border: 2px solid;font-size: 18px;">
        </div>
      </div>
      <div class="repassword" style="display: flex;justify-content: center;">
        <span class="info-order-long-text" ><strong>Confirm password:</strong></span>
        <div class="input-box">
          <input type="password" v-model="modifyPWInfo.confirmPw" placeholder=" Confirm new password" name="password" style="height: 33px;width: 300px;border: 2px solid;font-size: 18px;">
        </div>
      </div>
      <div class="button">
        <button type="submit" v-on:click="repassword()" style="width: 130px;height: 45px;font-size: 26px;">Commit</button>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
    export default {
        name: "RePassword",
      data()
      {
        return{
          modifyPWInfo:{
            email:'',
            password:'',
            confirmPw:'',
            verifyCode:''
            }
          }
        },
      methods:{
        back() {
          this.$router.go(-1);//返回上一层
        },
          repassword(){
            this.$axios.post("/users/repassword",this.$data.modifyPWInfo)
              .then(response => {
                this.$notify({
                  title: '成功',
                  message: '修改密码成功',
                  type: 'success',
                  duration: 2000
                });
              }).catch(error => {
              console.log(error)
              this.$notify({
                title: '失败',
                message: '修改密码失败',
                type: 'error',
                duration: 2000
              });
            })
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
    bottom: 5%;
    float: left;
    font-size: 30px;
  }
  .main{
    margin: 0 auto;
    margin-top: 10%;
    padding: 0;
    border: 0;
    width:700px;
    height: 500px;
    background: url(../assets/white.jpg);
    opacity: 0.8;
    text-align: center;
    position: relative;
  }
  .name{
    margin:0 auto;
    position: absolute;
    top:60px;
    left: 10%;
  }
  .code{
    margin:0 auto;
    position: absolute;
    top:140px;
    left: 10%;
  }
  .new_password{
    margin:0 auto;
    position: absolute;
    top:220px;
    left: 10%;
  }
  .repassword{
    margin:0 auto;
    position: absolute;
    top:300px;
    left: 10%;
  }
  .button{
    margin:0 auto;
    position: absolute;
    top:390px;
    left: 40%;
  }
  input:focus{
    border-color:#66afe9;
    outline: 0;
    -webkit-box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(102,175,233,.6);
    box-shadow: inset 0 1px 1px rgba(0,0,0,.075),0 0 8px rgba(220,20,60,.6)
  }
  .info-order-text{
    width: 240px;
    font-family: STHeiti;
    font-size: 23px;
  }
  .info-order-long-text{
    width: 260px;
    margin-left:-20px;
    font-family: STHeiti;
    font-size: 23px;
  }

</style>
