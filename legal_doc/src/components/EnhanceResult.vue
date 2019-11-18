<template>
  <div class="item" @mouseover="selectStyle(item) ">
    <div>
      <!--<div id="bookInfo.id" class="content1" style="background-color: rgba(255,255,255,0.8);">-->
      <div class="intro" >
        <div class="text">
          <h6 style="text-align: left;">{{item.qa.question}}</h6>
          <h6 style="text-align: left;margin-top: 20px;">{{item.qa.answer}}</h6>
          <h6 style="text-align: left;margin-top: 20px;">{{item.qa.link}}</h6>
        </div>
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
        }
      },
    data: () => ({

      itemIndex:0
    }),
      created(){

    },
      methods:{
          selectStyle(item){


           /* console.log(item.section.sectionContent)*/
           /* var itemContent = document.getElementById(textLayer).innerText;
            if(itemContent.indexOf(item.searchContent) > -1){
              // 设置该元素的背景色
              document.getElementById(itemName).style.backgroundColor="#295";
            }*/
          }
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
  .sku-box .item {
    position: relative;
    float: left;
    border-right: 1px solid #efefef;
    border-bottom: 1px solid #efefef;
    width: 25%;
    height: 429px;
    background: #fff;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
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

  .intro{
    width: 500px;
    height: 220px;
    border-radius: 15px;
    position: absolute;
    left: 4%;
    top: 12%;
  }
  .text{
    float: left;
    width: 500px;
    height: 220px;
    margin-left: 25px;
    overflow-y: auto
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
