<template>
 <div class="container">
    <div class="filters-container">
      <h3><i class="fas fa-filter"></i> Filters</h3>
      <div class="filter-item">
        <label class="filter-label">Chocolate Type</label>
        <hr></hr>
        <div class="filterBy" v-for="type in chocolateTypes" :key="type">
          <div class="radio-group">
            <input type="radio" :id="type" :value="type" v-model="selectedType">    
            <label :for="type">{{ type }}</label>   
          </div>    
        </div>
      </div>
      <div class="filter-item">
        <label class="filter-label">Chocolate Kind</label>
        <hr></hr>
        <div class="filterBy" v-for="kind in chocolateKinds" :key="kind">
          <div class="radio-group">
            <input type="radio" :id="kind" :value="kind" v-model="selectedKind">
            <label :for="kind">{{ kind }}</label>
          </div>
        </div>
      </div>
      <div class="filter-item">
        <label class="filter-label">Is it open?</label>
        <hr></hr>
        
          <div class="filterBy">
            <div class="radio-group">
              <input type="radio" id="open" value="true" v-model="isOpen">
              <label for="open">Yes</label>
            </div>
          </div>
          <div class="filterBy">
            <div class="radio-group">
              <input type="radio" id="not-open" value="false" v-model="isOpen">
              <label for="not-open">No</label>
            </div>
          </div>
        
      </div>
      <div clas="filter-button">
        <button @click="combinedSearchFilterSort" class="apply-button">Apply</button>
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
      <button @click="combinedSearchFilterSort" class="search-button"><i class="bi bi-search"></i>Search</button>
    </div>


    <div>
      <!--<select v-model="sortOrder" class="sort-order">
        <option value="asc">Sort Ascending</option>
        <option value="desc">Sort Descending</option>
      </select>-->
      <div class="sort-container">
        <label for="sort-select">Sort by </label>
        <select id="sort-select" @change="handleSortChange">
          <option value="none">None</option>
          <option value="factoryName">Factory Name (Ascending)</option>
          <option value="factoryName_desc">Factory Name (Descending)</option>
          <option value="location">Location (Ascending)</option>
          <option value="location_desc">Location (Descending)</option>
          <option value="grade">Grade (Ascending)</option>
          <option value="grade_desc">Grade (Descending)</option>
        </select>
      </div>
      <!--<button @click="sortAscending" class="ascending-button">Sort Ascending</button>
      <button @click="sortDescending" class="descending-button">Sort Descending</button>-->
      <!--<button @click="combinedSearchFilterSort" class="combined-button">Search, Filter & Sort</button>-->
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
const isOpen = ref(null);
const sortOrder = ref('asc');
const sortAttribute = ref('');


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
  isOpen.value = null;

  loadFactories();
}

function combinedSearchFilterSort() {
  const params = {
    name: searchQuery.value.name,
    chocolateName: searchQuery.value.chocolate,
    location: searchQuery.value.location,
    grade: searchQuery.value.grade,
    chocolateType: selectedType.value,
    chocolateKind: selectedKind.value,
    isOpen: isOpen.value,
    sortOrder: sortOrder.value,
    attribute: sortAttribute.value
  };
  console.log('Params to be sent:', params);
  console.log('Sort attribute:', sortAttribute, 'Sort order:', sortOrder);
  axios.get('http://localhost:8080/WebShopAppREST/rest/factories/combined', { params })
    .then(response => {
      factories.value = response.data;
    });
}

/*const handleSortChange = async (event) => {
  const sortBy = event.target.value;
  if (sortBy !== 'none') {
    const [attribute, order] = sortBy.split('_');
    sortFactories(attribute, order || 'asc');
  } else {
    loadFactories();
  }
};*/

const handleSortChange = async (event) => {
  const sortBy = event.target.value;
  if (sortBy !== 'none') {
    const [attribute, order] = sortBy.split('_');
    sortOrder.value = order || 'asc';
    sortAttribute.value = attribute;
    console.log('Sort attribute:', sortAttribute.value, 'Sort order:', sortOrder.value);
    combinedSearchFilterSort();
  } else {
    sortOrder.value = 'asc';
    sortAttribute.value = '';
    loadFactories();
  }
}


const sortFactories = async (attribute, order) => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/sortBy/${attribute}/${order}`);
    if (response && response.data) {
      factories.value = response.data;
    }
  } catch (error) {
    console.error(`Error sorting purchases by ${attribute}:`, error);
  }
};


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
    background-color: #201d0e;
    color: white;
    border: none;
    border-radius: 20px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    width: 20%;
    height: 35px;
    margin-top: 30px;
  }

  .search-button i {
    margin-right: 10px;
  }
  
  .search-button:hover {
    background-color: white;
    color: black;
    border: 1px solid #201d0e;
  }



.read-more-button:hover {
  background-color: white; /* Darker shade of primary color */
  color: black;
  border: 1px solid #8f0710;
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
  width: 12%;
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
  margin-right: 10px;
  width: 12%;
}

.descending-button:hover {
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

  .radio-group-open {
    display: flex;
    flex-direction: column;
    margin-right: 1rem;
    font-weight: 100;
    font-size: 1.1vw;
  }
  
  .radio-group input[type="radio"] {
    display: none;
  }

  .radio-group-open input[type="radio"] {
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

  .radio-group-open input[type="radio"]:checked + label:before {
    background-color: #8f0710;
    border-color: rgb(220, 204, 180);
  }

.sort-order {
  padding: 8px;
  border-radius: 4px;
  margin-top: 30px;
  margin-right: 10px;
  font-size: 0.9em;
  width: 12%;
}

.combined-button {
  background-color: #201d0e;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  margin-top: 30px;
  width: 15%;
}

.combined-button:hover {
  background-color: white;
  color: black;
  border: 1px solid #201d0e;
}

.sort-container {
    margin-top: 20px;
    display: inline-block;
    margin-bottom: 10px;
    transform: translateX(-5%);
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

</style>
