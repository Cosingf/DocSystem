<template>
  <div id="MyBooks" class="body">
  <div class="top">
    <img class="photo" src="../assets/头像.jpg"onclick="javascrtpt:window.location.href='个人主页.html'">
    <p id="welcome">欢迎您，XXX</p>
    <div class="menu">
      <ul class="button-list">
        <li><a href='#' class='button' v-on:click="goToPublicLibrary()">公共书库</a></li>
        <li><a href='/' class='choosed_button' onclick="return false;">我的书库</a></li>
      </ul>
    </div>
    <p id="back">退出</p>
    <a href="#"  v-on:click="goToLogin()"><img class="back_image" src="../assets/enter.png"></a>
  </div>
  <div class="main">
    <div class="container">
      <form action="" class="parent">
        <input type="text" name="searchContent" id="searchContent"class="search" placeholder="请输入关键词">
        <input type="button" v-on:click="searchBooks()" name="sub" id="sub" class="btn">
      </form>
    </div>
    <div class="delete_button">
      <button type="button" v-on:click="deleteBooks()" style="width: 150px;height: 45px;font-size: 26px;border-radius: 20px;background-color: #FFA07A;">删除书籍</button>
    </div>



    <div class="sku-box store-content">
      <div class="sort-option">
      </div>
      <div class="gray-box">
        <div class="item-box" ></div>
        <BookInfo  v-for="item , index in books" :item="item" :key="index"></BookInfo>
      </div>
    </div>


    <div class="page">
      <a href="#" class="prev">上一页</a>
      <a href="#" class="on num">1</a>
      <a href="#" class="num">2</a>
      <a href="#" class="num">3</a>
      <a href="#" class="num">4</a>
      <span>...</span>
      <a href="#" class="next">下一页</a>
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
  import BookInfo from '@/components/BookInfo'
    export default {
        name: "MyBooks" ,
      data(){
        return{
          userId:localStorage.getItem('userId'),
          pageNum:1,
          books:[{
            id:"ggg",
            imgpath:"/apis/1.jpg",
            name:"2.pdf",
            path:"",
            author:"rrf"
          }
          ],
          searchBookInfo:[
            {
              id:"",
              imgpath:"",
              name:""
            }
          ]
        }
      },
      components: {
        BookInfo
      },
          /*searchBookInfo:[
            {
              id:"",
              imgpath:"",
              name:""
            }
          ],*/
      created(){
        let that=this;
        that.$axios({
          method:'POST',
          url:'/apis/mybooks/sixbooks/'+this.userId
        })
          .then(response=>{
           that.books=response.data
            console.log(books)
          }).catch(error=>{
          console.log(error)
        })

      },

      methods:{

        deleteBooks(){
          var checkedlist=$(":checkbox[name='check']:checked")
          var bookList=checkedlist.parentElement.id
          this.$axios({
            method: 'DELETE',
            url: '/mybooks/delete',
            data:{
              userId:window.localStorage['userId'],
              booklist:bookList
            }
          })
        },

        goToPublicLibrary(){
          this.$router.push({name: 'PublicBooks'});
        },
        goToLogin(){
          this.$router.push({name: 'Login'});
        },
        searchBooks(){
          var searchContent= document.getElementById("searchContent").value
          this.$axios({
            method: 'POST',
            url: '/apis/mybooks/sixbooks/' +searchContent +'/'+localStorage.getItem('userId'),

          })
            .then(response=>{
              {
                this.books=response.data
                console.log(books)
              }
            }).catch(error=>{
            console.log(error)
          })
        },
      }
    }
</script>

