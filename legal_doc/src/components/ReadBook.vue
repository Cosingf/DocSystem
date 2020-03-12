<template>
  <div class="read-book">
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
      <a class="myicon" style="color:#007bff">Reedpeer</a>
      <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
      <el-menu-item index="2" @click="goToMyLibrary">My library</el-menu-item>
      <el-input placeholder="Please enter keywords" prefix-icon="el-icon-search" class="my-input" v-model="input"></el-input>
      <el-button type="primary" v-on:click="searchBooks()">Find books</el-button>
      <el-dropdown  trigger="click" >
        <el-avatar icon="el-icon-user-solid"  class="el-dropdown-link" shape="square" size="medium"></el-avatar>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item ><router-link  to="/users/info" class="router-link">Settings</router-link></el-dropdown-item>
          <el-dropdown-item > <router-link  to="/" class="router-link">Sign out</router-link></el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-menu>
    <div class="white-panel">
      <div style="height: 20px;"></div>
      <p style="font-weight:normal;font-size:24px;margin:0 70px;color:#586069;">{{this.bookname}}</p>
      <p style="color:#909399;margin:5px 72px;">{{this.author}}</p>
      <el-button type="success" @click="showWiki" style="margin-top: -45px;margin-left: 600px;" plain>Show Wiki Annotation</el-button>
      <el-button type="success" @click="enhance" style="margin-top: -45px;margin-left: 800px;" plaindata-toggle="modal" data-target="#myModal" plain>Enhancn text</el-button>
      <el-divider></el-divider>
      <div class="drag-box" id="dragBox" >
        <el-scrollbar style="height: 200% ">
          <div class="wrapper" id="pdf-container" @mouseup="tooltip($event)" >
            <div  v-for="i in totals" :id="`page-${i}`" :key="i" class="pdf-box" >
              <canvas :id="'canvas-pdf-' + i" class="canvas-pdf" ></canvas>
            </div>
          </div>
        </el-scrollbar>
      </div>
       <!--显示文本测试内容，正式运行时注释掉这段，用上一段-->
       <!-- <div @mouseup="tooltip($event)"  class="pdf-canvas" id="pdf-canvas" >
        Now the Spring Festival has passed and the new semester is coming soon.</span> 
      Looking back on the past year, I have finished my small plans, but haven’t made
      any breakthrough. So I make up my mind that I must finish the tasks for the new
      semester. The first plan is to take regular exercise. I like to play computer games
      and sometimes I can't help staying up late, which makes me feel sleepy next day
      in class, so I need to sleep early and then do some sports to improve my efficiency.
      The second plan is to focus more attention to learn English. English is the
      international language, so I must learn it well, not only to make the way
      for study abroad someday, but also for travelling abroad. I need to have the
      strong will to fulfill my goals.

      I have a sister. She is younger than me. My sister has a special talent. She sings
      very well. Every time when she starts to sing, people will be quiet and listen to her
      singing. Sometimes I feel jealous, but I have to admit that her voice is so nice. I
      am so proud of being her sister, because we share the same families.
      Now the Spring Festival has passed and the new semester is coming soon.
      Looking back on the past year, I have finished my small plans, but haven’t made
      any breakthrough. So I make up my mind that I must finish the tasks for the new
      semester. The first plan is to take regular exercise. I like to play computer games
      and sometimes I can't help staying up late, which makes me feel sleepy next day
      in class, so I need to sleep early and then do some sports to improve my efficiency.
      The second plan is to focus more attention to learn English. English is the
      international language, so I must learn it well, not only to make the way 
      for study abroad someday, but also for travelling abroad. I need to have the
      strong will to fulfill my goals      
    
    </div> -->
      <!--划词搜索的弹出框-->
      <div id="tooltip"  ref="tip">
        <el-button @click="selectSearch()" icon="el-icon-search" circle></el-button>
        <!-- <el-button @click="selectSearch()" type="primary">Search</el-button> -->
      </div>

      <el-drawer
        title=""
        :visible.sync="drawer"
        :direction="direction"
        :modal="modal"
        size="30%"
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
      <div style="height: 70px;"></div>
    </div>
    <div class="pagination"><el-pagination layout="prev, pager, next" :total="100"></el-pagination></div>
  </div>
