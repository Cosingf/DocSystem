<template>
    <body>
        <router-link href='#' to="/"><i class="el-icon-back">&nbsp;Back</i></router-link>
        <div style="height: 10px;"></div>
        <div class="title">
            <a style="color: #6a737d;font-size:20px;">Join Readpeer</a>
            <a class="head">Reset password</a>
        </div>
        <div class="register">
        <!--邮箱（不允许修改），验证码，密码，确认密码，提交-->
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="80px" style="margin-left:20px;margin-top:10px;" class="demo-ruleForm">
            <el-form-item label="Username" label-width="100px;" style="font-weight: bold;" prop="email">
                <br>
                <div style="color:#606266;font-weight: 400;">{{account}}</div>
            </el-form-item>
            <!-- <el-form-item label="Vrification code" label-width="100px;" style="font-weight: bold;" prop="verificationCode">
                <br>
                <el-input style="width:45%;" v-model="ruleForm.verificationCode" autocomplete="off"></el-input>
                <el-button type="primary" style="margin-left: 50px;" plain>Send verification code</el-button>
            </el-form-item> -->
            <el-form-item label="New password" label-width="100px;" style="font-weight: bold;" prop="pass">
                <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="Confirm new password" label-width="100px;" style="font-weight: bold;" prop="checkPass">
                <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="success" @click="submitForm('ruleForm')" class="loginBtn"  round>Commmit</el-button>
            </el-form-item>
        </el-form>
        </div>
    </body>
</template>
<script>
export default {
  data () {
    var checkName = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('Username cannot be empty'))
      }
      setTimeout(() => {
        callback()
      }, 1000)
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter new password'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please enter new password again'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('The two passwords you typed do not match'))
      } else {
        callback()
      }
    }
    var checkEmail = (rule, value, callback) => {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
      if (value === '') {
        callback(new Error('Please enter email'))
      }
      setTimeout(() => {
        if (mailReg.test(value)) {
          callback()
        } else {
          callback(new Error('Please enter the correct email address'))
        }
      }, 100)
    }
    return {
      ruleForm: {
        pass: '',
        checkPass: ''
      },
      account:localStorage.getItem('user'),
      rules: {
        verificationCode: [
          { trigger: 'blur' }
        ],
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios({
            method: 'post',
            url: '/apis/reset',
            params: {
              userId:window.localStorage['userId'],
              pass: this.$data.ruleForm.pass
            }
          })
            .then(response => {
              if (response.status === 200) {
                this.$message({
                  message: 'Successfully reset pasword, please login again!',
                  type: 'success'
                });
                this.$router.push({ name: 'Login' })
              }
            }).catch(error => {
              this.$notify.error({
                title: 'fail！',
                message: 'Reset password failed！'
              })
            })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
<style>
body{
    background-color: #f6f6f6;
}
.tittle{
    letter-spacing: -.5px;
    box-sizing: border-box;
    display: block;
    text-align: center;
}
.head{
    font-size: 40px;
    font-weight: 500;
    box-sizing: border-box;
    display: block;
    color: #333;
    text-align: center;
    line-height:1.6;
}
.register{
    width: 500px;
    margin: 0 auto;
    margin-top:20px;
    border-top: 1px solid #d8dee2;
    border-radius: 5px;
    padding: 10px 45px 20px 30px;
    font-size: 15px;
    background-color: #fff;
    border: 1px solid #d8dee2;
}
.el-icon-collection{
    display: block;
    text-align: center;
    font-size: 40px;
    margin-bottom:20px;
}
.loginBtn{
    text-align: center;
    width:100%;
    margin-left:-27px;
    margin-top:5px;
    font-size:16px;
}
.create-account{
    width:370px;
    margin: 0 auto;
    margin-top:15px;
    padding: 15px 20px 0px 20px;
    text-align: center;
    border: 1px solid #d8dee2;
    border-radius: 5px;
}
.el-form-item__label {
    margin-bottom: -5px;
}
.el-form-item {
    margin-bottom: 15px;
}
.el-icon-back{
    margin-top:15px;
    font-size:20px;
    margin-left:30px;
    cursor: pointer;
    color: #333;
}
</style>
