<template>
  <div class="book-item">
    <!-- <div class="item-img">
      <img v-on:click="gotoBook(item.path,item.id,item.name,item.author)" :src="item.coverImg" height="300" width="300" style="cursor: pointer;">
    </div>
    <input style="position: absolute;left: 90%;top: 3%;width: 30px;height: 30px;"  type="checkbox" name="check" :value=item.id />
    <div class="intro" v-on:click="gotoBook(item.path,item.id,item.name,item.author)">
      <div class="text">
        <h2 style="text-align: center; cursor: pointer;">{{item.name}}</h2>
        <h6 style="text-align: center;margin-top: 20px">{{item.author}}</h6>
      </div>
    </div> -->
    <el-image style="cursor: pointer;width: 167px; height: 240px" v-on:click="gotoBook(item.path,item.id,item.name,item.author)"  src="https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg"></el-image>
    <div class="intro">
      <p style="font-size:19px;cursor: pointer;color:#3377aa;margin: 0px;"  v-on:click="gotoBook(item.path,item.id,item.name,item.author)">{{item.name}}</p>
      <p style="color: rgb(88, 96, 105);margin-top:0px;">{{item.author}}</p>
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
  .intro{
    position: relative;
    text-align: center;
    margin-left: -50px;
  }
</style>
