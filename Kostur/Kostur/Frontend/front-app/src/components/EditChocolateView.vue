<template>
  <div class="container">
    <h1>Edit Chocolate</h1>
    <form @submit.prevent="updateChocolate" class="form">
      <div class="form-group">
        <label for="name" class="label">Name</label>
        <input type="text" id="name" v-model="editedChocolate.name" class="input" required>
      </div>
      <div class="form-group">
        <label for="price" class="label">Price</label>
        <input type="number" id="price" v-model="editedChocolate.price" class="input" required>
      </div>
      <div class="form-group">
        <label for="kind" class="label">Kind</label>
        <select id="kind" v-model="editedChocolate.kind" class="select" required>
          <option value="Ordinary">Ordinary</option>
          <option value="ForCooking">ForCooking</option>
          <option value="ForDrinking">ForDrinking</option>
        </select>
      </div>
      <div class="form-group">
        <label for="type" class="label">Type</label>
        <select id="type" v-model="editedChocolate.type" class="select" required>
          <option value="Dark">Dark</option>
          <option value="Milk">Milk</option>
          <option value="White">White</option>
        </select>
      </div>
      <div class="form-group">
        <label for="weight" class="label">Weight</label>
        <input type="number" id="weight" v-model="editedChocolate.weight" class="input" required>
      </div>
      <div class="form-group">
        <label for="description" class="label">Description</label>
        <textarea id="description" v-model="editedChocolate.description" class="textarea" required></textarea>
      </div>
      <div class="form-group">
        <label for="image" class="label">Image URL</label>
        <input type="url" id="image" v-model="editedChocolate.image" class="input" required>
      </div>
      <button type="submit" class="update-button">Update</button>
    </form>
  </div>
</template>

<script setup>
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const editedChocolate = ref({
  id: null,
  name: "",
  price: 0,
  kind: "",
  type: "",
  weight: 0,
  description: "",
  image: ""
});
const error = ref("");
const route = useRoute();
const router = useRouter();

const chocolateId = route.params.id;

onMounted(() => {
  axios.get(`http://localhost:8080/WebShopAppREST/rest/chocolates/${chocolateId}`)
    .then(response => {
      editedChocolate.value = response.data;
    })
    .catch(() => {
      alert('Error fetching chocolate data.');
    });
});

function updateChocolate() {
  axios.put(`http://localhost:8080/WebShopAppREST/rest/chocolates/${editedChocolate.value.id}`, editedChocolate.value)
    .then(() => {
      router.push('/');
    })
    .catch(() => {
      alert('Error updating chocolate.');
    });
}
</script>

<style scoped>
.container {
  max-width: 600px;
  margin: 0 auto;
  border: 1px solid #ccc;
  padding: 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
   align-items: center;
  text-align: center;
  margin-top: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}

.h1 {
  margin-bottom: 8px;
  font-size: 0.8em;
  color: #333;
}


.form {
  display: grid;
  gap: 10px;
}

.form-group {
  display: flex;
  align-items: center;
  font-size: 1.2em;
  color: #333;
}

.label {
  font-size: 18px;
  margin-right: 10px;
}

.input,
.select,
.textarea {
  flex: 1;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.update-button {
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
}

.update-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;

}
</style>
