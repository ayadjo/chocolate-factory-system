<template>
  <nav>
    <router-link  to="/">Home</router-link> 
    <!--<router-link to="/about">About</router-link>-->
  </nav>
  <router-view/>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
  border: 1px solid #b94289;
  border-radius: 5px;
  background-color: #201d0e;
}

nav a {
  font-weight: bold;
  color:rgb(220, 204, 180)
}



nav a.router-link-exact-active {
  color: #ec60b4;
}
</style>
