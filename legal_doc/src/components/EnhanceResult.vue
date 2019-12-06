<template>
  <div :id="'main-' + index" class="item" >

    <!--编辑及删除Q&A-->
    <!-- <el-button v-on:click="deleteQA()"size="medium" style="float: right;margin-top: 5px;margin-left: 510px;" type="danger" icon="el-icon-delete" circle></el-button>
    <el-button  v-on:click="editAnswer()" size="medium" style="float: right;margin-top: 5px;margin-left: 460px;" type="primary" icon="el-icon-edit" circle></el-button> -->

    <div class="intro" >
      <div>test QA</div>
      <div>test QA</div>
      <div>test QA</div>
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
          this.item.qa.answer=this.textarea2;//将textarea的值更新到item中
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
            var spanNode = spanNodes[i];  
            var spanText = spanTextArr[i];    
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
.intro{
  width: 500px;
  height: auto;
  border-radius: 15px;
  left: 3%;
  top: 40%;
  color: #586069;
  font-size:15px;
}
</style>
