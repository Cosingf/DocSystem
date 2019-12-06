<template>
  <div id="ReadBook" class="body">

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
      <div class="container-fluid" >
      <div class="row">
        <div class="bookname"><h1 style="margin-right:20px;width: 300px;margin-left:60px;margin-top:90px;">{{this.bookname}}</h1></div>
        <div class="bookauthor"><p style="font-size:20px;padding-top:10px;margin-left:0px;width: 300px;margin-top:90px;">{{this.author}}</p></div>
        <div class="addmybook">
          <button type="button" v-on:click="addToMyLibrary()" class="btn btn-light"style="font-size:20px;margin-top:90px;background-color: #ed757a;color:white;border-radius: 10px;" data-toggle="modal" data-target="#myModal">
            Add to My Book
          </button>
          <button type="button" v-on:click="enhance()" class="btn btn-light"style="font-size:20px;margin-top:90px;margin-left:50px;background-color: #ed757a;color:white;border-radius: 10px;" data-toggle="modal" data-target="#myModal">
            Enhance
          </button>
        </div>
       <!-- <button class="btn col-1 offset-3" style="background-color:rgba(255,255,255,0.5);margin-top:10px;margin-left: 800px;"><img src="../assets/add.png"></button>-->
      </div>
    </div>


    <div style="margin-top:30px;">
      <div style="width:60%">

        <button type="button" v-on:click="last()" style="margin-left: 300px;margin-bottom: 10px"> Prev</button>
        <button type="button" v-on:click="next()" style="margin-left: 50px;margin-bottom: 10px"> Next</button>
          <div class="drag-box" id="dragBox" >
            <el-scrollbar style="height: 200% ">
              <div class="wrapper" id="pdf-container" @mouseup="tooltip($event)" >
                <div  v-for="i in totals" :id="`page-${i}`" :key="i" class="pdf-box" >
                  <canvas :id="'canvas-pdf-' + i" class="canvas-pdf" ></canvas>
                </div>
              </div>
            </el-scrollbar>
          </div>
      </div>
    </div>



  </div>

    <!--划词搜索的弹出框-->
    <div id="tooltip"  ref="tip">
      <el-button @click="selectSearch()" type="primary">
      click
    </el-button>
    </div>



    <el-drawer
      title=""
      :visible.sync="drawer"
      :direction="direction"
      :modal="modal"
      size="40%"
      >
         <div class="sku-box store-content">
           <el-scrollbar style="height: 47% ">
        <div class="gray-box" >
          <!--Q&A折叠面板-->
          <el-collapse v-model="activeName"  :accordion="accordion" >
          <SearchResult  v-for="item , index in results" :item="item" :index="index"  v-if="selectShown===true"></SearchResult>
          <EnhanceResult  v-for="item , index in enhancedResults" :item="item"  :index="index" v-if="enhancedShown===true"></EnhanceResult>
          </el-collapse>
        </div>
           </el-scrollbar>
        </div>
    </el-drawer>
  </div>
</template>

