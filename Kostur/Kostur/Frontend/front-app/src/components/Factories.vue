<template>
    <div class="card-container">
      <div v-for="factory in factories" :key="factory.id" class="card">
        <h3>{{ factory.name }}</h3>
        <img :src="factory.logo" alt="Factory Image" class="logo">
        <div class="location">
          <p>{{ factory.location.address }}</p>
          <p>{{ factory.location.longitude }}</p>
          <p>{{ factory.location.latitude }}</p>
        </div>
        <div class="rating">
          {{ factory.grade }} <i class="fas fa-star"></i>
        </div>
        <div class="button-container">
            <button @click="viewFactory(factory.id)" class="read-more-button">See more</button>
          </div>
      </div>
    </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import '@fortawesome/fontawesome-free/css/all.css';

const factories = ref([]);
const router = useRouter();

onMounted(() => {
  loadFactories();
})

function loadFactories() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/factories/')
    .then(response => {
      factories.value = response.data;
    });
}

function viewFactory(id) {
  router.push(`/factory/${id}`);
}
</script>

<style scoped>
.card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
}

.card {
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 280px;
  padding: 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  margin-top: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.card:hover {
  transform: scale(1.05);
}

.card h3 {
  margin-bottom: 8px;
  font-size: 1.2em;
  color: #333;
}

.card img {
  width: 100px;
  height: 100px;
  object-fit: contain;
  margin-bottom: 16px;
}

.location {
  margin-top: auto;
  font-size: 0.9em;
  color: #666;
}

.rating {
  margin-top: 8px;
  font-size: 0.9em;
  color: #333;
  font-weight: bold;
  display: flex;
  align-items: center;
}

.rating .fas {
  color: #FFD700;
  margin-left: 4px;
}

.location p {
  margin: 0;
}

.button-container {
  margin-top: 16px;
}

.read-more-button {
  background-color: #8f0710; /* Primary color */
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
}

.read-more-button:hover {
  background-color: white; /* Darker shade of primary color */
  color: black;
  border: 1px solid #8f0710;
}
</style>
