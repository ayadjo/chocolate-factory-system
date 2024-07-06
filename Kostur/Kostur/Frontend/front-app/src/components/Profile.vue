<template>
    <div v-if="user" class="user-profile">
      <div class="header-section">
        <div class="edit-button-container">
            <button class="edit-button" @click="toggleDropdown">
              <img src="../assets/settings.png" alt="Edit Icon" />
            </button>
            <ul v-if="showDropdown" class="dropdown-menu">
                <li><button class="edit-profile-button" @click="navigateToEditProfile"> <i class="fas fa-pencil-alt"></i>Edit</button></li>
                <li><button class="edit-profile-button" v-if="userRole == 'Admin'" @click="navigateToAllUsers"> <i class="fas fa-user"></i>Users</button></li>
                <li><button class="factory-profile-button" v-if="userRole == 'Manager' && user.factory.id != -1" @click="navigateToFactory"> <i class="fas fa-industry search-icon"></i>Factory</button></li>
                <li><button class="purchases-button" v-if="userRole == 'Customer' " @click="navigateToPurchases(userId)"> <i class="fas fa-shopping-cart search-icon"></i>Purchases</button></li>
            </ul>
          </div>
        <div class="profile-pic">
          <img src="../assets/user.png" alt="User Image" />
        </div>
        <div class="username">{{ user.username }}</div>
      </div>
      <div class="personal-info">
        <h2>Personal Information</h2>
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
            <span class="info-value">{{ user.birthday }}</span>
        </div>
        <div v-if="userRole == 'Manager' || userRole == 'Employee'" class="info-item">
            <span class="info-label">FACTORY</span>
            <span class="info-value">{{ user.factory.name }}</span>
        </div>
        <div v-if="userRole == 'Customer' " class="info-item">
            <span class="info-label">CUSTOMER TYPE</span>
            <span class="info-value">{{ user.type.name }}</span>
        </div>
        <div v-if="userRole == 'Customer' " class="info-item">
            <span class="info-label">POINTS</span>
            <span class="info-value">{{ user.points }}</span>
        </div>
      </div>
    </div>
    <div v-else>
      <p>Loading user information...</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  
  const user = ref(null);
  const userId = localStorage.getItem('loggedUserId');
  const userRole = localStorage.getItem('userRole');
  const showDropdown = ref(false);
  const router = useRouter();
  
  const fetchUser = async () => {
    try {
      const response = await fetch(`http://localhost:8080/WebShopAppREST/rest/users/getUser/${userId}`);
      if (response.ok) {
        const userData = await response.json();
        if (userData.birthday) {
          const dateParts = userData.birthday.split('T')[0].split('-'); 
          const formattedDate = `${dateParts[0]}-${dateParts[1]}-${dateParts[2]}`; 
          userData.birthday = formattedDate;
          user.value = userData;
        } else {
          console.error('Empty or null value received for birthday field');
        }
      } else {
        console.error('Failed to fetch user data:', response.status);
      }
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };
  
  onMounted(() => {
    fetchUser();
  });

  const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const navigateToEditProfile = () => {
  if (userId) {
    router.push({ name: 'editProfile', params: { id: userId } });
  } else {
    console.error('User ID not found');
  }
};

const navigateToAllUsers = () => {
  if (userRole == 'Admin') {
    router.push('/all-users');
  } else {
    console.error('User role is not Admin!');
  }
};


const navigateToFactory = () => {
  if (user.value && user.value.factory && user.value.factory.id) {
    router.push({ name: 'factory', params: { id: user.value.factory.id } });
  } else {
    console.error('Factory ID not found or user is not a manager');
  }
};

const navigateToPurchases = (userId) => {  
  router.push({ name: 'purchases', params: { id: userId }})
}
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
    margin-bottom: 50px;
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
    font-size: 1.1vw;
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
    width: 120px;
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
    display: inline-block;
    white-space: nowrap;
  }
  
  .dropdown-menu li {
    list-style-type: none;
    width: 100%;
    margin-bottom: 2px;
  }
  
  .dropdown-menu li a {
    display: block;
    padding: 10px;
    text-decoration: none;
    color: #333;
    cursor: pointer;
  }
  
  .purchases-button .fas,
  .factory-profile-button .fas,
  .edit-profile-button .fas{
    color: white;
    margin-right: 10px
  }
  
  .edit-profile-button {
    background-color: #8f0710; 
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    width: 90%;
    margin-bottom: 3px;
  }
  
  .edit-profile-button:hover{
    background-color: white; 
    color: #503216;
    border: 1px solid #8f0710;
  }
  
  .purchases-button:hover .fas,
  .factory-profile-button:hover .fas,
  .edit-profile-button:hover .fas{
    color:#503216
  }
  
  .factory-profile-button{
    background-color: #8f0710; 
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    width: 90%;
    margin-bottom: 3px;
  }
  
  .factory-profile-button:hover{
    background-color: white; 
    color: #503216;
    border: 1px solid #8f0710;
  }

  .purchases-button {
    background-color: #8f0710; 
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    width: 90%;
    margin-bottom: 3px;
  }
  
  .purchases-button:hover{
    background-color: white; 
    color: #503216;
    border: 1px solid #8f0710;
  }
  
  </style>
  