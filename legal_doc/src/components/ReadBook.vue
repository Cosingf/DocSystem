<template>
  <body style="overflow:visible;">
  <div class="read-book" >
    <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="">
      <a class="myicon" style="color:#007bff">Readpeer</a>
      <el-menu-item index="1" @click="goToPublicLibrary">Public library</el-menu-item>
      <el-menu-item index="2" @click="goToMyLibrary">My library</el-menu-item>
      <el-menu-item index="3" @click="goToDiscussHome">Discussion</el-menu-item>
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
    <div class="white-panel" >
      <el-tabs v-model="selectName" type="card" @tab-click="handleClick">
        <el-tab-pane label="Show Wiki Annotaion" name="first" @tab-click="showWiki">
          <div style="height: 20px;"></div>
          <p style="font-weight:normal;font-size:24px;margin:0 70px;color:#586069;">{{this.bookname}}</p>
          <p style="color:#909399;margin:5px 72px;">{{this.author}}</p>
          <el-divider></el-divider>
          <div @mouseup="tooltip($event)"  class="pdf-canvas" id="pdf-canvas" >
            {{legalDoc}}
          </div>
        </el-tab-pane>

        <el-tab-pane label="Enhance Text" name="second" >
          <div style="height: 20px;"></div>
          <p style="font-weight:normal;font-size:24px;margin:0 70px;color:#586069;">{{this.bookname}}</p>
          <p style="color:#909399;margin:5px 72px;">{{this.author}}</p>
          <!--全文增强按钮，绑定enhance方法-->
          <el-button type="success" @click="enhance" style="margin-top: -45px;margin-left: 800px;" plaindata-toggle="modal" data-target="#myModal" plain>Enhancn text</el-button>
          <el-divider></el-divider>
          <!--pdf内容容器，分页展示-->
          <div class="drag-box" id="dragBox" >
            <el-scrollbar style="height: 200% ">
              <div class="wrapper" id="pdf-container" @mouseup="tooltip($event)" >
                <div  v-for="i in totals" :id="`page-${i}`" :key="i" class="pdf-box" >
                  <canvas :id="'canvas-pdf-' + i" class="canvas-pdf" ></canvas>
                </div>
              </div>
            </el-scrollbar>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div style="height: 20px;"></div>
      <!--高亮搜索按钮，绑定selectSearch()方法-->
      <div id="tooltip"  ref="tip">
        <el-button @click="selectSearch()" icon="el-icon-search" circle></el-button>
      </div>
      <!--问答注解侧边栏，用抽屉实现-->
      <el-drawer title="" :visible.sync="drawer" :direction="direction" :modal="modal" size="30%">
        <div class="sku-box store-content">
          <el-scrollbar style="height: 47% ">
            <div class="gray-box" >
              <!--Q&A折叠面板-->
              <el-collapse v-model="activeName"  :accordion="accordion" >
                <!--高亮搜索、全文增强的结果都用该侧边栏展示-->
                <SearchResult  v-for="item , index in results" :item="item" :index="index"  v-if="selectShown===true"></SearchResult>
                <EnhanceResult  v-for="item , index in enhancedResults" :item="item"  :index="index" v-if="enhancedShown===true"></EnhanceResult>
              </el-collapse>
            </div>
          </el-scrollbar>
        </div>
      </el-drawer>
      <div style="height: 70px;"></div>
    </div>
    <!--页码选择器-->
    <div class="pagination"><el-pagination @current-change="currentChangeHandle" :current-page="currentPageNo" layout="prev, pager, next" :total="100"></el-pagination></div>
  </div>
