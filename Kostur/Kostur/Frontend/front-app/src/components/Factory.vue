<template>
  <div class="factory-detail">
    <h2>{{ factory.name }}</h2>
    <div class="rating">
      {{ factory.grade }} <i class="fas fa-star"></i>
    </div>
    <img :src="factory.logo" alt="Factory Logo" class="logo">
    <p>{{ factory.startTime }} - {{ factory.endTime }}</p>
    <p><strong>Open:</strong> {{ factory.open ? 'true' : 'false' }}</p>
    <div class="location">
      <p>{{ factory.location.address }}</p>
      <p>{{ factory.location.longitude }}</p>
      <p>{{ factory.location.latitude }}</p>
    </div>
    
    
  </div>
  <h3>Chocolates</h3>
  <button @click="addChocolate" class="add-button">
    <i class="fas fa-plus"></i> Add Chocolate
  </button>
    <div class="chocolates">
      <div v-if="chocolates.length === 0" class="no-chocolates">
        <p>No chocolates available.</p>
        <i class="fas fa-cookie-bite fa-5x"></i>
      </div>
      
        <div v-else v-for="chocolate in chocolates" :key="chocolate.id" class="chocolate-card">
          <img :src="chocolate.image" alt="Chocolate Image" class="chocolate-image">
          <h4>{{ chocolate.name }}</h4>
          <p><strong>Type:</strong> {{ chocolate.type }}</p>
          <p><strong>Kind:</strong> {{ chocolate.kind }}</p>
          <p><strong>Price:</strong> ${{ chocolate.price }}</p>
          <p><strong>Weight:</strong> {{ chocolate.weight }}g</p>
          <div class="card-actions">
            <button @click="updateChocolate(chocolate.id)" class="action-button">
              <i class="fas fa-pencil-alt"></i>
            </button>
            <button @click="deleteChocolate(chocolate.id)" class="action-button">
              <i class="fas fa-trash-alt"></i>
            </button>
          </div>
        
      </div>
      
    </div>
    <div class="no-comments">
      <p>No comments available.</p>
      <i class="far fa-comment-alt fa-3x"></i>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter  } from 'vue-router';
import axios from 'axios';
import '@fortawesome/fontawesome-free/css/all.css';

const factory = ref({ name: "", grade: 0, logo: "", location: { address: "", longitude: "", latitude: "" } });
const chocolates = ref([]);
const route = useRoute();
const router = useRouter();

onMounted(() => {
  loadFactory(route.params.id);
  loadChocolates(route.params.id);
});

function loadFactory(id) {
  axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${id}`)
    .then(response => {
      factory.value = response.data;
    });
}

function loadChocolates(factoryId) {
  axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/byFactory/${factoryId}`)
    .then(response => {
      chocolates.value = response.data;
    });
}

function addChocolate() {
  router.push('/add-chocolate');
}

function updateChocolate(id) {
  router.push(`/edit-chocolate/${id}`);
}

async function deleteChocolate(id) {
  try {
    await axios.patch(`http://localhost:8080/WebShopAppREST/rest/chocolates/${id}`);
    alert("Chocolate successfully deleted!")
    loadChocolates(route.params.id);
  } catch (error) {
    console.error('Error deleting chocolate:', error);
  }
}
</script>

<style scoped>
.factory-detail {
  max-width: 600px;
  margin: 20px auto; 
  padding: 16px;
  text-align: center;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: white; 
  height: auto;
}

.rating {
  font-size: 1.2em;
  margin: 8px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.rating .fas {
  color: #FFD700; 
  margin-left: 4px;
}

.logo {
  width: 150px;
  height: auto;
  margin: 16px 0;
}

.location {
  margin-top: 16px;
  font-size: 0.9em;
  color: #666;
}

.location p {
  margin: 4px 0;
}

.chocolates {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 16px;
  justify-content: center;
}

.chocolate-card {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 16px;
  width: 250px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: white; 
  font-size: small;
}

.chocolate-card:hover {
  transform: scale(1.05);
}

.chocolate-image {
  width: 100px;
  height: auto;
  margin-bottom: 8px;
}

.action-button {
  background-color:rgb(220, 204, 180);
  border: none;
  border-radius: 8px;
  width: 40px;
  height: 40px;
  cursor: pointer;
  font-size: 1.2em;
  color: #333;
  transition: color 0.3s; /* Add transition for smoother color change */
  margin: 5px;
}

.action-button:hover {
  background-color: white;
  border: 1px solid #8f0710;
}

.action-button .fas{
  color: #8f0710;
}

.add-button .fas{
  color: white;
}

.add-button{
  margin-top: 20px;
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;

}

.add-button:hover{
  background-color: white; /* Darker shade of primary color */
  color: black;
  border: 1px solid #8f0710;
}

.add-button:hover .fas{
  color:black
}


.no-chocolates .fas{
  color: #503216;
}

.no-comments {
  max-width: 600px;
  margin: 20px auto; 
  padding: 16px;
  text-align: center;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: white; 
  height: auto;
}
</style>
