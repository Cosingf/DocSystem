<template>
  <div class="discuss-item">
    <div style="font-weight: 600;">username 2020/2/13</div>
    <div style="font-size: 15px;line-height: 1.67;" >content content content content content content content content content
      content content content content content content content content content
      content content content content content content content content
    </div>
    <span style="font-size: 14px;color:#8590a6;"><i class="el-icon-chat-round"></i>100 comments</span>
  </div>
</template>

<script>
    export default {
      name: "Discuss",

      // props:{
      //   item:{
      //     type:Object
      //   }
      // },
      data: () => ({

      }),
      methods:{
        gotoDiscussDetail(){
          this.$router.push({name: 'DiscussDetail'});
        }
      },
      created(){

      }
    }
</script>

<style scoped>
  .discuss-item{
    margin:10px 40px;
  }
  .my-title{
    font-size: 18px;
    font-weight: 600;
    cursor: pointer;
  }
  .my-title:hover{
    color:#292985;
  }
</style>
