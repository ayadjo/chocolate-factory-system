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
           {{ factory.grade }}<i class="fas fa-star"></i>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import axios from 'axios';
  import { onMounted, ref } from 'vue';
  import '@fortawesome/fontawesome-free/css/all.css';

  
  const factories = ref([]);
  
  onMounted(() => {
    loadFactories();
  })
  
  function loadFactories() {
    axios.get('http://localhost:8080/WebShopAppREST/rest/factories/')
      .then(response => {
        factories.value = response.data;
      });
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
    height: 100px; /* Ensure all images are the same height */
    object-fit: contain; /* Maintain aspect ratio and fit within the height */
    margin-bottom: 16px;
  }
  
  .location {
    margin-top: auto; /* Push the location section to the bottom */
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
    color: #FFD700; /* Gold color for the star */
    margin-right: 4px; /* Space between star and grade */
  }
  
  .location p {
    margin: 0;
  }
  </style>
  