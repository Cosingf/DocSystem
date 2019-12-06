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
        name: "SearchResult",
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
            textarea:'',//双向绑定编辑框
          }
      },


      methods:{
          editAnswer(){
            this.showEditBox=true;
            this.$refs.bg.style.display="block";
            this.$refs.bg.style.width=window.innerWidth.toString()+ "px";
            this.$refs.bg.style.height=window.innerHeight.toString()+ "px";//设置遮罩层为整个页面
            var s=this.item.answer;
            this.textarea=s;
          },
        cancel(){
          this.showEditBox=false;//隐藏编辑框
          this.$refs.bg.style.display="none";//取消遮罩
        },
        save(){
          this.showEditBox=false;
          this.$refs.bg.style.display="none";
          this.item.answer=this.textarea;//将textarea的值更新到item中
        },
        deleteQA(){
          var box = document.getElementById("main-"+this.index);
          box.remove();
        }


      },
      created(){

      },

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
