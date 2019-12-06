<template>
  <div class="read-book">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
      <a class="myicon" style="color:#007bff">Reedpeer</a>
      <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
      <el-menu-item index="2" @click="goToMyLibrary">My library</el-menu-item>
      <el-input placeholder="Please enter keywords" prefix-icon="el-icon-search" class="my-input" v-model="input"></el-input>
      <el-button type="primary">Find books</el-button>
      <el-dropdown  trigger="click" >
        <el-avatar icon="el-icon-user-solid"  class="el-dropdown-link" shape="square" size="medium"></el-avatar>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="a">Settings</el-dropdown-item>
          <el-dropdown-item command="b"> <router-link  to="/" class="router-link">Sign out</router-link></el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-menu>
    <div class="white-panel">
      <div style="height: 20px;"></div>
      <p style="font-weight:normal;font-size:24px;margin:0 70px;color:#586069;">{{this.bookname}}</p>
      <p style="color:#909399;margin:5px 72px;">{{this.author}}</p>
      <el-button type="success" @click="enhance" style="margin-top: -45px;margin-left: 800px;" plain>Enhancn text</el-button>
      <el-divider></el-divider>
      <!--显示文本-->
      <!-- <div @mouseup="tooltip($event)" v-for="i in totals" :id="`page-${i}`" :key="i" class="pdf-canvas">
        <canvas :id="'canvas-pdf-' + i" class="canvas-pdf" ></canvas> 
      </div> -->
      <!--显示文本测试内容，正式运行时注释掉这段，用上一段-->
      <div @mouseup="tooltip($event)"  class="pdf-canvas">
          测试文本内容，对接好接口，可以显示文本后，注释掉这段，用code中上一段注释掉的代码
           ---------------------------------------------------
          -------------------------------------------------------------
          -------------------------------------------------------------
          --------------------------------------------------------------
          ----------------------------------------------------------------
          -----------------------------------------------------------------
          ----------------------------------------------------------------
      </div>
      <!--划词搜索的弹出框-->
      <div id="tooltip"  ref="tip">
        <el-button @click="selectSearch()" icon="el-icon-search" circle></el-button>
        <!-- <el-button @click="selectSearch()" type="primary">Search</el-button> -->
      </div>
      <el-drawer title="" :visible.sync="drawer" :direction="direction" :modal="modal" size="30%">
        <div class="sku-box store-content">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>QA Name</span>
              <el-button style="float: right; padding: 8px 12px" type="success" plain>Edit</el-button>
            </div>
            <SearchResult class="text item" v-for="item , index in results" :item="item" :index="index"  v-if="selectShown===true"></SearchResult>
            <EnhanceResult class="text item" v-for="item , index in enhancedResults" :item="item"  :index="index" v-if="enhancedShown===true"></EnhanceResult>
          </el-card>
        </div>
    </el-drawer>
    <div style="height: 70px;"></div>
    </div>
    <div class="pagination"><el-pagination layout="prev, pager, next" :total="100"></el-pagination></div>
  </div>
</template> 
<script>
import PDFJS from 'pdfjs-dist';
import { TextLayerBuilder } from 'pdfjs-dist/web/pdf_viewer'
import 'pdfjs-dist/web/pdf_viewer.css'
import $ from 'jquery'
import SearchResult from '@/components/SearchResult'
import EnhanceResult from "./EnhanceResult";
export default {
  data() {
    return {
      activeName: '0',
      activeIndex: '0',
      accordion:true,
      pdf: localStorage.getItem('bookId'),
      bookId: localStorage.getItem('id'),
      bookname:localStorage.getItem('bookname'),
      author:localStorage.getItem('author'),
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
      enhancedResults:[{
        qa:{ answer:"",
          question:"",
          ink:"",},
          section:{sectionContent:"",}
      }]
    };
  },
  components: {
    EnhanceResult,
    SearchResult
  },
  mounted () {
    this.renderPdf(this.scale);
    this.$refs.tip.style.display="none";
    this.initialEnhance();
  },
  watch: {
    scale (val) {
      this.totals = []
      this.renderPdf(val)
    }
  },
  methods: {
    handleCommand(command) {
      this.$message('click on item ' + command);
    },
    goToMyLibrary(){
      this.$router.push({name: 'MyBooks'});
    },
    goToPublicLibrary(){
      this.$router.push({name: 'PublicBooks'});
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
      else{
        this.$refs.tip.style.display="none";
      }
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
        for(let j=2;j<=this.totalPage;j++){
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
    //预调用增强接口
    initialEnhance(){
      this.$refs.tip.style.display="none";//隐藏弹框
      // this.enhancedShown = true;
      // this.selectShown = false;
      this.$axios({
        method: 'POST',
        url: '/apis/read/'+this.bookId,
      }).then(response=>{
        this.enhancedResults=response.data
      }).catch(error=>{
        console.log(error)
      })
    },
    //显示增强结果
    enhance(){
      this.$refs.tip.style.display="none";//隐藏弹框
      this.enhancedShown = true;
      this.selectShown = false;
      this.drawer = true;//弹出抽屉
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
.myicon{
    position: absolute;
    margin-top: 6px;
    font-size: 24px;
    font-weight: 500;
    margin-left: -135px;
}
.read-book >>> .el-input {
    position: absolute;
    width:250px;
    margin-top:9px;
    margin-left:30px;
}    
.read-book >>> .el-input__inner{
    background: #f6f6f6;
    height:33px;
}
.read-book >>> .el-button {
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
.panel-tips >>> .el-button {
    position:absolute;
    font-size:14px;
    padding: 10px 10px;
    margin-top:3px;
    margin-left:460px;
    line-height:13px;
}
.el-avatar{
    position: absolute;
    margin-top:9px;
    margin-left:510px;
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
.el-popper[x-placement^=bottom] {
    margin-top:48px;
    margin-left: 1145px;
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
    overflow: hidden; 
    border-radius: 2px; 
    -webkit-box-shadow: 0 1px 3px rgba(26,26,26,.1); 
    box-shadow: 0 1px 3px rgba(26,26,26,.1); 
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
}
.white-panel>>>.el-divider--horizontal {
    display: block;
    height: 1px;
    width: 850px;
    margin: 15px 65px;
}
.white-panel>>>.el-drawer__header{
  margin-bottom:0px;
}
.pagination >>> .el-pagination{
    margin-left: 570px;
    margin-top:20px;
    color:#586069;
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
.pdf-canvas{
  margin:20px 70px;
  color:#586069;
}
.box-card>>>.el-button{
  margin-top:-2px;
  margin-left:280px;
}
</style>
  