<template>
    <div class="search-filter-container">
      <div class="search-container">     
        <div v-if="isCustomer" class="search-input-wrapper">
          <input type="text" v-model="searchQuery.factoryName" placeholder="Search by Factory Name">
          <i class="fas fa-industry search-icon"></i>
        </div>

    
        <div class="search-input-wrapper">
          <i class="fas fa-dollar-sign search-icon"></i>
          <input type="number" v-model="searchQuery.priceFrom" placeholder="Price from">
        </div>
        <div class="search-input-wrapper">
          <i class="fas fa-dollar-sign search-icon"></i>
          <input type="number" v-model="searchQuery.priceTo" placeholder="Price to">
        </div>
  

        <div class="search-input-wrapper">
          <i class="far fa-calendar-alt search-icon"></i>
          <input type="date" v-model="searchQuery.dateFrom" placeholder="Date from">
        </div>
        <div class="search-input-wrapper">
          <i class="far fa-calendar-alt search-icon"></i>
          <input type="date" v-model="searchQuery.dateTo" placeholder="Date to">
        </div>
        <button @click="search" class="search-button"><i class="bi bi-search"></i>Search</button>
      </div>
    </div>

    <div class="sort-container">
      <label for="sort-select">Sort by </label>
      <select id="sort-select" @change="handleSortChange">
        <option value="none">None</option>
        <option value="factoryName" v-if="isCustomer">Factory Name (Ascending)</option>
        <option value="factoryName_desc" v-if="isCustomer">Factory Name (Descending)</option>
        <option value="price">Price (Ascending)</option>
        <option value="price_desc">Price (Descending)</option>
        <option value="date">Date (Ascending)</option>
        <option value="date_desc">Date (Descending)</option>
      </select>
    </div>

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
                <p class="purchase-date">{{purchase.status}}</p>
            </div>

            <p class="user-name" v-if="isManager"><strong>{{ purchase.user.firstName }} {{ purchase.user.lastName }}</strong></p>
            <p class="purchase-date">Date: {{ purchase.purchaseDateAndTime }}</p>
            <p class="purchase-price">Price: ${{ formatPrice(purchase.price) }}</p>
            <button class="review-button"  @click="openCommentModal(purchase)" v-if="isCustomer && purchase.status == 'Approved' & !purchase.hasComment">Review</button>
            <button class="details-button" @click="showDetails(purchase)">Show Details</button>
        </div>

    </div>


    <div v-if="showDetailsModal" class="modal" @click.self="closeModal">
      <!--<span class="close-button3" v-if="isCustomer" @click="closeModal">&times;</span>
      <span class="close-button2" v-if="isManager" @click="closeModal">&times;</span>-->
      <div class="modal-content">
        <!--<span class="close-button" @click="closeModal">&times;</span>-->
        <h4>Purchase Details</h4>
        <p v-if="isCustomer">Name: {{selectedPurchase.user.firstName}} {{ selectedPurchase.user.lastName }}</p>
        <p v-if="isManager">Factory name: {{selectedPurchase.factory.name}} </p>
        <p >Date: {{ selectedPurchase.purchaseDateAndTime }}</p>
        <p >Price: ${{ formatPrice(selectedPurchase.price) }}</p>
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
          <button v-if="isManager && selectedPurchase.status == 'Processing'" class="approve-button" @click="approvePurchase(selectedPurchase.id)">
            <i class="fas fa-check"></i> Approve
          </button>
          <button v-if="isManager && selectedPurchase.status == 'Processing'" class="reject-button" @click="openRejectModal(selectedPurchase)">
            <i class="fas fa-times"></i> Reject
          </button>
          <button v-if="isCustomer && selectedPurchase.status == 'Processing'" class="cancel-button" @click="cancelPurchase(selectedPurchase)">
            <i class="fas fa-times"></i> Cancel
          </button>
        </div>
      </div>
    </div>

    <div v-if="showRejectModal" class="small-modal" @click.self="closeRejectModal">
      <div class="modal-content-reject">
          <span class="close-button" @click="closeRejectModal">&times;</span>
          <h4>Reject Purchase</h4>
          <textarea v-model="rejectionNote" placeholder="Enter reason for rejection" class="rejection-note"></textarea>
          <button class="reject-button" @click="submitRejection">Submit Rejection</button>
      </div>
    </div>

  <div v-if="showCommentModal" class="modal" @click.self="closeCommentModal">
    <div class="modal-content-reject">
      <span class="close-button" @click="closeCommentModal">&times;</span>
      <h4 class="review-title">Review for {{ selectedPurchase.factory.name }}</h4>
      <div class="rating">
        <span v-for="heart in 5" :key="heart" class="heart" :class="{'selected': heart <= rating}" @click="setRating(heart)">
          &#9829;
        </span>        
      </div>
      <textarea v-model="commentText" placeholder="Enter your comment" class="comment-text"></textarea>
      <button class="comment-button" @click="submitComment">Submit Comment</button>
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
const showRejectModal = ref(false);
const rejectionNote = ref('');
const userRole = ref(null);
const searchQuery = ref({
  factoryName: '',
  priceFrom: null,
  priceTo: null,
  dateFrom: null,
  dateTo: null
});
const showDetailsModal = ref(false);
const showCommentModal = ref(false);
const commentText = ref('');
const rating = ref(0);


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

