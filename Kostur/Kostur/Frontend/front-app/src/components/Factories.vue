<template>
 <div class="container">
    <div class="filters-container">
      <h3><i class="fas fa-filter"></i> Filters</h3>
      <div class="filter-item">
        <label class="filter-label">Chocolate Type</label>
        <hr></hr>
        <div class="filterBy" v-for="type in chocolateTypes" :key="type">
          <input type="radio" :id="type" :value="type" v-model="selectedType">    
          <label :for="type">{{ type }}</label>       
        </div>
      </div>
      <div class="filter-item">
        <label class="filter-label">Chocolate Kind</label>
        <hr></hr>
        <div class="filterBy" v-for="kind in chocolateKinds" :key="kind">
          <input type="radio" :id="kind" :value="kind" v-model="selectedKind">
          <label :for="kind">{{ kind }}</label>
        </div>
      </div>
      <div class="filter-item">
        <label class="filter-label">Is it open?</label>
        <hr></hr>
        <div class="filterBy">
          <input type="radio" id="open" value="true" v-model="isOpen">
          <label for="open">Yes</label>
        </div>
        <div class="filterBy">
          <input type="radio" id="not-open" value="false" v-model="isOpen">
          <label for="not-open">No</label>
        </div>
      </div>
      <div clas="filter-button">
        <button @click="applyFilters" class="apply-button">Apply</button>
        <button @click="deleteFilters" class="delete-button">Delete</button>
      </div>
      
    </div>

  <div class="right">
    <div class="search-container">     
      <div class="search-input-wrapper">
        <input type="text" v-model="searchQuery.name" placeholder="Search by Factory Name">
        <i class="fas fa-industry search-icon"></i>
      </div>
      <div class="search-input-wrapper">
        <input type="text" v-model="searchQuery.chocolate" placeholder="Search by Chocolate Name">
        <i class="fas fa-cookie search-icon"></i>
      </div>
      <div class="search-input-wrapper">
        <input type="text" v-model="searchQuery.location" placeholder="Search by Location">
        <i class="fas fa-building search-icon"></i>
      </div>
      <div class="search-input-wrapper">
        <input type="number" v-model="searchQuery.grade" placeholder="Search by Average Grade" min="1" max="5">
        <i class="fas fa-star search-icon"></i>
      </div>
      <button @click="search" class="search-button">Search</button>
    </div>

    <div>
      <button @click="sortAscending" class="ascending-button">Sort Ascending</button>
      <button @click="sortDescending" class="descending-button">Sort Descending</button>
    </div>

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
const searchQuery = ref({
  name: '',
  chocolate: '',
  location: '',
  grade: null
});
const chocolateTypes = ref([]); 
const chocolateKinds = ref([]); 
const selectedType = ref(''); 
const selectedKind = ref(''); 
const isOpen = ref(true);


onMounted(() => {
  loadFactories();
  fetchChocolateTypes();
  fetchChocolateKinds();

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

function fetchChocolateTypes() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/chocolates/types')
    .then(response => {
      chocolateTypes.value = response.data;
    });
}

function fetchChocolateKinds() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/chocolates/kinds')
    .then(response => {
      chocolateKinds.value = response.data;
    });
}

function sortAscending() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/factories/sortAscending')
    .then(response => {
      factories.value = response.data;
    });
}

function sortDescending() {
  axios.get('http://localhost:8080/WebShopAppREST/rest/factories/sortDescending')
    .then(response => {
      factories.value = response.data;
    });
}

function search() {
   const params = {
    name: searchQuery.value.name,
    chocolateName: searchQuery.value.chocolate,
    location: searchQuery.value.location,
    grade: searchQuery.value.grade
  };


  axios.get('http://localhost:8080/WebShopAppREST/rest/factories/search', { params })
    .then(response => {
      factories.value = response.data;
    });
}

function applyFilters() {
  const params = {
    chocolateType: selectedType.value,
    chocolateKind: selectedKind.value,
    isOpen: isOpen.value
  };


  axios.get('http://localhost:8080/WebShopAppREST/rest/factories/filter', { params })
    .then(response => {
      factories.value = response.data;
    });
}

function deleteFilters() {
  selectedType.value = '';
  selectedKind.value = '';
  isOpen.value = true;

  loadFactories();
}



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
  width: 85%;
  box-sizing: border-box;
}

.filters-container h3 {
  margin-bottom: 30px;
}


.filter-item {
  margin-bottom: 40px;
}

.filter-label {
    text-align: left;
    display: block;
    margin-bottom: 10px;
    font-weight: bold;
}


.filter-item input[type="radio"] {
  margin-right: 5px;
}


.filterBy {
  display: flex;
  align-items: center; 
  margin-bottom: 10px;  
}

.filterBy label {
  margin-left: 10px; 
}


.search-container {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  width: 90%;
  margin: 0 auto;
  margin-top: 10px;
}

.search-input-wrapper {
  position: relative;
  width: 50%;
  margin-top: 30px;
  margin-right: 10px;
}

.search-input-wrapper input {
  width: 100%;
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
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #aaa;
  pointer-events: none; 
}

.search-input-wrapper input {
  padding-left: 40px;
}

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
  margin-top: 40px;
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

.search-button {
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  margin-top: 30px;
}

.search-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;
}

.read-more-button:hover {
  background-color: white; /* Darker shade of primary color */
  color: black;
  border: 1px solid #8f0710;
}

.apply-button {
  background-color: #8f0710; 
  color: white;
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
  background-color: #8f0710; 
  color: white;
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

.ascending-button {
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  margin-top: 30px;
  margin-right: 10px;
  width: 10%;
}

.ascending-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;
}

.descending-button {
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  margin-top: 30px;
  width: 10%;
}

.descending-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;
}

</style>