<style scoped>
  html{
    width: 100%;
  }
  .sku-box{
    position: relative;
  }
  .sort-option{
    border-top: 1px solid #D8D8D8;
    color: #999;
  }
  .sort-option ul{
    height: 60px;
    line-height: 60px;
  }
  .sort-option li{
    position: relative;
    float: left;
    padding-left: 42px;
  }
  .sort-option li:first-child{
    padding-left: 9px;
  }
  .sort-option li:before{
    content: ' ';
    display: block;
    position: absolute;
    left: 20px;
    top: 50%;
    width: 2px;
    height: 2px;
    margin-top: -1px;
    background: #C7C7C7;
  }
  .sort-option li:first-child:before{
    display: none;
  }
  .sort-option a{
    display: block;
    font-size: 12px;
    color: #999;
  }
  .sort-option a.active, .sort-option a:hover{
    color: #5683EA;
  }
  .gray-box{
    overflow: hidden;
    background: #fff;
    border-radius: 8px;
    border: 1px solid #dcdcdc;
    border-color: rgba(0,0,0,.14);
    box-shadow: 0 3px 8px -6px rgba(0,0,0,.1);
  }
  .sku-box .item-box{
    clear: both;
    overflow: hidden;
    margin: 0 -1px -1px -1px;
  }
  .sku-box .item{
    position: relative;
    float: left;
    border-right: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    width: 25%;
    height: 429px;
    background: #fff;
    box-sizing: border-box
  }
  .sku-box .item:hover{
    box-shadow: 0 0 38px rgba(0,0,0,.08) inset;
    transition: all .15s ease;
  }
  .sku-box .item .item-img img{
    display: block;
    width: 206px;
    height: 206px;
    margin: 50px auto 10px;
  }
  .sku-box .item h3, .sku-box .item h6{
    overflow: hidden;
    text-align: center;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .sku-box .item h6{
    line-height: 1.2;
    font-size: 16px;
    color: #424242;
    margin: 0 auto;
    padding: 0 14px;
  }
  .sku-box .item h3{
    line-height: 1.2;
    font-size: 12px;
    color: #d0d0d0;
    margin: 8px auto 14px;
  }
  .sku-box .item .params-colors{
    margin-top: 23px;
    text-align: center;
  }
  .sku-box .item .colors-list{
    display: inline-block;
    overflow: hidden;
  }
  .sku-box .item .colors-list li{
    float: left;
    margin: 0 5px;
  }
  .sku-box .item .colors-list>li a{
    width: 8px;
    height: 8px;
    border: 1px solid #e5e5e5;
    -webkit-border-radius: 50%;
    -moz-border-radius: 50%;
    border-radius: 50%;
    padding: 2px;
    display: block;
  }
  .sku-box .item .colors-list>li a.active{
    box-shadow: inset 0 0 0 1px #b2b2b2;
    border-color: #b2b2b2;
  }
  .sku-box .item .colors-list>li img{
    width: inherit;
    height: inherit;
    border-radius: 50%;
    display: block;
  }
  .sku-box .item .item-btns{
    position: absolute;
    left: 0;
    right: 0;
    bottom: 29px;
    text-align: center;
    opacity: 0;
    z-index: 10;
  }
  .sku-box .item:hover .item-btns{
    opacity: 1;
    transition: all .2s ease-in;
  }
  .sku-box .item .item-btns .item-blue-btn, .sku-box .item .item-btns .item-disabled-btn, .sku-box .item .item-btns .item-gray-btn, .sku-box .item .item-btns .item-green-btn{
    display: inline-block;
    box-sizing: border-box;
    width: 100px;
    height: 30px;
    font-size: 12px;
    line-height: 28px;
    border-radius: 4px;
    cursor: pointer;
    font-weight: 200;
    transition: all .1s ease;
  }
  .sku-box .item .item-btns .item-gray-btn{
    border: 1px solid #d5d5d5;
    color: #646464;
  }
  .sku-box .item .item-btns .item-gray-btn a{
    display: block;
    color: #a1a1a1;
  }
  .sku-box .item .item-btns .item-gray-btn:hover{
    background-image: linear-gradient(#f6f6f6,#ededed);
  }
  .sku-box .item .item-btns .item-blue-btn{
    background-color: #5c85e5;
    background-image: linear-gradient(#779ae9,#5078df);
    border: 1px solid #5c81e3;
    color: #fff;
    margin-left: 10px;
  }
  .sku-box .item .item-btns .item-blue-btn:hover{
    border: 1px solid #5374c8;
    background-color: #5074db;
    background-image: linear-gradient(#6e8ed5,#4769c2);
  }
  .sku-box .item .item-btns .item-blue-btn:active{
    border: 1px solid #3e61d7;
    background-color: #5c85e5;
    background-image: linear-gradient(#4d72de,#6189e6);
  }


  .sku-box .item:hover .item-price{
    opacity: 0;
    transition: all .1s ease-out;
  }
  .sku-box .item .discount-icon{
    display: none;
  }
  .sku-box .item .item-cover a{
    display: block;
    position: absolute;
    left: 0;
    top: 0;
    z-index: 20;
    width: 100%;
    height: 310px;
  }

  .sort-option{
    border-top: 1px solid #D8D8D8;
    color: #999;
  }
  .gray-box{
    overflow: hidden;
    background: #fff;
    border-radius: 8px;
    border: 1px solid #dcdcdc;
    border-color: rgba(0,0,0,.14);
    box-shadow: 0 3px 8px -6px rgba(0,0,0,.1);

  }


  .store-content {
    width: 1250px;
    min-height: 600px;
    padding: 0 0 25px;
    top: 12%;
    left: 5%;
    position: relative;
  }
  .sku-box .item {
    position: relative;
    float: left;
    border-right: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    width: 25%;
    height: 429px;
    background: #fff;
    box-sizing: border-box;
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
    left: 25%;
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
    top: 7px;
    left: 93%;
    border: none;
    outline: none;
    cursor: pointer;
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
  .delete_button{
    position: absolute;
    top: 5%;
    left: 73%;
  }
  * {
    list-style:none;
    padding:0;
    margin:0;
    text-decoration:none;
    font-family:'Microsoft YaHei';
  }
  .content{
    position: absolute;
    top: 10%;
    left: 12%;
  }
  .type {
    width: 800px;
    position:relative;
    overflow:hidden;
  }
  .type li {
    border-right: 1px solid #888;
    position:relative;
    float:left;
    width:200px;
    height:53px;
    text-align:center;
    font-size: 25px;
    font-weight: bold;
    line-height:50px;
    color: #000;
  }
  .bg {
    height:100%;
    position:absolute;
    top:0;
    left:0;
    overflow:hidden;
    height:48px;
    width:200px;
    border-bottom-width: thick;
    border-bottom-color: #ed757a;
    border-bottom-style: solid;
  }
  .type li.active{
    color: #ed757a;
    font-weight: bold;
  }
  .tab .tab_list{
    display: none;
  }
  .tab .tab_list p{
    margin-top: 20px;
    text-align: center;
  }
  .content1{
    border: 1px solid #ccc;
    width: 400px;
    height: 370px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 120px;
    left: 8%;
  }
  .content2{
    border: 1px solid #ccc;
    width: 400px;
    height: 370px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top:120px;
    left: 53%;
  }
  .content3{
    border: 1px solid #ccc;
    width: 400px;
    height: 370px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 510px;
    left: 8%;
  }
  .content4{
    border: 1px solid #ccc;
    width: 400px;
    height: 370px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 510px;
    left: 53%;
  }
  .content5{
    border: 1px solid #ccc;
    width: 400px;
    height: 370px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 900px;
    left: 8%;
  }
  .content6{
    border: 1px solid #ccc;
    width: 400px;
    height: 370px;
    margin: 0;
    padding: 0;
    border-radius: 10px;
    position: absolute;
    top: 900px;
    left: 53%;
  }
  .book{
    width: 210px;
    height: 210px;
    position: absolute;
    top: 2%;
    left: 20%;
  }
  .intro{
    width: 360px;
    height: 120px;
    border-radius: 15px;
    position: absolute;
    left: 4%;
    top: 63%;
  }
  .text{
    float: left;
    width: 305px;
    margin-left: 25px;
    overflow: hidden;
  }
  .page{
    margin: 0 auto;
    position: absolute;
    bottom: 1%;
    left: 33%;
    text-align: center;
  }
  .page a,.page span{
    margin-left: 12px;
  }
  .page .num{
    display: inline-block;
    color: #333;
    text-decoration: none;
    width: 32px;
    height: 32px;
    text-align: center;
    line-height: 32px;
  }
  .page .next{
    display: inline-block;
    color: #333;
    text-decoration: none;
    width: 50px;
    height: 32px;
    text-align: center;
    line-height: 32px;
  }
  .page .on{
    background: #ED757A;
    color: #FFFFFF;
    border-radius: 100%;
  }
  .page .num:hover{
    background: #ED757A;
    color: #FFFFFF;
    border-radius: 100%;
  }
  .page .prev{
    color: #999;
    cursor: text;
  }
</style>