const openRejectModal = (purchase) => {
  selectedPurchase.value = purchase;
  showRejectModal.value = true;
};

const closeRejectModal = () => {
  showRejectModal.value = false;
  rejectionNote.value = '';
};

const openCommentModal = (purchase) => {
  selectedPurchase.value = purchase;
  showCommentModal.value = true;
  console.log("purchase:", selectedPurchase.value.id)
};

const closeCommentModal = () => {
  selectedPurchase.value = null;
  showCommentModal.value = false;
  commentText.value = '';
  rating.value = 0;
};

const setRating = (heart) => {
  rating.value = heart;
};

const submitComment = async () => {
  if (!selectedPurchase.value || !selectedPurchase.value.items || !selectedPurchase.value.items.length) {
    console.error("Selected purchase is not properly set.");
    return;
  }

  try {
    await axios.post(`http://localhost:8080/WebShopAppREST/rest/comments/addComment`, {
      userId: localStorage.getItem('loggedUserId'),
      factoryId: selectedPurchase.value.factory.id,
      text: commentText.value,
      grade: rating.value,
      purchaseId: selectedPurchase.value.id
    }
  
  );
    console.log("Comment submitted successfully.");
    alert("Review successfully submitted!");
    closeCommentModal();
    getPurchases(id); 
  } catch (error) {
    console.error("There was an error submitting the comment.", error);
  }
};

const rejectPurchase = async (purchaseId, rejectionNote) => {
  try {
    await axios.put(`http://localhost:8080/WebShopAppREST/rest/purchases/rejectPurchase`, {
      purchaseId,
      rejectionNote
    });
    alert("Purchase rejected successfully.");
    closeRejectModal();
    closeModal();
    getPurchases(id); 
  } catch (error) {
    console.error('Error rejecting purchase:', error);
    alert('Failed to reject purchase. Please try again.');
  }
};

const submitRejection = () => {
  if (!rejectionNote.value) {
    alert('Please enter a reason for rejection.');
    return;
  }
  rejectPurchase(selectedPurchase.value.id, rejectionNote.value);
};



