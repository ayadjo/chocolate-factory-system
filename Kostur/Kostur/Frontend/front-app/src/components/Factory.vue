<template>
  <div class="factory-detail">
        <div class="header">
          <div class="edit-button-container">
            <button class="edit-button" @click="toggleDropdown">
              <img src="../assets/more.png" alt="Edit Icon" />
            </button>
            <ul v-if="showDropdown" class="dropdown-menu">
              <li><button class="edit-profile-button" v-if="userFactory == factoryId && isManager" @click="navigateToEmployees"><i class="fas fa-user"></i>Employees</button></li>
            </ul>
          </div>
          <h2>{{ factory.name }}</h2>
        </div>
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
    <button v-if="isManager" @click="navigateToPurchases(factory.id)" class="purchases-button"><i class="fas fa-shopping-cart search-icon"></i>Purchases</button>
    
  </div>
  <h3>Chocolates</h3>
  <button @click="addChocolate(factory.id)" class="add-button" v-if="userFactory == factoryId && isManager">
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
          <p><strong>On Stock:</strong> {{ chocolate.onStock }}</p> 
          <div class="card-actions">
            <button @click="updateChocolate(chocolate.id)" v-if="userFactory == factoryId && isManager" class="action-button">
              <i class="fas fa-pencil-alt"></i>
            </button>
            <button @click="openQuantityModal(chocolate)" v-if="userFactory == factoryId && isEmployee"  class="action-button">
              <i class="fas fa-box"></i>
            </button>
            <button @click="deleteChocolate(chocolate.id)" v-if="userFactory == factoryId && isManager" class="action-button">
              <i class="fas fa-trash-alt"></i>
            </button>
            <div v-if="isCustomer">
              <button @click="decrementQuantity(chocolate.id)" class="quantity-button">-</button>
              <input type="number" v-model="quantities[chocolate.id]" class="quantity-input" min="1" disabled>
              <button @click="incrementQuantity(chocolate)" class="quantity-button">+</button>
              <button @click="addToBasket(chocolate, quantities[chocolate.id])" class="action-button">
                <i class="fas fa-cart-plus"></i> 
              </button>
            </div>
            
          </div>
        
      </div>
      
    </div>
    <div class="no-comments">
      <p>No comments available.</p>
      <i class="far fa-comment-alt fa-3x"></i>
    </div>

    <div>
      <h2 class="title">
        Where is {{ factory.name}} located?
        <i class="fas fa-map-marker-alt"></i>
      </h2>
      <div id="map"  ref="savedMapContainer"></div>
    </div>

    
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <span @click="closeModal" class="close">&times;</span>
      <img src="../assets/chocolates.png" class="chocolates-image" alt="Chocolates Image" />
      <h3>Edit Chocolate Quantity</h3>
      <input v-model="editedChocolate.onStock" type="number" min="0" />
      <button @click="updateQuantity" class="edit-quantity-button">Edit</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import '@fortawesome/fontawesome-free/css/all.css';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png'
});

const savedMapContainer = ref(null);
let savedMap = ref(null);
let factoryMarker = ref(null);

const factory = ref({ id: "",name: "", grade: 0, logo: "", location: { address: "", longitude: "", latitude: "" } });
const chocolates = ref([]);
const route = useRoute();
const router = useRouter();
const showModal = ref(false);

const isLoggedIn = ref(null);
const userRole = ref(null);

const quantities = ref({});

const showDropdown = ref(false);

const user = ref(null);
const userId = localStorage.getItem('loggedUserId');
const factoryId = route.params.id;
const userFactory = ref("");

const navigateToEmployees = () => {
  if (isManager) {
    router.push('/employees/' + factoryId);
  } else {
    console.error('User role is not Manager!');
  }
};

const setupMap = () => {
  if (!savedMap.value) {
    savedMap.value = L.map(savedMapContainer.value).setView([factory.value.location.latitude, factory.value.location.longitude], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> doprinosioci'
    }).addTo(savedMap.value);

    factoryMarker.value = L.marker([factory.value.location.latitude, factory.value.location.longitude]).addTo(savedMap.value);
    factoryMarker.value.bindPopup(`<b>${factory.value.name}</b><br>${factory.value.location.address}`).openPopup();
  }
};


const fetchUser = async () => {
    try {
      const response = await fetch(`http://localhost:8080/WebShopAppREST/rest/users/getUser/${userId}`);
      if (response.ok) {
        const userData = await response.json();
        user.value = userData;
        console.log("factryId: ",factoryId)
        userFactory.value = user.value.factory.id;
        console.log("user factory: ",userFactory.value)
      } else {
        console.error('Failed to fetch user data:', response.status);
      }
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };

const toggleDropdown = () => {
  showDropdown.value = !showDropdown.value;
};

const checkLoggedIn = () => {
  const userId = localStorage.getItem('loggedUserId');
    if (userId) {
      axios.get(`http://localhost:8080/WebShopAppREST/rest/users/${userId}`)
        .then(response => {
          const user = response.data;
          isLoggedIn.value = true;
          userRole.value = user.role;
          if(userRole.value == 'Customer'){
              loadChocolatesForCustomer(route.params.id);
            }else {
              loadChocolates(route.params.id);
            }
        })
        .catch(error => {
          console.error('Error fetching user data:', error);
          isLoggedIn.value = false;
          userRole.value = null;
        });
    } else {
      isLoggedIn.value = false;
      userRole.value = null;
    }
};

const editedChocolate = ref({
  id: null,
  onStock: 0
});

const isManager = computed(() => {
    return userRole.value === 'Manager';
  });

const isEmployee = computed(() => {
    return userRole.value === 'Employee';
  });

const isCustomer = computed(() => {
  return userRole.value === 'Customer';
});

onMounted(() => {
  loadFactory(route.params.id);
  fetchUser();
  checkLoggedIn();
});

function loadFactory(id) {
  axios.get(`http://localhost:8080/WebShopAppREST/rest/factories/${id}`)
    .then(response => {
      factory.value = response.data;
      setupMap();
    });
}

function loadChocolates(factoryId) {
  axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/byFactory/${factoryId}`)
    .then(response => {
      chocolates.value = response.data;
      chocolates.value.forEach(chocolate => {
        quantities.value[chocolate.id] = 1; 
      });
    });
}

function loadChocolatesForCustomer(factoryId) {
  axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/byFactory/forCustomer/${factoryId}`)
    .then(response => {
      chocolates.value = response.data;
      chocolates.value.forEach(chocolate => {
        quantities.value[chocolate.id] = 1; 
      });
    });
}

