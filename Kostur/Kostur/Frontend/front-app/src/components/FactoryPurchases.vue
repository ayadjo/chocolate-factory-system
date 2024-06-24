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

            <p class="user-name"><strong>{{ purchase.user.firstName }} {{ purchase.user.lastName }}</strong></p>
            <p class="purchase-date">Date: {{ purchase.purchaseDateAndTime }}</p>
            <p class="purchase-price">Price: {{ purchase.price }}</p>
            <button class="details-button">Show Details</button>
        </div>

    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const purchases = ref([])
const route = useRoute();
const factoryId = route.params.id;

const getPurchases = async (factoryId) => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/purchases/factory/${factoryId}`);
    purchases.value = response.data;

    // Loop through each purchase item in the response data
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

    console.log("Retrieved all purchases from the factory with id", factoryId);
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

onMounted(() => {
    getPurchases(factoryId);
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


</style>