const checkLoggedIn = () => {
  const userId = localStorage.getItem('loggedUserId');
  return new Promise((resolve, reject) => {
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
  showDetailsModal.value = true;
};



const closeModal = () => {
  selectedPurchase.value = null;
  showDetailsModal.value = false;
};

const formatPrice = (price) => {
  return parseFloat(price).toFixed(2);
};

const approvePurchase = async (purchaseId) => {
  try {
    const response = await axios.put(`http://localhost:8080/WebShopAppREST/rest/purchases/approvePurchase/${purchaseId}`);
    console.log("Purchase approved!");
    alert("Purchase successfully approved!")
    closeModal();
    await getPurchases(id);
  }
  catch(error){
    console.error(`Error approving the purchase.`, error);
  }
};


const cancelPurchase = async (purchase) => {
  try {
    const response = await axios.patch(`http://localhost:8080/WebShopAppREST/rest/purchases/cancel/${purchase.id}`);
    if (response.status === 200) { // Provera da li je PATCH uspešan
      console.log("Purchase cancelled successfully");

      const responsePoints = await axios.patch(`http://localhost:8080/WebShopAppREST/rest/users/points/${purchase.user.id}?price=${purchase.price}`);
      closeModal();
      await getPurchases(id); 
    } else {
      console.error(`Failed to cancel the purchase. Status code: ${response.status}`);
    }
  }
  catch(error){
    console.error(`Error cancelling the purchase.`, error);
  }
  
};


const handleSortChange = async (event) => {
  const sortBy = event.target.value;
  if (sortBy !== 'none') {
    const [attribute, order] = sortBy.split('_');
    sortPurchases(attribute, order || 'asc');
  } else {
    getPurchases(id);
  }
};

const sortPurchases = async (attribute, order) => {
  try {
    const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/purchases/sortBy/${attribute}/${order}`);
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
    }
  } catch (error) {
    console.error(`Error sorting purchases by ${attribute}:`, error);
  }
};

const search = async () => {
  try {
    const params = {};
    if (searchQuery.value.factoryName) {
      params.factoryName = searchQuery.value.factoryName;
    }
    if (searchQuery.value.priceFrom) {
      params.priceFrom = searchQuery.value.priceFrom;
    }
    if (searchQuery.value.priceTo) {
      params.priceTo = searchQuery.value.priceTo;
    }
    if (searchQuery.value.dateFrom) {
      params.dateFrom = searchQuery.value.dateFrom;
    }
    if (searchQuery.value.dateTo) {
      params.dateTo = searchQuery.value.dateTo;
    }

    const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/purchases/search', { params });
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
    }
  } catch (error) {
    console.error("There was an error searching purchases.", error);
  }
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
  margin-bottom: 0px;
}


.user-username {
  font-weight: bold;
  margin-bottom: 0px;
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

.review-button .fas {
  color: red; 
  margin-right: 4px;
}

.review-button {
  background-color: #00A478;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  position: absolute;
  width: 48%;
  bottom: 50px; 
}

.review-button:hover {
  background-color: #00654a;
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

.small-modal {
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
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  padding: 5px;
  border-radius: 5px;
  width: 80%;
  max-width: 600px;
}

.close-button2 {
    color:  #aaa;
    position: relative;
    top: -175px;
    left: 595px;
    font-size: 24px;
    cursor: pointer;
}

.close-button3 {
    color:  #aaa;
    position: relative;
    top: -175px;
    left: 595px;
    font-size: 24px;
    cursor: pointer;
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
  margin: 20px 0;
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

.rejection-note {
  width: 100%;
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ddd;
  margin-bottom: 16px;
  resize: none;
  height: 80px;
}

.search-container {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  width: 80%;
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
    margin-top: 32px;
  }
  
  .search-button i {
    margin-right: 10px;
  }

  .search-button:hover {
    background-color: white;
    color: black;
    border: 1px solid #201d0e;
  }

  .sort-container {
    margin-top: 20px;
    display: inline-block;
    margin-bottom: 10px;
    transform: translateX(-152%);
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

  .comment-button {
    background-color: #00A478;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    margin-top: 16px;
  }
  
  .comment-button:hover {
    background-color: #00654a;
  }
  
  .rating {
    display: flex;
    justify-content: center;
    margin-bottom: 16px;
  }
  
  .rating .heart {
    font-size: 2em;
    color: #ccc;
    cursor: pointer;
    margin: 0 5px;
  }
  
  .rating .heart.selected {
    color: red;
  }
  
  .comment-text {
    width: 80%;
    padding: 8px;
    border-radius: 4px;
    border: 1px solid #ddd;
    margin-bottom: 16px;
    resize: none;
    height: 80px;
  }


  

  .review-title {
    text-align: center;
    font-weight: 100;
    font-size: 1.3vw;
    color: #201d0e;
  }

.purchase-status {
  margin-top: -1px;
}

.modal-content-reject {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
  width: 80%;
  max-width: 600px;
}

</style>