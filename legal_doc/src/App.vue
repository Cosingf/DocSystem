<template>
<router-view></router-view>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import ReadBook from "./components/ReadBook";
import UserPage from "./components/UserPage";

export default {
  name: 'app',
  components: {
    UserPage,
    ReadBook,
    HelloWorld
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