function addChocolate(factoryId) {
  router.push(`/add-chocolate/${factoryId}`);
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


function openQuantityModal(chocolate) {
  editedChocolate.value = { id: chocolate.id, onStock: chocolate.onStock };
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

function updateQuantity() { 
  const id = editedChocolate.value.id;
  const onStock = editedChocolate.value.onStock;

  axios.patch(`http://localhost:8080/WebShopAppREST/rest/chocolates/quantity/${id}/${onStock}`)
    .then(() => {
        alert("Quantity successfully updated! New quantity: " + editedChocolate.value.onStock);
        loadChocolates(route.params.id);    
        closeModal();
      })
      .catch(error => {
        console.error("There was an error updating the quantity!", error);
        alert("Failed to update the quantity.");
      });
  
}

function incrementQuantity(chocolate) {
  if (quantities.value[chocolate.id] < chocolate.onStock) {
    quantities.value[chocolate.id] += 1;
  }
}


function decrementQuantity(chocolateId) {
  if (quantities.value[chocolateId] > 1) {
    quantities.value[chocolateId] -= 1;
  }
}

// function addToBasket(chocolate, quantity) {
//   const userId = localStorage.getItem('loggedUserId'); 
//   if (!userId) {
//     alert("Please log in to add chocolates to the basket.");
//     return;
//   }

//   try {
//     const response = axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/addChocolateToBasket/${userId}/${chocolate.id}/${quantity}`);
//     chocolate.value = response.data;
//     alert(`Added ${quantity} of ${chocolate.name} to the basket.`);
//   } catch (error) {
//     console.error('Error adding chocolate to basket:', error);
//     alert('Failed to add chocolate to the basket. Please try again.');
//   }
// }

async function addToBasket(chocolate, quantity) {
  const userId = localStorage.getItem('loggedUserId'); 
  if (!userId) {
    alert("Please log in to add chocolates to the basket.");
    return;
  }

  try {
    const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/addChocolateToBasket/${userId}/${chocolate.id}/${quantity}`);
    const updatedChocolate = response.data; 

    const chocolateIndex = chocolates.value.findIndex(choc => choc.id === updatedChocolate.id);
    if (chocolateIndex !== -1) {
      chocolates.value[chocolateIndex].onStock = updatedChocolate.onStock;
      chocolates.value[chocolateIndex].status = updatedChocolate.status;
    }


    alert(`Added ${quantity} of ${chocolate.name} to the basket.`);
  } catch (error) {
    console.error('Error adding chocolate to basket:', error);
    alert('Failed to add chocolate to the basket. Please try again.');
  }
}


function navigateToPurchases(factoryId) {
  router.push( { name: 'factoryPurchases', paramas: { id: factoryId}});
}

</script>

<style scoped>

#map {
  margin-top: 30px;
  margin-left: 150px;
  border-radius: 10px;
  border: 1px solid black;
  height: 500px;
  width: 80%;
  margin-bottom: 60px;
}

.title {
  text-align: center;
  font-weight: 100;
  font-size: 1.3vw;
  color: #201d0e;
  margin-top: 100px;
}
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

.header {
  position: relative;
  padding: 20px;
  border-radius: 10px;
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
  width: 160%;
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

.edit-profile-button .fas{
  color: white;
  margin-right: 10px
}

.edit-profile-button{
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  width: 90%;
}

.edit-profile-button:hover{
  background-color: white; 
  color: #503216;
  border: 1px solid #8f0710;
}

.edit-profile-button:hover .fas{
  color:#503216
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

.modal {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal input {
  width: 70%;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
  margin-right: 10px;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 300px;
  text-align: center;
}

.modal-content .close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.modal-content .close:hover,
.modal-content .close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.edit-quantity-button {
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

.edit-quantity-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;

}

.chocolates-image{
  width: 100px;
  height: auto;
  margin: 0 auto 20px;
}

.quantity-selector {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .quantity-button {
    background-color:rgb(220, 204, 180);
    border: none;
    border-radius: 8px;
    width: 30px;
    height: 30px;
    cursor: pointer;
    font-size: 1.2em;
    color: #333;
    transition: color 0.3s; /* Add transition for smoother color change */
    margin: 5px;
  }

  .quantity-button:hover {
    background-color: white;
    border: 1px solid #8f0710;
  }

  .quantity-input {
    width: 50px;
    padding: 5px;
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 14px;
  }

input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
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
    width: 25%;
    margin-top: 15px;
  }
  
  .purchases-button:hover{
    background-color: white; 
    color: #503216;
    border: 1px solid #8f0710;
  }

  .purchases-button .fas{
    color: white;
    margin-right: 5px
  }

  .purchases-button:hover .fas{
    color:#503216
  }
  
</style>
