<template>
  <div class="basket-container">
    <label class="title">Shopping Cart</label>


    <div class="items">
      <div v-if="items.length === 0" class="no-items">
        <i class="fas fa-shopping-cart fa-5x"></i>
        <p>Your shopping cart is empty.</p>   
      </div>
      
      <div v-else v-for="item in items" :key="item.id" class="item-card">
          <img :src="item.chocolate.image" alt="Chocolate Image" class="item-image">
          <div class="item-info">
            <h4 class="item-name">{{ item.chocolate.name }}</h4>
            <p class="p"><strong>Weight:</strong> {{ item.chocolate.weight }}</p>
            <p class="p"><strong>Type:</strong> {{ item.chocolate.type }}</p>
            <p class="p"><strong>Kind:</strong> {{ item.chocolate.kind }}</p>         
          </div>
          <div class="item-quantity">
            <button @click="decrementQuantity(item)" class="quantity-button">-</button>
            <input type="number" v-model="item.quantity" class="quantity-input" min="1">
            <button @click="incrementQuantity(item)" class="quantity-button">+</button>
          </div>
          <div class="item-price">
            <p><strong>${{ (item.chocolate.price * item.quantity).toFixed(2) }}</strong></p>
          </div>       
      </div>
        
      <div class="checkout" v-if="items.length > 0">
        <label class="total-price"><strong>Total:</strong> ${{ totalPrice }}</label>
        <button @click="checkout()" class="checkout-button">Checkout</button>
      </div>
             
    </div>  
  </div>
</template>

<script setup>
  import { useRouter } from 'vue-router';
  import { ref, onMounted, watch, computed } from 'vue';
  import axios from 'axios';

  const items = ref([]);
  const quantities = ref({});
  let totalPrice = 0;


  const fetchBasketData = async () => {
    try {
      const userId = localStorage.getItem('loggedUserId');
      if (userId.value !== null) {
        const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/baskets/${userId}`);
        const basket = response.data;
        items.value = basket.items;
        totalPrice = basket.price.toFixed(2);

      }
    } catch (error) {
      console.error('Error fetching basket data:', error);
    }
  };


  async function decrementQuantity(item) {
  try {
    const userId = localStorage.getItem('loggedUserId');
    const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/decrementQuantity/${userId}/${item.chocolate.id}`);
    fetchBasketData(); 
  } catch (error) {
    console.error('Error updating basket:', error);
  }
};

  async function incrementQuantity(item) {
  try {
    const userId = localStorage.getItem('loggedUserId');
    const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/incrementQuantity/${userId}/${item.chocolate.id}`);
    fetchBasketData(); 
  } catch (error) {
    console.error('Error updating basket:', error);
  }
};

  onMounted(() => {
    fetchBasketData();
  });
</script>

<style>

  .basket-container {
    width: 750px;
    margin: 20px auto; 
    padding: 30px;
    border: 1px solid #ccc;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    background-color: white; 
    height: auto;
  }

  .items {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    margin-top: 16px;
    justify-content: center;
  }


.item-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px;
  width: 100%;
}

.item-image {
  width: 100px;
  height: auto;
  margin-right: 16px;
}

.item-info {
  flex: 1;
  text-align: left;
}

.p {
  font-size: 12px
}

.item-name {
  margin-bottom: -3px;
}

.item-quantity {
  margin-right: 110px;
  display: flex;
  align-items: center;
  gap: 10px;
}


.item-price {
  width: 30px;
  text-align: right;
}

.checkout {
  text-align: center;
}

.title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 16px;
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

.checkout-button {
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  margin-left: 50px;
}

.checkout-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;
}

.totalPrice {
  margin-left: 200px;
}

</style>