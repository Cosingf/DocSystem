<template>
    <div id="SearchBooks" class="body">
         <div  class="top">
      <router-link href='#' to="/users/info"><img class="photo" src="../assets/头像.jpg" ></router-link>
      <p id="welcome">Welcome</p>
      <div class="menu">
        <ul class="button-list">
          <li><router-link href='#' class='button' to="/publicbooks/sixbooks/1">Public library</router-link></li>
          <li><router-link href='#' class='button' to="/mybooks/sixbooks/1">My library</router-link></li>
        </ul>
      </div>
      <p id="back">Exit</p>
      <router-link href='#' to="/"><img class="back_image" src="../assets/enter.png"></router-link>
    </div>
      <div class="main">
        <div class="container">
          <form action="" class="parent">
            <input type="text" name="searchContent" id="searchContent"class="search" placeholder="请输入关键词">
            <input type="button" v-on:click="searchBooks()" name="sub" id="sub" class="btn">
          </form>
        </div>
        <div class="content1" style="background-color: rgba(255,255,255,1);" v-on:click="gotoBook()">
          <img class="book" src="../assets/1.png">
          <div class="intro">
            <div class="text">
              <img class="add" src="../assets/add.png">
              <h3>刑法学&nbsp;&nbsp;&nbsp;XXX（作者）</h3>
            </div>
          </div>
        </div>
      </div>
      <div class="last">
        <div class="lastinfo">
          <div class="network">
            <img class="tip" src="../assets/network.png">
            <ul style="list-style-type:none">
              <br />
              <li><a href="#">中国裁判文书网</a></li><br />
              <li><a href="#">中国法律文书网</a></li><br />
              <li><a href="#">中国庭审公开网</a></li><br />
              <li><a href="#">中国知识产权网</a></li>
            </ul>
          </div>
          <div class="tag">
            <img class="tip" src="../assets/tag.png">
            <ul style="list-style-type:none">
              <br />
              <li><a href="#">行政案件</a></li><br />
              <li><a href="#">刑事案件</a></li><br />
              <li><a href="#">民事案件</a></li><br />
              <li><a href="#">赔偿纠纷</a></li>
            </ul>
          </div>
          <div class="stats">
            <img class="tip" src="../assets/stats.png">
            <ul style="list-style-type:none">
              <br />
              <li><a href="#">婚姻方面</a></li><br />
              <li><a href="#">财产方面</a></li><br />
              <li><a href="#">合同方面</a></li><br />
              <li><a href="#">教育方面</a></li>
            </ul>
          </div>
          <div class="locate">
            <img class="tip" src="../assets/location.png">
            <ul style="list-style-type:none">
              <br />
              <li><a href="#">广东法院网</a></li><br />
              <li><a href="#">福建法院网</a></li><br />
              <li><a href="#">西藏法院网</a></li><br />
              <li><a href="#">宁夏法院网</a></li>
            </ul>
          </div>
          <div class="bookstore">
            <img class="tip" src="../assets/star.png">
            <ul style="list-style-type:none">
              <br />
              <li><a href="#">新华书店</a></li><br />
              <li><a href="#">当当购书网</a></li><br />
              <li><a href="#">京东书城</a></li><br />
              <li><a href="#">晓学堂书屋</a></li>
            </ul>
          </div>
        </div>
        <div class="bottom">
          <ul style="list-style-type:none">
            <li><a href="#">联系我们</a></li>
            <li><a href="#">使用说明</a></li>
            <li><a href="#">可访问性</a></li>
            <li><a href="#">内容举报</a></li>
            <li><a href="#">商业合作</a></li>
            <li><a href="#">广告宣传</a></li>
            <li><a href="#">退出系统</a></li>
          </ul>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
        name: "SearchBooks",
      data(){
          return{
          books:[{
            id:'',
            name:'',
            imgpath:''
          }]
      }
      },
      created() {
          this.books.id=this.$route.query.id;
        this.books.name=this.$route.query.name;
        this.books.imgpath=this.$route.query.imgpath;
      },

      methods:{
        searchBooks(){
          var searchContent= document.getElementById("searchContent").value
          this.$axios({
            method: 'GET',
            url: '/publicbooks/search' +searchContent,
            data:{
              content:searchContent
            }
          })
            .then(function (response) {
              if(response.status===200)
              {
                searchBookInfo=response.data;
                this.$router.push({name: 'SearchBooks',query:{id:searchBookInfo.id, imgpath:searchBookInfo.imgpath, name:searchBookInfo.name}});
              }

            })
        },

        goToMyLibrary(){
          this.$router.push({name: 'MyBooks'});
        },
        goToLogin(){
          this.$router.push({name: 'Login'});
        },
        goToPersonalPage(){
          this.$router.push({name: 'UserPage'});
        }
      }
    }
</script>