</body>
</template>
<script>
import Vue from 'vue'
import PDFJS from 'pdfjs-dist'
import { TextLayerBuilder } from 'pdfjs-dist/web/pdf_viewer'
import 'pdfjs-dist/web/pdf_viewer.css'
import $ from 'jquery'
import SearchResult from '@/components/SearchResult'
import EnhanceResult from './EnhanceResult'
export default {
  data () {
    return {
      show: false,
      input: '',
      selectName: 'first',
      activeName: '0',
      activeIndex: '0',
      accordion: true,
      totals: [],
      isCreatedPage: [],
      viewHeight: 0,
      currentPageNo: 1,
      totalPage: 10,
      idName: 'canvas-pdf-',
      scale: 1.5,
      i: 1,
      pdf: localStorage.getItem('bookId'),
      bookId: localStorage.getItem('id'),
      bookname: localStorage.getItem('bookname'),
      author: localStorage.getItem('author'),
      textContent: '',
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
      legalDoc: '',
      wikiAnnotaion: [{
        keyword: '',
        title: '',
        url: '',
        summary: '',
        pageNum: ''
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
  created () {
    this.initDocument()
    this.initWiki()
    this.renderPdf(this.scale)
    this.$refs.tip.style.display = 'none'
  },
  watch: {
    scale (val) {
      this.totals = []
      this.renderPdf(val)
    }
  },
  methods: {
    handleClick (tab, event) {
      if (tab.name == 'second') {
        console.log('click tab show wiki')
        // this.$options.methods.showWiki(this.wikiAnnotaion);
      }
    },
    showPopper () {
      this.show = true
    },
    closePopper () {
      this.show = false
    },
    handleCommand (command) {
      this.$message('click on item ' + command)
    },
    currentChangeHandle (val) {
      var p = this.currentPageNo
      if (val > 0 || val <= this.totalPage) {
        let pageDiv = document.getElementById(`page-${p}`)
        pageDiv.style.display = 'none'
        p = val
      }
      this.currentPageNo = p
      if (this.isCreatedPage[p] === 0) { this.createPage(p, this.scale); this.isCreatedPage[p] = 1 }
      let curPageDiv = document.getElementById(`page-${p}`)
      curPageDiv.style.display = 'block'
    },
    goToMyLibrary () {
      this.$router.push({ name: 'MyBooks' })
    },
    goToPublicLibrary () {
      this.$router.push({ name: 'PublicBooks' })
    },
    goToDiscussHome () {
      this.$router.push({ name: 'DiscussHome' })
    },
    initDocument () {
      this.$axios({
        method: 'POST',
        url: '/apis/read/doc/' + this.bookId
      })
        .then(response => {
          {
            this.legalDoc = response.data
            // console.log("legalDoc:"+this.legalDoc)
          }
        }).catch(error => {
          console.log(error)
        })
    },
    initWiki () {
      this.$axios({
        method: 'POST',
        url: '/apis/read/wiki/' + this.bookId
      })
        .then(response => {
          {
            // this.wikiAnnotaion = response.data
            this.$set(this, 'wikiAnnotaion', response.data)
            console.log('get wiki length 1:' + this.wikiAnnotaion.length)
            this.$options.methods.showWiki(this.wikiAnnotaion)
            // this.$options.methods.showWikiPdf(this.wikiAnnotaion)
          }
        }).catch(error => {
          console.log(error)
        })
    },
    // 处理pdf canvas，显示 wiki Annotaion
    showWikiPdf (wikiAnnotaion) {
      // var count=0;
      // // var keywords=[];
	    // // this.wikiAnnotaion.forEach(function (element) {
		  // //   keywords.push(element);
		  // //   // console.log("content keyword:"+element.keyword);
      // // });
      // let doc=$("#pdf-container").html();
      // console.log("doc:"+doc)

      // this.wikiAnnotaion.forEach(function(element) {
      //   if(element.pageNum==1){
      //     console.log("wiki:"+element.keyword+" 对应count:"+count)
      //     let key=element.keyword
      //     let replaceReg = new RegExp(key, 'g');
      //     if(!replaceReg.test(doc)){
      //       return true;//终止本次循环
      //     }
      //     // let replaceString = '<span style="cursor:pointer;background-color: #fdf6ec;color: #e6a23c;" name="test">'+key+'</span>'
      //     let replaceString=
      //       '<span style="cursor:pointer;background-color: #fdf6ec;color: #e6a23c;">'+key+'</span>'
      //     doc=doc.replace(replaceReg,replaceString)
      //     count=count+1
      //   }
      // });

      // function highlight(node,pos,element){
      //   var span = document.createElement("span");
		  //   span.className = "highlighted";
		  //   span.style.color = "black";
		  //   span.style.backgroundColor = "yellow";

      //   var highlighted = node.innerText.splitText(pos);
      //   console.log("Highlighted:"+highlighted);
      //   var afterHighlighted = highlighted.splitText(keyword.length);
      //   console.log("afterHighlighted:"+afterHighlighted);
		  //   var highlightedClone = highlighted.cloneNode(true);

		  //   span.appendChild(highlightedClone);
		  //   highlighted.parentNode.replaceChild(span, highlighted);
      // }

      // function addHighlights(node){
      //   var i;
      //   keywords.forEach(function (element) {
      // 	  var keyword=element.keyword.toLowerCase();
      //     var pos = node.innerText.toLowerCase().indexOf(keyword);
      //     console.log("document:"+node.innerText+"\nkeyword:"+keyword+" pos:"+pos);
      // 	  if (0 <= pos) {
      // 		  highlight(node, pos,element);
      // 		  count=count+1;
      //     }
      //   });
      // }

      // addHighlights(document.body);

      function replacePos (doc, pos, replacetext, len) {
        var str = doc.substr(0, pos) + replacetext + doc.substring(pos + len, doc.length)
        return str
      }
      let that = this
      let doc = $('#textLayer').html()
      console.log('show wiki doc:' + doc)
      // console.log("show wiki length:"+this.wikiAnnotaion.length)
      console.log('wiki Annotation:' + wikiAnnotaion.length)
      var position = []
      wikiAnnotaion.forEach(function (element) {
        if (element.pageNum == 1) {
          // console.log("wiki:"+element.keyword+" 对应count:"+count)
          let key = element.keyword
          let replaceReg = new RegExp(key, 'g')
          var start = 0
          let len = doc.length
          console.log('doc len:' + len)
          var pos = 0
          while (start < len && doc.indexOf(key, start) != -1) {
            pos = doc.indexOf(key, start)
            position.push(pos)
            console.log('start:' + start + ' keyword:' + key + ' position:' + pos)
            var textlen = element.summary.length - 5
		        var minlen = Math.min(textlen, 290)
		        var index = element.summary.indexOf(' ', minlen)
		        if (index != -1) var text = element.summary.substring(0, index)
		        else text = element.summary.substring(0, 300)
            let replaceString =
            '<el-button  class="popover popover-' + pos + '" style=" position:relative;cursor:pointer;background-color: #e6a23c;">' + key + '</el-button>' +
              '<div  role="tooltip" aria-hidden="false" class="my-popover my-popover-' + pos + ' el-popover el-popper el-popover--plain" tabindex="0" style="visibility:hidden;" x-placement="top">' +
              '<div class="el-popover__title">' + element.title + '</div>' +
              text + '...</br>' +
              'Read more: <el-link href="' + element.url + '" class="my-link-' + pos + ' el-link el-link--primary is-underline">' + element.url + '</el-link>' +
              '<div x-arrow="" class="popper__arrow" ></div>' +
            '</div>'
            // let replacetext = '<span style="cursor:pointer;background-color: #fdf6ec;color: #e6a23c;" name="test">'+key+'</span>'
            // console.log("replacetext len:"+replacetext.length)
            start = start + pos + replaceString.length + 1
            doc = replacePos(doc, pos, replaceString, key.length)
            len = doc.length
            // console.log("new doc:"+doc)
          }
        }
      })
      $('#textLayer').html(doc)
      position.forEach(function (pos) {
        // console.log("iteratre position:"+pos)
        setTimeout(() => {
        // 获取位置
        // console.log("count: "+i)
          console.log('父元素位置：' + $('.popover-' + pos).offset().left + ',' + $('.popover-' + pos).offset().top)
          console.log('子元素位置：' + $('.my-popover-' + pos).offset().left + ',' + $('.my-popover-' + pos).offset().top)
          // 设置提示框位置
          let left = $('.popover-' + pos).offset().left - 450
          let top = $('.popover-' + pos).offset().top - 320
          $('.my-popover-' + pos).attr('style', 'visibility:hidden;' + 'top:' + top + 'px;left:' + left + 'px;')
          // 使url link生效
          let url = $('.my-link-' + pos).attr('href')
          $('.my-link-' + pos).click(function () {
            window.location.href = url
          })
          $('.popover-' + pos).click(function () {
            let style = $('.my-popover-' + pos).attr('style')
            if (style == 'visibility:visible;' + 'top:' + top + 'px;left:' + left + 'px;') {
              $('.my-popover-' + pos).attr('style', 'visibility:hidden;' + 'top:' + top + 'px;left:' + left + 'px;')
            } else {
              $('.my-popover-' + pos).attr('style', 'visibility:visible;' + 'top:' + top + 'px;left:' + left + 'px;')
            }
          })
        }, 10000)
      })
    },
    // 显示wiki Annotation
    showWiki (wikiAnnotaion) {
      function replacePos (doc, pos, replacetext, len) {
        var str = doc.substr(0, pos) + replacetext + doc.substring(pos + len, doc.length)
        return str
      }
      let that = this
      let doc = $('#pdf-canvas').html()
      console.log('show wiki doc:' + doc)
      // console.log("show wiki length:"+this.wikiAnnotaion.length)
      console.log('wiki Annotation:' + wikiAnnotaion.length)
      var position = []
      wikiAnnotaion.forEach(function (element) {
        if (element.pageNum !=0) {
          // console.log("wiki:"+element.keyword+" 对应count:"+count)
          let key = element.keyword
          let replaceReg = new RegExp(key, 'g')
          var start = 0
          let len = doc.length
          console.log('doc len:' + len)
          var pos = 0
          while (start < len && doc.indexOf(key, start) != -1) {
            pos = doc.indexOf(key, start)
            position.push(pos)
            console.log('start:' + start + ' keyword:' + key + ' position:' + pos)
            var textlen = element.summary.length - 5
		        var minlen = Math.min(textlen, 290)
		        var index = element.summary.indexOf(' ', minlen)
		        if (index != -1) var text = element.summary.substring(0, index)
		        else text = element.summary.substring(0, 300)
            let replaceString =
            '<el-button  class="popover popover-' + pos + '" style=" position:relative;cursor:pointer;background-color: #fdf6ec;color: #e6a23c;">' + key + '</el-button>' +
              '<div  role="tooltip" aria-hidden="false" class="my-popover my-popover-' + pos + ' el-popover el-popper el-popover--plain" tabindex="0" style="visibility:hidden;" x-placement="top">' +
              '<div class="el-popover__title">' + element.title + '</div>' +
              text + '...</br>' +
              'Read more: <el-link href="' + element.url + '" class="my-link-' + pos + ' el-link el-link--primary is-underline">' + element.url + '</el-link>' +
              '<div x-arrow="" class="popper__arrow" ></div>' +
            '</div>'
            // let replacetext = '<span style="cursor:pointer;background-color: #fdf6ec;color: #e6a23c;" name="test">'+key+'</span>'
            // console.log("replacetext len:"+replacetext.length)
            start = start + pos + replaceString.length + 1
            doc = replacePos(doc, pos, replaceString, key.length)
            len = doc.length
            // console.log("new doc:"+doc)
          }
        }
      })
      $('#pdf-canvas').html(doc)
      position.forEach(function (pos) {
        // console.log("iteratre position:"+pos)
        setTimeout(() => {
        // 获取位置
        // console.log("count: "+i)
          console.log('父元素位置：' + $('.popover-' + pos).offset().left + ',' + $('.popover-' + pos).offset().top)
          console.log('子元素位置：' + $('.my-popover-' + pos).offset().left + ',' + $('.my-popover-' + pos).offset().top)
          // 设置提示框位置
          let left = $('.popover-' + pos).offset().left - 450
          let top = $('.popover-' + pos).offset().top - 91
          $('.my-popover-' + pos).attr('style', 'visibility:hidden;' + 'top:' + top + 'px;left:' + left + 'px;')
          // 使url link生效
          let url = $('.my-link-' + pos).attr('href')
          $('.my-link-' + pos).click(function () {
            window.location.href = url
          })
          $('.popover-' + pos).click(function () {
            let style = $('.my-popover-' + pos).attr('style')
            if (style == 'visibility:visible;' + 'top:' + top + 'px;left:' + left + 'px;') {
              $('.my-popover-' + pos).attr('style', 'visibility:hidden;' + 'top:' + top + 'px;left:' + left + 'px;')
            } else {
              $('.my-popover-' + pos).attr('style', 'visibility:visible;' + 'top:' + top + 'px;left:' + left + 'px;')
            }
          })
        }, 10000)
      })
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
      if (this.content !== '') {
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
        this.totalPage = parseInt(pdf.numPages)
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
          // 储存文本数据
          this.textContent = textContent
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
  /* change */
  .myicon{
  position: absolute;
  margin-top: 6px;
  font-size: 24px;
  font-weight: 500;
  margin-left: -185px;
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
 /* change */
.el-avatar{
  position: absolute;
  margin-top:9px;
  margin-left:440px;
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
/* change */
.el-popper[x-placement^=bottom] {
  margin-top:48px;
  margin-left: 1185px;
}
  .router-link{
    color: #606266;
    text-decoration: none;
  }
  .router-link:hover{
    color: #66b1ff;
  }
  .white-panel{
    min-height: 550px;
    width:1000px;
    margin:0 auto;
    margin-top: 13px;
    background: #fff;
    overflow: visible;
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
    margin:40px 80px;
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
.white-panel>>>.el-popper .popper__arrow, .el-popper .popper__arrow::after {
    position: absolute;
    display: block;
    width: 0;
    height: 0;
    border-color: transparent;
    border-style: solid;
}
.white-panel>>>.el-popper .popper__arrow {
    border-width: 6px;
    -webkit-filter: drop-shadow(0 2px 12px rgba(0, 0, 0, .03));
    filter: drop-shadow(0 2px 12px rgba(0, 0, 0, .03));
}
.white-panel>>>.el-popper[x-placement^=top] .popper__arrow {
    top: -6px;
    left: 46%;
    margin-right: 3px;
    border-top-width: 0;
    border-bottom-color: #ebeef5;
}
.white-panel>>>.el-popper[x-placement^=top] .popper__arrow::after {
    top: 1px;
    margin-left: -6px;
    border-top-width: 0;
    border-bottom-color: #fff;
}
.white-panel>>>.el-popper .popper__arrow::after {
    content: " ";
    border-width: 6px;
}
.white-panel>>>.el-popper .popper__arrow, .el-popper .popper__arrow::after {
    position: absolute;
    display: block;
    width: 0;
    height: 0;
    border-color: transparent;
    border-style: solid;
}
.pdf-canvas  >>> .el-button {
    border:none;
    font-size: 16px;
    padding:10px 5px;
}
.pdf-canvas >>> .el-popover{
  width:410px;
  max-width: 42%;
}
.pdf-canvas{
  line-height:40px;
  font-size:17px;
}
.my-popover{
  width: 200px;
  transform-origin: center top;
  z-index: 2027;
  position: absolute;
  top: 207px;
  left: 244px;

}
.white-panel >>> .el-tabs__nav{
  margin-left:690px;
}
.white-panel >>> .el-tabs__content{
  overflow: visible;
}
</style>
