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
    <button @click="navigateToPurchases(factory.id)" class="purchases-button"><i class="fas fa-shopping-cart search-icon"></i>Purchases</button>
    
  </div>
  <h3>Chocolates</h3>
  <button @click="addChocolate(factory.id)" class="add-button">
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
            <button @click="updateChocolate(chocolate.id)" v-if="isManager" class="action-button">
              <i class="fas fa-pencil-alt"></i>
            </button>
            <button @click="openQuantityModal(chocolate)" v-if="isEmployee"  class="action-button">
              <i class="fas fa-box"></i>
            </button>
            <button @click="deleteChocolate(chocolate.id)" v-if="isManager" class="action-button">
              <i class="fas fa-trash-alt"></i>
            </button>
            <div v-if="isCustomer">
              <button @click="decrementQuantity(chocolate.id)" class="quantity-button">-</button>
              <input type="number" v-model="quantities[chocolate.id]" class="quantity-input" min="1">
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

const factory = ref({ name: "", grade: 0, logo: "", location: { address: "", longitude: "", latitude: "" } });
const chocolates = ref([]);
const route = useRoute();
const router = useRouter();
const showModal = ref(false);

const isLoggedIn = ref(null);
const userRole = ref(null);

const quantities = ref({});



const checkLoggedIn = () => {
  const userId = localStorage.getItem('loggedUserId');
    if (userId) {
      axios.get(`http://localhost:8080/WebShopAppREST/rest/users/${userId}`)
        .then(response => {
          const user = response.data;
          isLoggedIn.value = true;
          userRole.value = user.role;
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
  loadChocolates(route.params.id);
  checkLoggedIn();
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

function addToBasket(chocolate, quantity) {
  const userId = localStorage.getItem('loggedUserId'); // Assuming the user is logged in and their ID is stored in localStorage.
  if (!userId) {
    alert("Please log in to add chocolates to the basket.");
    return;
  }

  try {
    axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/addChocolateToBasket/${userId}/${chocolate.id}/${quantity}`);
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