</template>
<script>
import PDFJS from 'pdfjs-dist'
import { TextLayerBuilder } from 'pdfjs-dist/web/pdf_viewer'
import 'pdfjs-dist/web/pdf_viewer.css'
import $ from 'jquery'
import SearchResult from '@/components/SearchResult'
import EnhanceResult from './EnhanceResult'
export default {
  data () {
    return {
      input: '',
      activeName: '0',
      activeIndex: '0',
      accordion: true,
      totals: [],
      isCreatedPage: [],
      viewHeight: 0,
      currentPageNo: 1,
      idName: 'canvas-pdf-',
      scale: 1.5,
      i: 1,
      pdf: localStorage.getItem('bookId'),
      bookId: localStorage.getItem('id'),
      bookname: localStorage.getItem('bookname'),
      author: localStorage.getItem('author'),
      textContent:'',
      drawer: false,
      direction: 'rtl',
      modal: false,
      selectShown: false,
      enhancedShown: false,
      results: [{
        question: '',
        answer: '',
        link: '',
        sectionContent: ''
      }],
      wikiAnnotaion: [{
        keyword: '',
        title: '',
        url: '',
        summary: '',
        pageNum:''
      }],
      enhancedResults: [
        {
          answer: '',
          question: '',
          link: '',
          sectionContent: '',
          sectionId: '',
          sectionNum: '',
          questionId: '',
          answerId: ''
        }
      ]
    }
  },
  components: {
    EnhanceResult,
    SearchResult
  },
  mounted () {
    this.renderPdf(this.scale)
    this.$refs.tip.style.display = 'none'
    this.initWiki()
  },
  watch: {
    scale (val) {
      this.totals = []
      this.renderPdf(val)
    }
  },
  methods: {
    handleCommand (command) {
      this.$message('click on item ' + command)
    },
    goToMyLibrary () {
      this.$router.push({ name: 'MyBooks' })
    },
    goToPublicLibrary () {
      this.$router.push({ name: 'PublicBooks' })
    },
    initWiki () {
      this.$axios({
        method: 'GET',
        url: '/apis/read/wiki/' + this.bookId
      })
        .then(response => {
          {
            this.wikiAnnotaion = response.data
          }
        }).catch(error => {
          console.log(error)
        })
    },
    // 显示wiki Annotation
    showWiki () {
      let doc=$("#pdf-canvas").html()
      console.log("doc:"+doc)
      console.log(this.wikiAnnotaion.length)
      this.wikiAnnotaion.forEach(function(element) {
        if(element.pageNum==1){
          console.log("wiki:"+element.keyword)
          let key=element.keyword
          let replaceReg = new RegExp(key, 'g');
          let replaceString = '<span style="cursor:pointer;background-color: #fdf6ec;color: #e6a23c;" name="test">'+key+'</span>'
          
          doc=doc.replace(replaceReg,replaceString)
        }
        // console.log("replaceReg"+replaceReg)
      });
      $("#pdf-canvas").html(doc)
      $("#pdf-canvas").on("click","span",function(){
        console.log("on click")
      });
      // let canvas = document.getElementById(this.idName + 1)
      // let ctx = canvas.getContext('2d')
      // ctx.font = 'bold 16px Arial' //文字样式：加粗 16像素 字体Arial
      // ctx.fillStyle = '#F09000' //字体颜色
      // ctx.fillText('The', 40, 35) //fillText里面的可填写的值(文本内容, x坐标, y坐标, 文本最大宽度)
      // for (let i = 1; i <= this.textContent.items.length; i++) {
      //   let txt=this.textContent.items[i].str
      //   ctx.fillText(txt)
      //   console.log(txt)
      //   console.log(txt.length)
      // }
      // console.log(this.textContent.items.length)
    },
    // 打开弹框
    tooltip (event) {
      var x = 10
      var y = 10
      // 获取选中的文本
      if (document.selection) {
        this.content = document.selection.createRange().text
      } else if (window.getSelection()) {
        this.content = window.getSelection().toString()
      }
      // 弹框
      if (this.content != '') {
        this.$refs.tip.style.top = (event.pageY + y) + 'px'
        this.$refs.tip.style.left = (event.pageX + x) + 'px'
        this.$refs.tip.style.opacity = '1'
        this.$refs.tip.style.width = '219px'
        this.$refs.tip.style.height = '33px'
        this.$refs.tip.style.position = 'absolute'
        this.$refs.tip.style.display = 'block'
      } else {
        this.$refs.tip.style.display = 'none'
      }
    },
    renderPdf (scale) {
      PDFJS.workerSrc = require('pdfjs-dist/build/pdf.worker.min')
      // 当 PDF 地址为跨域时，pdf 应该已流的形式传输，否则会出现pdf损坏无法展示
      PDFJS.getDocument('/apis/' + this.bookname, { params: {
        withCredentials: true,
        dataType: 'jsonp'
      } }).then(pdf => {
        this.pdf = pdf
        // 得到PDF的总的页数
        this.totalPage = pdf.numPages
        // 根据总的页数创建相同数量的canvas
        this.createCanvas(this.totalPage, this.idName)
        this.createPage(1, scale)
        this.isCreatedPage[1] = 1
        for (let j = 2; j <= this.totalPage; j++) {
          this.isCreatedPage[j] = 0
        }
      })
    },
    createCanvas (totalPages) {
      for (let i = 1; i <= totalPages; i++) {
        this.totals.push(i)
      }
    },
    createPage (i, scale) {
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
          textLayerDiv.setAttribute('id', 'textLayer')
          // 将文本图层div添加至每页pdf的div中
          pageDiv.appendChild(textLayerDiv)
          // 创建新的TextLayerBuilder实例
          let textLayer = new TextLayerBuilder({
            textLayerDiv: textLayerDiv,
            pageIndex: page.pageIndex,
            viewport: viewport
          })
          textLayer.setTextContent(textContent)
          //储存文本数据
          this.textContent=textContent
          textLayer.render()
        })
      })
    },
    selectSearch () {
      this.drawer = true
      this.$refs.tip.style.display = 'none'
      this.selectShown = true
      this.enhancedShown = false
      this.$axios({
        method: 'POST',
        url: '/apis/read/highlight',
        params: {
          content: this.content,
          bookId: localStorage.getItem('id'),
          pageNum: this.currentPageNo
        }
      })
        .then(response => {
          this.results = response.data
        }).catch(error => {
          console.log(error)
        })
    },

    // 显示增强结果
    enhance () {
      this.$refs.tip.style.display = 'none'// 隐藏弹框
      this.enhancedShown = true
      this.selectShown = false
      this.$axios({
        method: 'POST',
        url: '/apis/read/enhance',
        params: {
          bookId: localStorage.getItem('id'),
          pageNum: this.currentPageNo
        }
      })
        .then(response => {
          this.enhancedResults = response.data
        }).catch(error => {
          console.log(error)
        })
      this.drawer = true// 弹出抽屉
    },
    searchBooks: function () {
      var searchContent = this.input
      this.$axios({
        method: 'POST',
        url: '/apis/publicbooks/search/' + searchContent

      })
        .then(response => {
          this.books = response.data
        }).catch(error => {
          console.log(error)
        })
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
    border-radius: 2px;
    border: 1px solid #dcdcdc;
    border-color: rgba(0,0,0,.14);
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
    height: 10000px;
    padding: 0 0 25px;
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
  .sku-box .item {
    position: relative;
    float: left;
    border-right: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    width: 500px;
    height:200px;
    background: #fff;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
  }
</style>
