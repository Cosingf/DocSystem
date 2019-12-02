<template>



  <div class="item">
    <div>
        <!--<div id="bookInfo.id" class="content1" style="background-color: rgba(255,255,255,0.8);">-->
          <div class="item-img">
          <img v-on:click="gotoBook(item.path,item.id,item.name,item.author)" :src="item.coverImg" height="300" width="300" style="cursor: pointer;">

          </div>

          <input style="position: absolute;left: 90%;top: 3%;width: 30px;height: 30px;"  type="checkbox" name="check" :value=item.id />
          <div class="intro" v-on:click="gotoBook(item.path,item.id,item.name,item.author)">
            <div class="text">
              <h2 style="text-align: center; cursor: pointer;">{{item.name}}</h2>
              <h6 style="text-align: center;margin-top: 20px">{{item.author}}</h6>
            </div>
          </div>
        </div>
    </div>


</template>

<script>
    export default {
      name: "BookInfo",

      props:{
        item:{
          type:Object
        }
      },
      data: () => ({

      }),
      methods:{
        gotoBook(path,id,name,author){
          localStorage.setItem('bookId',path)
          localStorage.setItem('id',id)
          localStorage.setItem('bookname',name)
          localStorage.setItem('author',author)
          this.$router.push({name: 'ReadBook'});
        }
      },
      created(){

      }
    }
</script>

<style scoped>
  html{
    width: 100%;
  }
  .item-img{
    top: 10px;
    left: 5px;
    position: relative;
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
    width: 360px;
    height: 120px;
    border-radius: 15px;
    position: absolute;
    left: -5%;
    top: 75%;
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