<script>
  import "bootstrap/dist/css/bootstrap.css";
  import PDFJS from 'pdfjs-dist';

  import { TextLayerBuilder } from 'pdfjs-dist/web/pdf_viewer'
  import 'pdfjs-dist/web/pdf_viewer.css'
  import $ from 'jquery'
  import SearchResult from '@/components/SearchResult'
  import EnhanceResult from "./EnhanceResult";
    export default {
        name: "ReadBook",
      props: ['pdfUrl'],
        data () {
        return {
          activeName: '0',
          accordion:true,
          pdf: localStorage.getItem('bookId'),
          bookId: localStorage.getItem('id'),
          bookname:localStorage.getItem('bookname'),
          author:localStorage.getItem('author'),
          totalPage: '',
          i: 1,
          content: "",
          idName : 'canvas-pdf-',
          scale: 1.5,
          totals: [],
          isCreatedPage:[],
          currentPageNo :1,
          viewHeight: 0,
          drawer: false,
          direction: 'rtl',
          modal: false,
          selectShown :false,
          enhancedShown: false,
          results:[{
            question:"",
            answer:"",
            link:"",
            sectionContent:"",
          }],
          enhancedResults:[
            {
               qa:{ answer:"",
                 question:"",
                 link:"",},
              section:{sectionContent:"",}

            }
          ]

        }
      },
      components: {
        EnhanceResult,
        SearchResult
      },
      mounted () {
        this.renderPdf(this.scale);
        this.$refs.tip.style.display="none";
        this.enhance();

      },
      watch: {
        scale (val) {
          this.totals = []
          this.renderPdf(val)
        }
      },
      methods:{
        goToPublicLibrary() {
          this.$router.push({name: 'PublicBooks'});
        },
        goToLogin() {
          this.$router.push({name: 'Login'});
        },
        goToMyLibrary(){
          this.$router.push({name: 'MyBooks'});
        },
        addToMyLibrary(){
          var checkedlist=$(":checkbox[name='check']:checked")
          var bookList=checkedlist.parentElement.id
          this.$axios({
            method: 'POST',
            url: '/mybooks/insert',
            data:{
              userId:window.localStorage['userId'],
              booklist:bookList
            }
          })
        },

        //打开弹框
        tooltip(event){
          var x = 10;
          var y = 10;

          //获取选中的文本
          if (document.selection) {
           this.content = document.selection.createRange().text;
          }
          else if (window.getSelection()) {
           this.content = window.getSelection().toString();
          }
          //弹框
          if (this.content!= "") {
            this.$refs.tip.style.top=(event.pageY +  y) + "px";
            this.$refs.tip.style.left=(event.pageX + x) + "px";
              this.$refs.tip.style.opacity="1";
            this.$refs.tip.style.width="219px";
            this.$refs.tip.style.height="33px";
            this.$refs.tip.style.position="absolute";
            this.$refs.tip.style.display="block";
          }

        },


       //上一页
        last(){
          var p= this.currentPageNo
          if(p-1>0){
            let pageDiv = document.getElementById(`page-${p}`);
            pageDiv.style.display="none";
            p = p-1;}
          this.currentPageNo=p
          if(this.isCreatedPage[p]==0){ this.createPage(p,this.scale);this.isCreatedPage[p]=1;}

          let curPageDiv = document.getElementById(`page-${p}`);
          curPageDiv.style.display="block";

        },
        //下一页
        next(){
          var p= this.currentPageNo
          if(p+1<=this.totalPage){
            let pageDiv = document.getElementById(`page-${p}`);
            pageDiv.style.display="none";
            p = p+1;
          }
          this.currentPageNo=p
          if(this.isCreatedPage[p]==0){ this.createPage(p,this.scale);this.isCreatedPage[p]=1;}
          let curPageDiv = document.getElementById(`page-${p}`);
          curPageDiv.style.display="block";

        },


        renderPdf (scale) {
          PDFJS.workerSrc = require('pdfjs-dist/build/pdf.worker.min')
          // 当 PDF 地址为跨域时，pdf 应该已流的形式传输，否则会出现pdf损坏无法展示
          PDFJS.getDocument("/apis/"+this.pdf,{params: {
              withCredentials : true,
              dataType : 'jsonp'
            }}).then(pdf => {
            this.pdf = pdf
            // 得到PDF的总的页数
            this.totalPage = pdf.numPages
            // 根据总的页数创建相同数量的canvas
            this.createCanvas(this.totalPage, this.idName)
              this.createPage(1,scale);
            this.isCreatedPage[1]=1;
            for(let j=2;j<=this.totalPage;j++)
            {
              this.isCreatedPage[j]=0;
            }

          })
        },
        createCanvas (totalPages) {
          for (let i = 1; i <= totalPages; i++) {
            this.totals.push(i)
          }
        },
        createPage(i,scale){
          this.pdf.getPage(i).then((page) => {
            let pageDiv = document.getElementById(`page-${i}`)
            let viewport = page.getViewport(scale)
            let canvas = document.getElementById(this.idName + i)
            let context = canvas.getContext('2d')
            canvas.height = viewport.height
            canvas.width = viewport.width
            this.viewHeight = viewport.height
            let renderContext = {
              canvasContext: context,
              viewport
            }
            // 如果你只是展示pdf而不需要复制pdf内容功能，则可以这样写render
            // page.render(renderContext) 如果你需要复制则像下面那样写利用text-layer
            page.render(renderContext).then(() => {
              return page.getTextContent()
            }).then((textContent) => {
              // 创建文本图层div
              const textLayerDiv = document.createElement('div')
              textLayerDiv.setAttribute('class', 'textLayer')
              textLayerDiv.setAttribute('id','textLayer')
              // 将文本图层div添加至每页pdf的div中
              pageDiv.appendChild(textLayerDiv)
              // 创建新的TextLayerBuilder实例
              let textLayer = new TextLayerBuilder({
                textLayerDiv: textLayerDiv,
                pageIndex: page.pageIndex,
                viewport: viewport
              })
              textLayer.setTextContent(textContent)
              textLayer.render()
            })
          })
        },



        selectSearch(){
          this.drawer = true;
          this.$refs.tip.style.display="none";
          this.selectShown = true;
          this.enhancedShown = false;
          this.$axios({
            method: 'POST',
            url: '/apis/read/highlight',
            data:{
              content:this.content,
              bookId:localStorage.getItem('bookId'),
              pageNum:this.currentPageNo,
            }
          })
            .then(response=>{

             this.results=response.data
            }).catch(error=>{
            console.log(error)
          })
        },


        enhance(){
          this.$refs.tip.style.display="none";//隐藏弹框
          this.enhancedShown = true;
          this.selectShown = false;
          this.$axios({
            method: 'POST',
            url: '/apis/read/'+this.bookId,
          })
            .then(response=>{
              this.enhancedResults=response.data


            }).catch(error=>{
            console.log(error)
          })
          this.drawer = true;//弹出抽屉
        }


      }
    }



</script>

<style scoped>
  html{
    width:60%;
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

  .drag-box {
    height: 800px;
  }
  .pdf-box {
    position: relative;


  }

  .sku-box{
    position: relative;
  }

  .gray-box{
    overflow: visible;
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



  .store-content {
    width: 580px;
    height: 1300px;
    padding: 0 0 25px;
    top: 2%;
    left: 2%;
    position: relative;
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
    top:10%;
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
    text-decoration: none;
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
  .last{
    width: 100%;
    height: 400px;
    background: url("../assets/pink.jpg");
    position: relative;
    margin-top: 26.5%;
  }
  .lastinfo{
    height: 300px;
    width: 1380px;
    border-bottom: 1px solid #888888;
    position: absolute;
    left: 3%;
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
    left: 5%;
  }
  .bottom ul{
    width: 1400px;
  }
  .bottom li{
    display: inline-block;
    width: 200px;
    float: left;
  }
  .seepaper{
    color: #FFFFFF;
  }
  .seepaper:hover{
    color: #FFFFFF;
    text-decoration: underline;
  }
  .seepaper:active{
    color: #FFFFFF;
    text-decoration: underline;
  }






</style>
