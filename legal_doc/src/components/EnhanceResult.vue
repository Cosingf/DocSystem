<template>
  <div :id="'main-' + index" class="item" @mouseenter="highlight()" @mouseleave="cancelhighlight()">

    <!--编辑及删除Q&A-->
    <el-button v-on:click="deleteQA()"size="medium" style="float: right;margin-top: 5px;margin-right: 10px;" type="danger" icon="el-icon-delete" circle></el-button>
    <el-button  v-on:click="editAnswer()" size="medium" style="float: right;margin-top: 5px;margin-right: 5px;" type="primary" icon="el-icon-edit" circle></el-button>


    <div class="intro" >
      <div class="text" >

        <!--启用编辑后的遮罩层-->
        <div ref="bg"  style="opacity: 0.7; position:fixed; left: 0px; top:0px; background-color: lightgrey; z-index: 99998;display: none"></div>

        <!--保存和取消编辑-->
       <!-- <el-button v-if="showEditBox==true"  type="primary" round style="left: 30%;top: -80%;position: absolute;z-index: 99999;" v-on:click="save()">save</el-button>
        <el-button v-if="showEditBox==true"  type="primary" round style="left: 60%;top: -80%;position: absolute;z-index: 99999;" v-on:click="cancel()">cancel</el-button>
-->
        <!--编辑框-->
        <el-input  v-model="textarea2"  v-if="showEditBox==true" style="font-size:15px;position: absolute;z-index: 99999;left: 4%;top: 30%;background: #fff;" type="textarea" autosize >
        </el-input>



        <!--Q&A折叠面板-->
        <el-collapse-item  :name=index  >
          <!--自定义标题-->
          <template slot="title">
            <h6>{{item.qa.question}}<br><br>
              original link: {{item.qa.link}}<br>
              {{item.qa.answer.substring(0,87)}}</h6>
          </template>
          <!--隐藏面板-->
          <div style="background-color:lightgoldenrodyellow;font-size:15px;position: absolute;z-index: 9999;left: 5%;width:100%;top: 125%;">{{item.qa.answer.substring(87,item.qa.answer.length)}}</div>
        </el-collapse-item>

      </div>
    </div>
  </div>

</template>

<script>
    export default {
        name: "EnhanceResult",
      props:{
        item:{
          type:Object
        },
        index:{
          type:Number
        }
      },
      data () {
        return{
          showEditBox:false,
          textarea2:'',//双向绑定编辑框
        }
      },

      methods:{
        editAnswer(){
          this.showEditBox=true;
          this.$refs.bg.style.display="block";
          this.$refs.bg.style.width=window.innerWidth.toString()+ "px";
          this.$refs.bg.style.height=window.innerHeight.toString()+ "px";//设置遮罩层为整个页面
          var s=this.item.qa.answer;
          this.textarea2=s;
        },
        cancel(){
          this.showEditBox=false;//隐藏编辑框
          this.$refs.bg.style.display="none";//取消遮罩
        },
        save(){
          this.showEditBox=false;
          this.$refs.bg.style.display="none";
          this.item.qa.answer=this.textarea2;//将textarea2的值更新到item中
        },
        deleteQA(){
          var box = document.getElementById("main-"+this.index);
          box.remove();
        },
        highlight(){
         var text=this.item.section.sectionContent;
          var spanNodes = document.getElementById('textLayer').getElementsByTagName('span');
          var spanTextArr = [];
          for(var i=0; i<spanNodes.length; i++){
            spanTextArr.push(spanNodes[i].innerHTML);
          }
          var start=0;
          for(var i=0; i<spanNodes.length; i++) {
            var spanNode = spanNodes[i];  //段落节点
            var spanText = spanTextArr[i];    //每一段的文字
              if(spanText!=null){
              if (spanText.indexOf(text[0]) != -1) {
                  spanNode.style.backgroundColor="blue";
                start=i+1;
                break;
              }
            }
          }

          for(var j=start;j<spanNodes.length; j++){
            var spanNode = spanNodes[j];
            var spanText = spanTextArr[j];
            if (spanText.indexOf(text[text.length-1]) == -1){

              spanNode.style.backgroundColor="blue";
            }
            else break;
          }

        },
        cancelhighlight(){
          var text=this.item.section.sectionContent;
          var spanNodes = document.getElementById('textLayer').getElementsByTagName('span');
          var spanTextArr = [];
          for(var i=0; i<spanNodes.length; i++){
            var spanNode = spanNodes[i];
            if( spanNode.style.backgroundColor="blue"){
              spanNode.style.backgroundColor="white";
            }
          }
        },



      }
    }
</script>

<style scoped>
  html{
    width: 100%;
  }

  .button-list li {
    float: left;
    margin: 0 50px 0 0;
  }


  li a{
    color: #000000;
    text-align: center;
    padding: 0;
  }

  .bottom ul{
    width: 1400px;
  }
  .bottom li{
    display: inline-block;
    width: 200px;
    float: left;
  }

  * {
    list-style:none;
    padding:0;
    margin:0;
    text-decoration:none;
    font-family:'Microsoft YaHei';
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
  .sku-box .item {

    float: top;
    border-right: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    width: 100%;
    height: 200px;
    background: #fff;
    box-sizing: border-box;
    position: relative;

  }
  .intro{
    width: 500px;
    height: auto;
    border-radius: 15px;
    position: absolute;
    left: 3%;
    top: 40%;


  }
  .text{
    float: left;
    height:auto;
    width: 500px;
    margin-right: 25px;
    margin-left: 25px;
    overflow: visible;
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
