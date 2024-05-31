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
  </template>
  
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute } from 'vue-router';
  import axios from 'axios';
  import '@fortawesome/fontawesome-free/css/all.css';
  
  const factory = ref({name: "", grade:0, logo: "", location: ""});
  const route = useRoute();
  
  onMounted(() => {
    loadFactory(route.params.id);
  })
  
  function loadFactory(id) {
    axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${id}`)
      .then(response => {
        factory.value = response.data;
      });
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
  height: 450px;
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
</style>
