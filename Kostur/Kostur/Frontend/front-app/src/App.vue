<template>
  <div>
    <nav>
      <div class="nav-container">
        <div @click="toggleMenu" class="menu-button">Menu</div>
        <div v-if="menuOpen" class="dropdown-menu">
          <router-link to="/">Home</router-link>
          <router-link v-if="isLoggedIn !== null" @click="handleLogoutOrRedirect" :to="logoutOrSignInLink">
            {{ isLoggedIn ? 'Logout' : 'Sign In' }}
          </router-link>
          <router-link v-if="!isLoggedIn" to="/registration">Sign Up</router-link>
        </div>
        <div class="navbar-middle">
          <img src="./assets/cake.png" alt="Logo" class="navbar-logo"/>
          <label class="navbar-text">Chocolate Factory System</label>
        </div>
        <div class="navbar-right">
          <router-link to="/basket" class="cart-button" v-if="isLoggedIn">
            <i class="fas fa-shopping-cart cart-icon"></i>
            <span class="cart-count">{{ cartItemCount }}</span>
          </router-link>
        </div>
      </div>
    </nav>
    <router-view/>
  </div>
</template>


<script setup>
  import { useRouter } from 'vue-router';
  import { ref, onMounted, watch } from 'vue';
  import axios from 'axios';

  const router = useRouter();
  const isLoggedIn = ref(null);
  const menuOpen = ref(false);
  const cartItemCount = ref(0);

  const checkLoggedIn = () => {
    const userId = localStorage.getItem('loggedUserId');
    isLoggedIn.value = userId ? true : false;
    if (userId != null) {
      fetchBasketData();
    } 
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

  const fetchBasketData = async () => {
    try {
      const userId = localStorage.getItem('loggedUserId');
      if (userId.value !== null) {
        const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/baskets/${userId}`);
        const basket = response.data;
        cartItemCount.value = basket.items.length;
      }
    } catch (error) {
      console.error('Error fetching basket data:', error);
    }
  };

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
    padding: 20px;
    border: 1px solid #8f0710;
    border-radius: 5px;
    background-color: #201d0e;
    display: flex;
    justify-content: center;
  }

  .nav-container {
    display: flex;
    align-items: center;
    width: 100%;
    position: relative;
  }


  nav a {
    margin-right: 20px;
    color: rgb(220, 204, 180);
  }

  nav a.router-link-exact-active {
    color: #b3141e;
  }

  .menu-button {
    margin-left: 20px;
    cursor: pointer;
    color: rgb(220, 204, 180);
    text-align: left;
  }

  .dropdown-menu {
    width: auto;
    position: absolute;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px; /* Added padding */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    z-index: 1;
    top: 100%;
    left: 0;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
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

  .navbar-middle {
    display: flex;
    align-items: center;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
  }
  
  .navbar-logo {
    height: 40px;
    width: 40px;
    object-fit: cover;
    margin-right: 5px; 
  }
  
  .navbar-text {
    color: rgb(220, 204, 180);
    font-size: 13px;
  }
  
  .navbar-right {
    display: flex;
    align-items: center;
    position: absolute;
    right: 1%;
    transform: translateX(40%);
}

.cart-icon {
  font-size: 22px;
}

.cart-count {
  background-color: white;
  color: black;
  font-size: 9px;
  border-radius: 45%;
  padding: 2px 6px;
  margin-left: -25px;
}



</style>
