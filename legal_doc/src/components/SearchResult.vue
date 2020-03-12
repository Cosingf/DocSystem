<template>

  <div class="item"  @mouseover="selectStyle(item) ">

    <div class="text">
      <h6 style="text-align: left;">{{item.question}}</h6>
      <h6 style="text-align: left;margin-top: 20px;">{{item.answer}}</h6>
      <h6 style="text-align: left;margin-top: 20px;">{{item.link}}</h6>
    </div>
  </div>

</template>

<script>
export default {
  name: 'SearchResult',
  props: {
    item: {
      type: Object
    },
    index: {
      type: Number
    }

  },
  data () {
    return {
      showEditBox: false,
      textarea: ''// 双向绑定编辑框
    }
  },

  methods: {
    editAnswer () {
      this.showEditBox = true
      this.$refs.bg.style.display = 'block'
      this.$refs.bg.style.width = window.innerWidth.toString() + 'px'
      this.$refs.bg.style.height = window.innerHeight.toString() + 'px'// 设置遮罩层为整个页面
      var s = this.item.answer
      this.textarea = s
    },
    cancel () {
      this.showEditBox = false// 隐藏编辑框
      this.$refs.bg.style.display = 'none'// 取消遮罩
    },
    save () {
      this.showEditBox = false
      this.$refs.bg.style.display = 'none'
      this.item.answer = this.textarea// 将textarea的值更新到item中
    },
    deleteQA () {
      var box = document.getElementById('main-' + this.index)
      box.remove()
    }

  },
  created () {

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
  .item {
    width: 350px;
    height: 200px;
    border-radius: 1px;
    position: relative;
    top: 10%;
  }
  .text{
    float: left;
    width: 350px;
    height: 200px;
    margin-left: 4%;
    overflow-y: auto
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
