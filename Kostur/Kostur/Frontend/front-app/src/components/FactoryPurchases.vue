<template>
    <div class="purchases">
        <div class="purchase-card"
            v-for="purchase in purchases"
            :key="purchase.id" >

            <div class="image-container">
                <img 
                    v-if="purchase.status === 'Processing'"
                    class="status-image"
                    src="../assets/processingPurchase.png" 
                    alt="Icon" 
                />
                <img 
                    v-if="purchase.status === 'Approved'" 
                    class="status-image" 
                    src="../assets/approvedPurchase.png" 
                    alt="Icon" 
                />
                <img 
                    v-if="purchase.status === 'Rejected'" 
                    class="status-image" 
                    src="../assets/rejectedPurchase.png" 
                    alt="Icon"  
                />
                <img 
                    v-if="purchase.status === 'Cancelled'" 
                    class="status-image" 
                    src="../assets/processingPurchase.png"  <//zameniti ikonicu
                    alt="Icon" 
                />
            </div>

            <p class="user-name" v-if="isManager"><strong>{{ purchase.user.firstName }} {{ purchase.user.lastName }}</strong></p>
            <p class="purchase-date">Date: {{ purchase.purchaseDateAndTime }}</p>
            <p class="purchase-price">Price: ${{ formatPrice(purchase.price) }}</p>
            <button class="details-button" @click="showDetails(purchase)">Show Details</button>
        </div>

    </div>


    <div v-if="selectedPurchase" class="modal" @click.self="closeModal">
    <div class="modal-content">
      <span class="close-button" @click="closeModal">&times;</span>
      <h4>Purchase Details</h4>
      <table class="styled-table">
        <thead>
          <tr>
            <th>Chocolate Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in selectedPurchase.items" :key="item.id">
            <td>{{ item.chocolate.name }}</td>
            <td>${{ formatPrice(item.chocolate.price) }}</td>
            <td>{{ item.quantity }}</td>
            <td>{{ item.chocolate.description }}</td>
          </tr>
        </tbody>
      </table>

      <div class="status-buttons">
        <button v-if="isManager" class="approve-button" @click="approvePurchase(purchase)">
          <i class="fas fa-check"></i> Approve
        </button>
        <button v-if="isManger" class="reject-button" @click="rejectPurchase(purchase)">
          <i class="fas fa-times"></i> Reject
        </button>
        <button v-if="isCustomer" class="cancel-button" @click="cancelPurchase(purchase)">
          <i class="fas fa-times"></i> Cancel
        </button>
      </div>
      
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const purchases = ref([])
const route = useRoute();
const id = route.params.id;
const selectedPurchase = ref(null);
const userRole = ref(null);

const getPurchases = async (id) => {
  try {
    let response;
    console.log(isManager.value);
    console.log(userRole.value === 'Customer');
    if (isManager.value){
      response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/purchases/factory/${id}`);
    } else if(isCustomer.value) {
      response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/purchases/user/${id}`);
    }
    
    if (response && response.data) {
      purchases.value = response.data;

      purchases.value.forEach(purchase => {
        const date = purchase.purchaseDateAndTime;
        if (date) {
          const dateParts = date.split('T')[0].split('-'); 
          const formattedDate = `${dateParts[0]}-${dateParts[1]}-${dateParts[2]}`;
          purchase.purchaseDateAndTime = formattedDate;
        } else {
          console.error('Empty or null value received for date field');
        }
      });
      console.log("Retrieved all purchases", id);
    }
    
  } catch (error) {
    console.error("There was an error retrieving purchases.", error);
  }
};

 const formatDate = (dateString) => {
  const date = new Date(dateString);
  if (isNaN(date)) {
    const parts = dateString.split('T')[0].split('-');
    const year = parts[0];
    const month = parts[1] - 1;
    const day = parts[2];
    return new Date(year, month, day).toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' });
  }
  return date.toLocaleDateString(undefined, { year: 'numeric', month: 'long', day: 'numeric' });
};

const checkLoggedIn = () => {
  return new Promise((resolve, reject) => {
    const userId = localStorage.getItem('loggedUserId');
    if (userId) {
      axios.get(`http://localhost:8080/WebShopAppREST/rest/users/${userId}`)
        .then(response => {
          const user = response.data;
          userRole.value = user.role;
          resolve();
        })
        .catch(error => {
          console.error('Error fetching user data:', error);
          userRole.value = null;
          reject();
        });
    } else {
      userRole.value = null;
      reject();
    }
  });
};


const showDetails = (purchase) => {
  selectedPurchase.value = purchase;
};

const closeModal = () => {
  selectedPurchase.value = null;
};

const formatPrice = (price) => {
  return parseFloat(price).toFixed(2);
};

const approvePurchase = (purchase) => {
  console.log("Purchase approved:", purchase);
};

const rejectPurchase = (purchase) => {
  console.log("Purchase rejected:", purchase);
};

const cancelPurchase = (purchase) => {
  console.log("Purchase cancelled:", purchase);
};


onMounted(async () => {
    await checkLoggedIn();
    getPurchases(id);
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

</script>

<style scoped>

.purchases {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.purchase-card {
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

.purchase-card:hover {
  transform: scale(1.05);
}

.image-container {
  position: relative;
}

.status-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  margin-bottom: 8px;
}


.user-username {
  font-weight: bold;
  margin-bottom: 1px;
}

.user-name {
  margin-bottom: 0px;
}

.purchase-date,
.purchase-price {
  margin-bottom: 0px;
}

.details-button {
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

.details-button:hover {
  background-color: #8f0710;
}

.status-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  margin-bottom: 10px;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  width: 80%;
  max-width: 600px;
}

.close-button {
  color: #aaa;
  float: right;
  font-size: 18px;
  font-weight: bold;
}

.close-button:hover,
.close-button:focus {
  color: #000;
  text-decoration: none;
  cursor: pointer;
}

.styled-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  font-size: 14px;
  font-family: Arial, sans-serif;
  color: #333;
  border-radius: 8px;
  overflow: hidden;
}

.styled-table th, .styled-table td {
  padding: 10px;
  text-align: center;
  border: none;
}

.styled-table th {
  background: rgb(220, 204, 180);
  color: black;
}

.styled-table tbody tr:nth-of-type(even) {
  background-color: #f3f3f3;
}

.styled-table tbody tr:last-of-type {
  border-bottom: 2px solid rgb(220, 204, 180);
}

.styled-table tbody tr.active-row {
  font-weight: bold;
  color: rgb(35, 35, 53);
}

.status-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
  margin-bottom: 10px;
}

.approve-button, .reject-button, .cancel-button{
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  margin-right: 10px;
}

.reject-button:hover,
.approve-button:hover,
.cancel-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;
}

.approve-button i {
  color: white;
  margin-right: 4px;
}

.reject-button i {
  color: white;
  margin-right: 4px;
}

.approve-button:hover i {
  color: black;
}

.reject-button:hover i {
  color: black;
}

</style>