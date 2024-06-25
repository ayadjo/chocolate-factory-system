<template>
  <div class="basket-container">
    <label v-if="items.length != 0" class="title"><img class="title-image" src="../assets/carts.png"></img>Shopping Cart</label>


    <div class="items">
      <div v-if="items.length === 0" class="no-items">
        <!-- <i class="fas fa-shopping-cart fa-5x"></i> -->
         <img class="no-items-image" src="../assets/Discount-pana.png"></img>
        <p class="no-items-message">Ups!... your shopping bag is currently empty, visit our shop and check discounts</p>   
      </div>
      
      <div v-else v-for="item in items" :key="item.id" class="item-card">
          <img :src="item.chocolate.image" alt="Chocolate Image" class="item-image">
          <div class="item-info">
            <h4 class="item-name">{{ item.chocolate.name }}</h4>
            <p class="p"> {{ item.chocolate.weight }}g</p>
            <p class="p"> {{ item.chocolate.type }}</p>
            <p class="p">{{ item.chocolate.kind }}</p>     
            <p class="p">{{ item.chocolate.onStock }}</p>     
          </div>
          <div class="item-quantity">
            <button @click="decrementQuantity(item)" class="quantity-button">-</button>
            <input type="number" v-model="item.quantity" class="quantity-input" min="1" >
            <button @click="incrementQuantity(item)" class="quantity-button">+</button>
          </div>
          <div class="item-price">
            <p class="itemPrice"><strong>${{ (item.chocolate.price * item.quantity).toFixed(2) }}</strong></p>
            <button @click="removeChocolate(item)" class="remove-button">
              <i class="fas fa-trash-alt"></i> Remove
            </button>
          </div>       
      </div>
        
      <div class="checkout" v-if="items.length > 0">
        <label class="total-price"><strong>Total&nbsp;</strong> ${{ totalPrice }}</label>
        <button @click="checkout(items)" class="checkout-button">Checkout</button>
      </div>
             
    </div>  
  </div>
</template>

<script setup>
  import { useRouter, useRoute } from 'vue-router';
  import { ref, onMounted, watch, computed } from 'vue';
  import axios from 'axios';
  const route = useRoute();
  const router = useRouter();

  const items = ref([]);
  const quantities = ref({});
  let totalPrice = 0;
  const basketId = ref("");


  const fetchBasketData = async () => {
    try {
      const userId = localStorage.getItem('loggedUserId');
      if (userId) {
        const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/baskets/${userId}`);
        const basket = response.data;
        totalPrice = basket.price.toFixed(2);
        basketId.value = basket.id;
        console.log(basketId.value)
        await loadItems(basketId.value);
      }
    } catch (error) {
      console.error('Error fetching basket data:', error);
    }
  };

  const loadItems = async (basketId) => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/basketItems/byBasket/${basketId}`);
    items.value = response.data;
  } catch (error) {
    console.error('Error loading items:', error);
  }
};


  async function decrementQuantity(item) {
  try {
    if (item.quantity > 1) {
      const userId = localStorage.getItem('loggedUserId');
      const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/decrementQuantity/${userId}/${item.chocolate.id}`);
      await fetchBasketData();
    }
  } catch (error) {
    console.error('Error updating basket:', error);
  }
};

  async function incrementQuantity(item) {
    try {
      const userId = localStorage.getItem('loggedUserId');
      if (item.chocolate.status === 'onStock') {
        await axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/incrementQuantity/${userId}/${item.chocolate.id}`);
        await fetchBasketData();
      } else {
        alert(`${item.chocolate.name} is currently out of stock.`);
      }
    } catch (error) {
      console.error('Error updating basket:', error);
    }
};

function removeChocolate(item) {
  const userId = localStorage.getItem('loggedUserId');

  try {
    axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/removeChocolate/${userId}/${item.chocolate.id}`);
    alert(`Removed ${item.chocolate.name} from the basket.`);
    fetchBasketData();
  } catch (error) {
    console.error('Error removing chocolate from basket:', error);
    alert('Failed to remove chocolate from the basket. Please try again.');
  }
}

function checkout(items){
  // const purchase = {
  //       items: items,
  //       price: this.totalPrice
  //     };
  const userId = localStorage.getItem('loggedUserId');

 axios.post(`http://localhost:8080/WebShopAppREST/rest/purchases/${userId}/${basketId.value}`)
    .then(() => {
      alert("Successful purchases!");

      axios.put(`http://localhost:8080/WebShopAppREST/rest/users/points/${userId}?price=${this.totalPrice}`)
        .then(() => {
          console.log('Points updated successfully!');
        })
        .catch(error => {
          console.error('Something went wrong while updating points!', error);
        });

      axios.put(`http://localhost:8080/WebShopAppREST/rest/baskets/clearBasket/${userId}`)
        .then(() => {
          fetchBasketData();
        })
        .catch(error => {
          console.error('Spmething went wrong with the basket!', error);
        });

        // axios.put(`http://localhost:8080/WebShopAppREST/rest/chocolates/purchase`, purchase)
        // .then(() => {
        //   console.log('Chocolates updated successfully!');
        // })
        // .catch(error => {
        //   console.error('Spmething went wrong with the basket!', error);
        // });
    })
    .catch(error => {
      console.error('Spmething went wrong!', error);
    });
}


  onMounted(() => {
    fetchBasketData();
  });
</script>

<style>

.no-items {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 0px;
}

.no-items-image {
  width: 60%;
  height: auto;
  margin-bottom: 10px;
}

.no-items-message {
  font-size: 20px;
  color: #666;
  text-align: center;
}

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
    margin-top: 40px;
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
  align-items: center;
  width: 30px;
  text-align: right;
}

.itemPrice {
  margin-bottom: 5px;
  margin-left: -10px;
}

.remove-button {
  color: #8f0710;
  border: none;
  background: white;
  font-size: 10px;
  width: 70px;
  margin-left: -17px;
  cursor: pointer;
}

.checkout {
  text-align: center;
  margin-top: 20px;
}

.title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 20px;
}

.title-image {
  margin-right: 10px; 
  width: 4%;
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