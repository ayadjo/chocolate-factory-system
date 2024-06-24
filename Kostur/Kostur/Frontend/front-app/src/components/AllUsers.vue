<template>
    <div class="all-users">
      <div 
        class="user-card" 
        v-for="user in allUsers" 
        :key="user.id"
        :class="[
          user.role === 'Manager' ? 'manager-card' : '',
          user.role === 'Customer' ? 'customer-card' : '',
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
  </template>
  
  
  
  
  <script setup>
  import { ref, onMounted } from 'vue';
  
  const allUsers = ref([]);
  const adminId = localStorage.getItem('loggedUserId');
  
  const fetchAllUsers = async () => {
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
  
  onMounted(() => {
    fetchAllUsers();
  });
 

</script>

<style scoped>
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
  background-color: #201d0e;
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
  background-color: #8f0710;
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
</style>
