<template>
  <div>
    <nav>
      <router-link to="/">Home</router-link>
      <router-link v-if="isLoggedIn !== null" @click="handleLogoutOrRedirect" :to="logoutOrSignInLink">
        {{ isLoggedIn ? 'Logout' : 'Sign In' }}
      </router-link>
      <router-link v-if="!isLoggedIn" to="/registration">Sign Up</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script setup>
  import { useRouter } from 'vue-router';
  import { ref, onMounted, watch } from 'vue';

  const router = useRouter();
  const isLoggedIn = ref(null);

  const checkLoggedIn = () => {
    const userId = localStorage.getItem('loggedUserId');
    isLoggedIn.value = userId ? true : false;
  };

  const handleLogoutOrRedirect = () => {
    if (isLoggedIn.value) {
      handleLogout();
    } else {
      router.push('/login');
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('loggedUserId');
    isLoggedIn.value = false;
    router.push('/');
  };

  const logoutOrSignInLink = ref('/');

  watch(isLoggedIn, (newValue) => {
    logoutOrSignInLink.value = newValue ? '/' : '/login';
  });

  onMounted(() => {
    checkLoggedIn();
  });
</script>

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
    border: 1px solid #8f0710;
    border-radius: 5px;
    background-color: #201d0e;
  }

  nav a {
    font-weight: bold;
    margin-right: 10px;
    color: rgb(220, 204, 180);
  }

  nav a.router-link-exact-active {
    color: #b3141e;
  }
</style>
