<template>
    <div v-if="user" class="user-profile">
      <div class="header-section">
        <div class="edit-button-container">
            <button class="edit-button" @click="toggleDropdown">
              <img src="../assets/settings.png" alt="Edit Icon" />
            </button>
            <ul v-if="showDropdown" class="dropdown-menu">
              <li><a @click="editProfile">Edit</a></li>
            </ul>
          </div>
        <div class="profile-pic">
          <img src="../assets/user.png" alt="User Image" />
        </div>
        <div class="username">{{ user.username }}</div>
      </div>
      <div class="personal-info">
        <h2>MY PERSONAL INFORMATION</h2>
        <div class="info-item">
          <span class="info-label">NAME</span>
          <span class="info-value">{{ user.firstName }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">SURNAME</span>
          <span class="info-value">{{ user.lastName }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">GENDER</span>
          <span class="info-value">{{ user.gender }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">ROLE</span>
          <span class="info-value">{{ user.role }}</span>
        </div>
        <div class="info-item">
            <span class="info-label">BIRTHDAY</span>
            <span class="info-value">{{ formatDate(user.birthday) }}</span>
          </div>
      </div>
    </div>
    <div v-else>
      <p>Loading user information...</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  
  const user = ref(null);
  const userId = localStorage.getItem('loggedUserId');
  const showDropdown = ref(false);
  
  const fetchUser = async () => {
    try {
      const response = await fetch(`http://localhost:8080/WebShopAppREST/rest/users/getUser/${userId}`);
      if (response.ok) {
        user.value = await response.json();
      } else {
        console.error('Failed to fetch user data');
      }
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };

  const formatDate = (dateString) => {
  const date = new Date(dateString);
  if (isNaN(date)) {
    const parts = dateString.split('T')[0].split('-');
    const year = parts[0];
    const month = parts[1] - 1;
    const day = parts[2];
    return new Date(year, month, day).toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' });
  }
  return date.toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' });
};
  
  onMounted(() => {
    fetchUser();
  });

  const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const editProfile = () => {
  
  console.log('Editing profile...');
};
  </script>
  
  <style scoped>
  .user-profile {
    text-align: center;
    font-family: Arial, sans-serif;
    border: 1px solid #000;
    padding: 20px;
    width: 500px;
    margin: 0 auto;
    margin-top: 50px;
    border-radius: 20px;
    background-color: #f9f9f9;
  }
  
  .header-section {
    position: relative;
    background-color: rgb(252, 244, 234);
    color: #201d0e;
    padding: 20px;
    border-radius: 10px;
  }
  
  .profile-pic {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    margin: 0 auto;
    overflow: hidden;
  }
  
  .profile-pic img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .username {
    font-size: 20px;
    font-weight: bold;
    margin-top: 10px;
  }
  
  .edit-button-container {
    position: absolute;
    top: 10px;
    left: -20px;
    display: inline-block;
  }

  
  .edit-button {
    background: none;
    border: none;
    cursor: pointer;
    width: 100px;
  }
  
  .edit-button img {
    width: 20px;
    height: 20px;
  }
  
  .personal-info {
    font-size: 18px;
    margin-top: 20px;
  }
  
  .personal-info h2 {
    font-weight: 100;
    font-size: 1.3vw;
    color: #201d0e;
    margin-bottom: 20px;
    padding: 10px;
  }
  
  .info-item {
    display: flex;
    justify-content: space-between;
    margin: 10px 0;
    padding: 10px;
    border-bottom: 1px solid #ccc;
  }
  
  .info-label {
    font-weight: 600;
    font-size: 1.1vw;
    color: #201d0e;
  }
  
  .info-value {
    font-size: 1.1vw;
    font-weight: normal;
    color: #201d0e;
  }
  
  .dropdown {
    position: relative;
    display: inline-block;
  }
  
  .dropdown-menu {
    position: absolute;
    top: 100%;
    right: -20px; 
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 5px 0;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    z-index: 1000; 
  }
  
  .dropdown-menu li {
    list-style-type: none;
  }
  
  .dropdown-menu li a {
    display: block;
    padding: 10px;
    text-decoration: none;
    color: #333;
    cursor: pointer;
  }
  
  
  </style>
  