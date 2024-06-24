<template>
    <div class="container">
        <div class="filters-container">
            <h3><i class="fas fa-sliders-h"></i> Filters</h3>
            <div class="filter-item">
              <label class="filter-label">User Role</label>
              <hr>
              <div class="filterBy" v-for="role in roles" :key="role">
                <div class="radio-group">
                  <input type="radio" :id="role" :value="role" v-model="selectedRole" />
                  <label :for="role">{{ role }}</label>
                </div>
              </div>
            </div>
            <div class="filter-item">
              <label class="filter-label">Customer Type</label>
              <hr>
              <div class="filterBy" v-for="type in types" :key="type">
                <div class="radio-group">
                  <input type="radio" :id="type" :value="type" v-model="selectedType" />
                  <label :for="type">{{ type }}</label>
                </div>
              </div>
            </div>
            <div class="filter-button">
              <button class="apply-button">Apply</button>
              <button class="delete-button">Remove</button>
            </div>
          </div>
      <div class="right">
        <div class="search-container">
          <div class="search-input-wrapper">
            <input type="text" placeholder="Search by First Name" />
            <i class="material-icons search-icon">person</i>
          </div>
          <div class="search-input-wrapper">
            <input type="text" placeholder="Search by Last Name" />
            <i class="material-icons search-icon">person</i>
          </div>
          <div class="search-input-wrapper">
            <input type="text" placeholder="Search by Username" />
            <i class="material-icons search-icon">account_circle</i>
          </div>
          <button class="search-button"><i class="bi bi-search"></i> Search</button>
        </div>
  
        <div class="sort-container">
          <label for="sort-select">Sort by </label>
          <select id="sort-select">
            <option value="none">None</option>
            <option value="firstName">First Name (Ascending)</option>
            <option value="firstName_desc">First Name (Descending)</option>
            <option value="lastName">Last Name (Ascending)</option>
            <option value="lastName_desc">Last Name (Descending)</option>
            <option value="username">Username (Ascending)</option>
            <option value="username_desc">Username (Descending)</option>
            <option value="points">Points (Ascending)</option>
            <option value="points_desc">Points (Descending)</option>
          </select>
        </div>
  
        <div class="all-users">
          <div
            class="user-card"
            v-for="user in allUsers"
            :key="user.id"
            :class="[
              user.role === 'Manager' ? 'manager-card' : '',
              user.role === 'Customer' && user.type.name === 'Gold' ? 'gold-customer' : '',
              user.role === 'Customer' && user.type.name === 'Silver' ? 'silver-customer' : '',
              user.role === 'Customer' && user.type.name === 'Regular' ? 'regular-customer' : ''
            ]"
          >
            <div class="image-container">
              <img class="user-image" src="../assets/manager.png" alt="User Image" />
              <img
                v-if="user.role === 'Customer' && user.type.name === 'Gold'"
                class="user-icon"
                src="../assets/gold.png"
                alt="Gold Icon"
              />
              <img
                v-if="user.role === 'Customer' && user.type.name === 'Silver'"
                class="user-icon"
                src="../assets/silver.png"
                alt="Silver Icon"
              />
              <img
                v-if="user.role === 'Customer' && user.type.name === 'Regular'"
                class="user-icon"
                src="../assets/bronze.png"
                alt="Regular Icon"
              />
            </div>
            <b class="user-username">{{ user.username }}</b>
            <p class="user-name">{{ user.firstName }} {{ user.lastName }}</p>
            <p class="user-role">{{ user.role }}</p>
            <p class="user-points" v-if="user.role == 'Customer'">points: {{ user.points }}</p>
            <button class="block-button" v-if="user.role != 'Admin'">Block</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  
  const allUsers = ref([]);
  const roles = ref([]);
  const types = ref([]);
  
  const selectedRole = ref(null);
  const selectedType = ref(null);
  
  // Function to fetch roles from backend
  const fetchRoles = async () => {
    try {
      const response = await fetch('http://localhost:8080/WebShopAppREST/rest/users/roles', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
  
      if (response.ok) {
        const data = await response.json();
        roles.value = data; // Set the fetched roles to the roles ref
      } else {
        console.error('Failed to fetch roles:', response.status);
      }
    } catch (error) {
      console.error('Error fetching roles:', error);
    }
  };
  
  // Function to fetch customer types from backend
  const fetchTypes = async () => {
    try {
      const response = await fetch('http://localhost:8080/WebShopAppREST/rest/types/', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });
  
      if (response.ok) {
        const data = await response.json();
        types.value = data; // Set the fetched types to the types ref
      } else {
        console.error('Failed to fetch types:', response.status);
      }
    } catch (error) {
      console.error('Error fetching types:', error);
    }
  };
  
  // Function to fetch all users from backend
  const fetchAllUsers = async () => {
    const adminId = localStorage.getItem('loggedUserId');
    try {
      const response = await fetch(`http://localhost:8080/WebShopAppREST/rest/users/allUsers/${adminId}`);
      if (response.ok) {
        const users = await response.json();
        allUsers.value = users;
      } else {
        console.error('Failed to fetch users:', response.status);
      }
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };
  
  // Apply filters function
  const applyFilters = () => {
    // Filter logic
  };
  
  // Reset filters function
  const resetFilters = () => {
    // Reset logic
  };
  
  onMounted(() => {
    fetchRoles(); // Fetch roles on component mount
    fetchTypes(); // Fetch types on component mount
    fetchAllUsers(); // Fetch all users on component mount
  });
  </script>
  
  <style scoped>
  .container {
    display: flex;
  }
  
  .filters-container {
    width: 15%;
    box-sizing: border-box;
    margin-top: 20px;
    margin-left: 30px;
  }
  
  .right {
    width: 75%;
    box-sizing: border-box;
    margin-top: 20px;
  }
  
  .search-container {
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
    width: 100%;
    margin: 0 auto;
    margin-top: 0px;
}

.search-input-wrapper {
    position: relative;
    width: 30%;
    margin-top: 10px;
    margin-right: 10px;
  }
  
  .search-input-wrapper input {
    width: 70%;
    padding: 10px 20px 10px 40px;
    border: 1px solid #ddd;
    border-radius: 20px;
    box-sizing: border-box;
    transition: border-color 0.3s;
  }
  
  .search-input-wrapper input:focus {
    outline: none;
    border-color: #8f0710; 
  }
  
  .search-icon {
    position: absolute;
    left: 60px;
    top:50%;
    transform: translateY(-50%);
    color: #aaa;
    pointer-events: none; 
  }
  
  .search-input-wrapper input {
    padding-left: 40px;
  }

  
  .all-users {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .user-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 16px;
    margin: 8px;
    text-align: center;
    width: 200px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 40px;
    transition: transform 0.2s;
    position: relative;
    height: 300px;
  }
  
  .user-card:hover {
    transform: scale(1.05);
  }
  
  .image-container {
    position: relative;
  }
  
  .user-image {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    margin-bottom: 8px;
  }
  
  .user-icon {
    position: absolute;
    top: 50px;
    right: -5px;
    width: 35px;
    height: 35px;
  }
  
  .user-username {
    font-weight: bold;
    margin-bottom: 1px;
  }
  
  .user-name {
    margin-bottom: 0px;
  }
  
  .user-role,
  .user-type,
  .user-points {
    margin-bottom: 0px;
  }
  
  .block-button {
    background-color: #8f0710;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px;
    cursor: pointer;
    transition: background-color 0.3s;
    position: absolute;
    bottom: 16px;
  }
  
  .block-button:hover {
    background-color: #201d0e;
  }
  
  .manager-card {
    background-color: rgb(252, 244, 234);
    border: 1px solid #ccc;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  .customer-card {
    background-color: #fff7e6;
    border: 1px solid #ffa500;
  }
  
  .gold-customer {
    border: 1px solid #ffd700;
    background-color: #fffacd;
  }
  
  .silver-customer {
    border: 1px solid #c0c0c0;
    background-color: #f0f0f0;
  }
  
  .regular-customer {
    border: 1px solid #8b4513;
    background-color: #f5f5dc;
  }
  
  .search-button {
    background-color: #201d0e;
    color: white;
    border: none;
    border-radius: 20px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    width: 13%;
    height: 35px;
    margin-top: 10px;
  }
  
  .search-button:hover {
    background-color: white;
    color: black;
    border: 1px solid #201d0e;
  }
  
  .sort-container {
    margin-top: 20px;
    display: inline-block;
    margin-bottom: 10px;
    transform: translateX(-152%);
  }
  
  .sort-container label {
    font-size: 14px;
    margin-right: 10px;
  }
  
  .sort-container select {
    padding: 8px;
    font-size: 14px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: border-color 0.2s, box-shadow 0.2s;
    cursor: pointer;
    outline: none;
  }
  
  .sort-container select:hover,
  .sort-container select:focus {
    border-color: #8f0710;
    box-shadow: 0 0 6px rgba(1, 10, 19, 0.5);
  }
  
  .sort-container select option {
    padding: 8px;
  }
  
  .filters-container {
    width: 18%;
    box-sizing: border-box;
    margin-top: 20px;
    margin-left: 30px;
    color: #201d0e;
  }

  .filter-item {
    margin-bottom: 40px;
    color: #201d0e;
  }
  
  .filter-label {
      text-align: left;
      display: block;
      margin-bottom: 10px;
      font-weight: bold;
      color: #201d0e;
  }
  
  
  .filter-item input[type="radio"] {
    margin-right: 5px;
  }
  
  
  .filterBy {
    display: flex;
    align-items: center; 
    margin-bottom: 10px;  
    color:#201d0e;
  }
  
  .filterBy label {
    margin-left: 10px; 
    color: #201d0e;
  }
  
  .right {
    width: 75%;
    box-sizing: border-box;
  }

  .apply-button {
    background-color: #f0f0f0; 
    color: #201d0e;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    margin-top: 30px;
    margin-right: 10px;
    width: 60%;
  }
  
  .apply-button:hover {
    background-color: white; 
    color: black;
    border: 1px solid #8f0710;
  }
  
  .delete-button {
    background-color: #f0f0f0; 
    color: #201d0e;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    margin-top: 30px;
    width: 30%;
  }
  
  .delete-button:hover {
    background-color: white; 
    color: black;
    border: 1px solid #8f0710;
  }

  .radio-group {
    display: inline-block;
    margin-right: 1rem;
    font-weight: 100;
    font-size: 1.3vw;
  }
  
  .radio-group input[type="radio"] {
    display: none;
  }
  
  .radio-group label {
    cursor: pointer;
    font-weight: 100;
    font-size: 1.0vw;
  }
  
  .radio-group label:before {
    content: "";
    display: inline-block;
    width: 1em;
    height: 1em;
    border-radius: 50%;
    border: 2px solid #ccc;
    margin-right: 0.5em;
    position: relative;
    top: 0.2em;
    background-color: transparent;
  }
  
  .radio-group input[type="radio"]:checked + label:before {
    background-color: #8f0710;
    border-color: rgb(220, 204, 180);
  }


  </style>
  