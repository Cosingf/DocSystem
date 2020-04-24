<template>
  <div class="discuss-item">
    <span  class="my-title" @click="gotoDiscussDetail">{{item.title}}</span><br/>
    <div style="font-size: 15px;line-height: 1.67;" >{{item.content}}</div>
    <span style="font-size: 14px;color:#8590a6;cursor: pointer;" @click="gotoDiscussDetail"><i class="el-icon-chat-round"></i>{{item.commentCount}} comments</span>
    <span style="font-size: 14px;color:#8590a6;">&nbsp;&nbsp;&nbsp;&nbsp;{{item.account}}&nbsp;&nbsp;{{item.createdDate}}</span>

  </div>
</template>

<script>
    export default {
      name: "Discuss",

      props:{
        item: {
          type: Object
        }
      },
      data: () => ({

      }),
      methods:{
        gotoDiscussDetail(){
          localStorage.setItem('discussId',this.item.id)
          this.$router.push({name: 'DiscussDetail'});
        }
      },
      created(){
        
      }
    }
</script>

<style scoped>
  .discuss-item{
    margin:0px 30px;
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
