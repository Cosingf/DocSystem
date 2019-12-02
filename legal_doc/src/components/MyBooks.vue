<template>
  <div id="MyBooks" class="body">
  <div class="top">
    
    <router-link href='#' to="/users/info"><img class="photo" src="../assets/头像.jpg" ></router-link>
    <p id="welcome">Welcome</p>
    <div class="menu">
      <ul class="button-list">
        <!-- <el-radio-group v-model="tabPosition" style="height:35px;">
          <router-link href='#' to="/publicbooks/sixbooks/1"><el-radio-button label="top">Public library</el-radio-button></router-link>
          <el-radio-button label="right" onclick="return false;" >My library</el-radio-button>
       </el-radio-group> -->
        <li><router-link href='#' class='button' to="/publicbooks/sixbooks/1">Public library</router-link></li>
        <li><a href='/' class='choosed_button' onclick="return false;">My library</a></li>
      </ul>
    </div>
    <p id="back">Exit</p>
    <router-link href='#' to="/"><img class="back_image" src="../assets/enter.png"></router-link>
  </div>
  <div class="main">
    <div class="container">
      <form action="" class="parent">
        <input type="text" name="searchContent" id="searchContent" class="search" placeholder="Keywords">
        <input type="button" v-on:click="searchBooks()" name="sub" id="sub" class="btn">
      </form>
    </div>
    <div class="upload_button">
      <button type="button" v-on:click="uploadBookBox(1)" style="width: 150px;height: 45px;font-size: 26px;border-radius: 20px;background-color: #FFA07A;">Upload</button>
    </div>
    <div class="delete_button">
      <el-button type="button" v-on:click="deleteBooks()" style="width: 150px;height: 45px;font-size: 26px;border-radius: 20px;background-color: #FFA07A;">Delete</el-button>
    </div>




    <el-popover
      placement="bottom"
      width="400"
      trigger="click">
      <el-form  :model="uploadBookMsg" :inline="true">
        <el-upload
          action="https://jsonplaceholder.typicode.com/posts/"
          class="upload-demo"
          :before-upload="beforeupload"
          :limit="1"
          >
          <el-button slot="trigger" size="medium" type="primary">Select files</el-button>
          <div slot="tip" class="el-upload__tip">Only support pdf files</div>

        </el-upload>
        <el-form-item :label=uploadBookMsg.file.name>
        </el-form-item>
        <el-form-item label="author name: ">
          <el-input size="small" v-model="uploadBookMsg.authorName"></el-input>
        </el-form-item>
        <el-form-item label="share book: ">
          <el-switch v-model="uploadBookMsg.isPublic"></el-switch>
        </el-form-item>


      </el-form>
      <el-button size="medium" type="primary" @click="uploadBook">Upload</el-button>
      <el-button slot="reference" style=" position: absolute;top: 5%;left: 60%;width: 150px;height: 45px;font-size: 26px;border-radius: 20px;background-color: #FFA07A;">
        Upload</el-button>
    </el-popover>


    <div class="sku-box store-content">
      <div class="sort-option">
      </div>
      <div class="gray-box">
        <div class="item-box" ></div>
        <BookInfo  v-for="item , index in books" :item="item" :key="index"></BookInfo>
      </div>
    </div>


    <div class="page">
      <a href="#" class="prev">prev</a>
      <a href="#" class="on num">1</a>
      <a href="#" class="num">2</a>
      <a href="#" class="num">3</a>
      <a href="#" class="num">4</a>
      <span>...</span>
      <a href="#" class="next">next</a>
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
          ],
            uploadBookMsg:{
                authorName:"",
                file:'',
                isPublic:""
            }
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
          url:'/apis/mybooks/sixbooks/'+this.userId+'/'+this.pageNum
        })
          .then(response=>{
           that.books=response.data
            console.log(that.books)
          }).catch(error=>{
          console.log(error)
        })

      },

      methods:{

        beforeupload(file){
          this.uploadBookMsg.file=file;
          return false;
        },
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
        },  uploadBookBox(n){
            document.getElementById('uploadBox').style.display=n?'block':'none';
        },
        inputFileChange (e){
            // input的@change事件拿到数据
            this.uploadBookMsg.file = e.target.files[0]
        },
        uploadBook(){
            let formData = new FormData()
            var isPublic= 1
            this.uploadBookMsg.isPublic?isPublic=1:isPublic=0;
            formData.append('isPublic',isPublic)
            formData.append('author', this.uploadBookMsg.authorName)
            formData.append('file', this.uploadBookMsg.file)
          console.log(formData.get(""))
            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
            axios.post('/apis/upload/'+localStorage.getItem('userId'),formData,config).then(response => {
                if (response.status===200){
                    this.$notify.success({
                        title: 'upload success！',
                        message: 'upload success！'
                    })
                }
            })
                .catch(error => {
                    this.$notify.error({
                        title: 'upload fail！',
                        message: 'upload fail！'
                    });
                })
        },
        goToPublicLibrary(){
          this.$router.push({name: 'PublicBooks'});
        },
        goToLogin(){
          this.$router.push({name: 'Login'});
        },
         handleClick(tab, event) {
          console.log(tab, event);
        },
        searchBooks(){
          var searchContent= document.getElementById("searchContent").value
          this.$axios({
            method: 'POST',
            url: '/apis/mybooks/' +searchContent +'/'+localStorage.getItem('userId'),

          })
            .then(response=>{
              {
                this.books=response.data
                console.log(books)
              }
            }).catch(error=>{
            console.log(error)
          })
        }
      }
    }
</script>

<style scoped>
  html{
    width: 100%;
  }
  .el-radio-button__inner{
    height:51px;
    font-size:25px;
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
    margin-right: 10px;
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
    left: 15%;
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
  .upload_button{
    position: absolute;
    top: 5%;
    left: 60%;
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
  .box{
    width:50%; margin-top:10%; margin:auto; padding:28px;
    height:350px; border:1px #111 solid;
    display:none;            /* 默认对话框隐藏 */
  }
  .box.show{display:block;}
  .box .x{ font-size:18px; text-align:right; display:block;}
  .box input{width:80%; font-size:18px; margin-top:18px;}
  .info-order-text{
    width: 50px;
    font-family: STHeiti;
    font-size: 15px;
  }
</style>
