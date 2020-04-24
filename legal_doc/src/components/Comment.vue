<template>
  <div class="discuss-item">
    <div style="font-weight: 600;">{{item.account}}</div>
    <div style="font-size: 15px;line-height: 1.67;" >{{item.content}}</div>
    <span style="font-size: 14px;color:#8590a6;">
      <el-button type="primary" v-bind:class="[likeClass]" plain><i class="el-icon-caret-top"  @click="like">{{likeStatus}}&nbsp;{{likeCount}}</i></el-button>
      <el-button type="primary" v-bind:class="[dislikeClass]" plain><i class="el-icon-caret-bottom" @click="disLike"></i></el-button>      &emsp;
      Released on {{item.createdDate}}&emsp;
      <i class="el-icon-chat-round"></i>0 comments
    </span>
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
        likeCount:'0',
        likeStatus:'agree',
        likeClass: '',
        dislikeClass:''
      }),
      created(){
        this.likeCount=this.item.likeCount
        this.initLikeStatus()
      },
      methods:{
        gotoDiscussDetail(){
          this.$router.push({name: 'DiscussDetail'});
        },
        initLikeStatus(){
          if(this.item.likeStatus==1){
            this.likeStatus='agreed'
            this.likeClass="is-disabled"
            // $(".like-button").attr("class","el-button like-button el-button--primary is-plain is-disabled")
          }else if(this.item.likeStatus==-1){
            this.dislikeClass="is-disabled"
          }else if(thie.item.likeStatus==0){
            this.dislikeClass=''
            this.likeClass=''
          }
        },
        //like dislike按钮状态更新待处理
        like(){
          this.$axios({
            method: 'POST',
            url: '/apis/like',
            params: {
              commentId:this.item.id
            }
          })
          .then(response => {
            this.likeCount=response.data
            this.likeStatus='agreed'
            this.likeClass="is-disabled"
            this.dislikeClass=""
            console.log("after like new likeCount:"+this.likeCount)
          }).catch(error => {
            console.log(error)
          })
        },
        disLike(){
          this.$axios({
            method: 'POST',
            url: '/apis/dislike',
            params: {
              commentId:this.item.id
            }
          })
          .then(response => {
            this.likeCount=response.data
            this.likeStatus='agree'
            this.likeClass=""
            this.dislikeClass="is-disabled"
            console.log("after dislike new likeCount:"+this.likeCount)
          }).catch(error => {
            console.log(error)
          })
        }
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
