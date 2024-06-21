<template>
  <div>
    <nav>
      <div @click="toggleMenu" class="menu-button">Menu</div>
      <div v-if="menuOpen" class="dropdown-menu">
        <router-link v-if="isLoggedIn !== null" @click="handleLogoutOrRedirect" :to="logoutOrSignInLink">
          {{ isLoggedIn ? 'Logout' : 'Sign In' }}
        </router-link>
        <router-link v-if="!isLoggedIn" to="/registration">Sign Up</router-link>
  
      </div>
      <router-link to="/">Home</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script setup>
  import { useRouter } from 'vue-router';
  import { ref, onMounted, watch } from 'vue';

  const router = useRouter();
  const isLoggedIn = ref(null);
  const menuOpen = ref(false);

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

  const toggleMenu = () => {
    menuOpen.value = !menuOpen.value;
  };

  const closeMenu = () => {
    menuOpen.value = false;
  };

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
    padding: 10px;
    border: 1px solid #8f0710;
    border-radius: 5px;
    background-color: #201d0e;
  }

  nav a {
    margin-right: 20px;
    color: rgb(220, 204, 180);
  }

  nav a.router-link-exact-active {
    color: #b3141e;
  }

  .menu-button {
    cursor: pointer;
    color: rgb(220, 204, 180);
    text-align: left;
  }

  .dropdown-menu {
    width: 10%;
    position: absolute;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    z-index: 1;
  }

  .dropdown-menu a {
    display: block;
    color: #333;
    padding: 5px 0;
    text-decoration: none;
  }

  .dropdown-menu a:hover {
    color: #b3141e;
  }
</style>
