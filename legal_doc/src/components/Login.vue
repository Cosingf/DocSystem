<template>
    <body>
        <div style="height: 80px;"></div>
        <i class="el-icon-collection" style="text-align: center;"></i>
        <a class="title">Sign in to Reedpeer</a>
        <div class="login">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="80px" style="margin-left:-10px;margin-top:10px;" class="demo-ruleForm">
            <el-form-item label="Username" label-position="top" label-width="80px;" style="font-weight: bold;" prop="name">
                <el-input v-model.number="ruleForm.name" style="padding-top:-20px;"></el-input>
            </el-form-item>
            <el-form-item label="Password" label-position="top" label-width="80px;" style="font-weight: bold;" prop="pass">
                <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="success" @click="submitForm('ruleForm')" class="loginBtn"  round>Sign in</el-button>
            </el-form-item>
        </el-form>
        </div>
        <div class="create-account">
            <p>New to Reedpeer?<el-link type="primary" style="font-size:16px;"><router-link href='#' to="/users/register">&nbsp;Create an account.</router-link></el-link></p>    
        </div>
    </body>
</template>
<script>
    export default {
        data() {
        var checkName = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('Username cannot be empty'));
            }
            setTimeout(() => {
                callback();
            }, 1000);
        };
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('Please enter password'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                  this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        return {
            ruleForm: {
                name: '',
                pass: ''
            },
            rules: {
                name: [
                  { validator: checkName, trigger: 'blur' }
                ],
                pass: [
                  { validator: validatePass, trigger: 'blur' }
                ]
              }
            };
        },
        methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                  alert('submit!');
                } else {
                  console.log('error submit!!');
                  return false;
                }
            });
        },
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    }
}
</script>
<style>
body{
    background-color: #f6f6f6;
}
.title{
    font-size: 25px;
    font-weight: 350;
    letter-spacing: -.5px;
    box-sizing: border-box;
    display: block;
    color: #333;
    text-align: center;
    line-height:1.5;
}
.login{
    width: 370px;
    margin: 0 auto;
    margin-top:20px;
    border-top: 1px solid #d8dee2;
    border-radius: 5px;
    padding: 10px 30px 20px 30px;
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
    margin-left:-33px;
    margin-top:28px;
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
    margin-bottom: 12px;
}
</style>