<style scoped>
  html{
    width: 100%;
  }
  .body{
    height: 1400px;
    background: url("../assets/bg2.jpg") ;
    background-repeat:no-repeat;
    background-size: 100%;
    margin:auto
  }
  .top{
    margin: 0;
    padding: 0;
    border: 0;
    width:100%;
    height: 60px;
    background: url(../assets/grey.jpg);
    position: absolute;
    top: 0;
  }
  .photo{
    width: 50px;
    height: 50px;
    position: absolute;
    top:8%;
    left: 1%;
  }
  #welcome{
    margin: 0;
    padding: 0;
    font-size: 24px;
    font-weight: bold;
    position: absolute;
    top: 25%;
    left: 5%;
  }
  .menu{
    position: absolute;
    top:15%;
    left: 34%;
  }
  .choosed_button {
    font-size: 25px;
    text-decoration: none;
    padding: 6px 100px;
    display: block;
    color: #FFFFFF;
    font-weight: bold;
    border-radius: 5px;
    background: #ED757A;
  }
  .button {
    font-size: 25px;
    text-decoration: none;
    padding: 6px 100px;
    display: block;
    color: #000000;
    font-weight: bold;
    border-radius: 5px;
    background: #FFFFFF;
  }
  .button:hover {
    color: #FFFFFF;
    background: #ED757A;
  }
  .button:active {
    background-position: 0 top;
    position: relative;
    top: 1px;
    color: #FFFFFF;
    padding: 6px 100px 4px;
    background: #ED757A;
  }
  .button-list {
    list-style: none;
    padding: 0;
    margin: 0;
    width: 100%;
    float: left;
    display: block;
    margin: 0 0 30px;
  }
  .button-list li {
    float: left;
    margin: 0 50px 0 0;
  }
  #back{
    margin: 0;
    padding: 0;
    font-size: 24px;
    font-weight: bold;
    position: absolute;
    top: 23%;
    right: 4%;
  }
  .back_image{
    margin-top: 3px;
    padding: 0;
    border: 0;
    width: 50px;
    height: 50px;
    position: absolute;
    top: 3%;
    right: 1%;
  }
  .main{
    margin: 0 auto;
    margin-top: 60px;
    padding: 0;
    border: 0;
    width:1400px;
    height: 1500px;
    background-color: rgba(255,255,255,0.8);
    position: relative;
  }
  .container {
    height: 60px;
    width: 600px;
    position: absolute;
    top: 5%;
    left: 30%;
  }
  .parent {
    position: relative;
  }
  .search {
    width: 600px;
    height: 40px;
    font-size: 20px;
    border-radius: 18px;
    outline: none;
    border: 3px solid #888888;
    padding-left: 20px;
    position: absolute;
  }
  .btn {
    height: 35px;
    width: 35px;
    position: absolute;
    background: url("../assets/search.png") no-repeat ;
    top: 8px;
    left: 96%;
    border: none;
    outline: none;
    cursor: pointer;
  }
  .content1{
    width: 1000px;
    height: 220px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 15%;
    left: 15%;
  }
  .content2{
    width: 1000px;
    height: 220px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 32%;
    left: 15%;
  }
  .content3{
    width: 1000px;
    height: 220px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 49%;
    left: 15%;
  }
  .content4{
    width: 1000px;
    height: 220px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 66%;
    left: 15%;
  }
  .content5{
    width: 1000px;
    height: 220px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 83%;
    left: 15%;
  }
  .book{
    width: 210px;
    height: 210px;
    position: absolute;
    top: 3%;
    left: 3%;
  }
  .add{
    width: 30px;
    height: 30px;
    position: absolute;
    top: 4%;
    right: 3%;
  }
  .intro{
    width: 630px;
    height: 170px;
    border: 1px solid black;
    border-radius: 15px;
    position: absolute;
    left: 28%;
    top: 12%;
  }
  .text{
    margin-left: 20px;
  }
  .last{
    width: 100%;
    height: 400px;
    background: url("../assets/pink.jpg");
    position: relative;
  }
  .lastinfo{
    height: 300px;
    width: 1380px;
    border-bottom: 1px solid #888888;
    position: absolute;
    left: 13%;
    top: 10%;
  }
  .network{
    width: 250px;
    border-right: 1px solid #888888;
    position: absolute;
    left: 4%;
    top: 0;
  }
  .tag{
    width: 250px;
    border-right: 1px solid #888888;
    position: absolute;
    left: 24%;
    top: 0;
  }
  .stats{
    width: 250px;
    border-right: 1px solid #888888;
    position: absolute;
    left: 44%;
    top: 0;
  }
  .locate{
    width: 250px;
    border-right: 1px solid #888888;
    position: absolute;
    left: 64%;
    top: 0;
  }
  .bookstore{
    width: 250px;
    position: absolute;
    left: 84%;
    top: 0;
  }
  ul{
    width: 250px;
    margin-top: 6px;
    padding: 0;
    text-decoration: none;
    font-size: 20px;
    color: black;
  }
  li a{
    color: #000000;
    text-align: center;
    padding: 0;
  }
  .bottom{
    width: 1200px;
    position: absolute;
    bottom: 1%;
    left: 15%;
  }
  .bottom ul{
    width: 1400px;
  }
  .bottom li{
    display: inline-block;
    width: 200px;
    float: left;
  }
</style>
