<template>
  <div class="container">
    <h2 class="heading">Add Chocolate</h2>
    <form @submit.prevent="addChocolate($event)" class="form">
      <div class="form-group">
        <label for="name" class="label">Name</label>
        <input type="text" id="name" v-model="chocolate.name" v-bind:style="nameColor" class="input" required>
      </div>
      <div class="form-group">
        <label for="price" class="label">Price</label>
        <input type="number" id="price" v-model="chocolate.price" class="input" required>
      </div>
      <div class="form-group">
        <label for="kind" class="label">Kind</label>
        <select id="kind" v-model="chocolate.kind" class="select" required>
          <option value="Ordinary">Ordinary</option>
          <option value="ForCooking">ForCooking</option>
          <option value="ForDrinking">ForDrinking</option>
        </select>
      </div>
      <div class="form-group">
        <label for="type" class="label">Type</label>
        <select id="type" v-model="chocolate.type" class="select" required>
          <option value="Dark">Dark</option>
          <option value="Milk">Milk</option>
          <option value="White">White</option>
        </select>
      </div>
      <div class="form-group">
        <label for="weight" class="label">Weight</label>
        <input type="number" id="weight" v-model="chocolate.weight" class="input" required>
      </div>
      <div class="form-group">
        <label for="description" class="label">Description</label>
        <textarea id="description" v-model="chocolate.description" class="textarea" required></textarea>
      </div>
      <div class="form-group">
        <label for="image" class="label">Image URL</label>
        <input type="url" id="image" v-model="chocolate.image" class="input" required>
      </div>
      <img :src="chocolate.image" alt="Chocolate" class="chocolate-image">
      <button type="submit" class="add-button">Add</button>
      <div class="form-row">        
          <h5 v-bind:style="errorColor">{{errorMessage}}</h5>
      </div>
    </form>
  </div>
</template>

<script setup>
import axios from 'axios';
import {ref} from 'vue';
import { useRouter, useRoute } from 'vue-router';

const chocolate = ref({
  name: "",
  price: 0,
  kind: "",
  type: "",
  weight: 0,
  description: "",
  image: ""
});
const errorMessage = ref("");
const route = useRoute();
const router = useRouter();

const factoryId = route.params.id;

function addChocolate(event){

  if(!this.chocolate.name){
				this.nameColor='border-color: red';
	}

  if(!this.chocolate.name){
				this.errorMessage='Field name is neccessary!';
				this.errorColor = "color:red";
				return;
			}
			
	this.errorMessage = '';

  axios.post(`http://localhost:8080/WebShopAppREST/rest/chocolates/${factoryId}`, chocolate.value)
    .then(() => {
      router.push('/')
    })
    .catch(() => {
      alert('Something went wrong.')
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
  display: grid;
  grid-template-columns: 1fr 2fr;
  align-items: center;
  font-size: 1.2em;
  color: #333;
}



.label {
  font-size: 16px;
  text-align: left;
  margin-right: 10px;
}

.input,
.select,
.textarea {
  width: 100%;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
}

.add-button {
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

.add-button:hover {
  background-color: white; 
  color: black;
  border: 1px solid #8f0710;

}

.form-row {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.chocolate-image{
  width: 150px;
  height: auto;
  margin: 0 auto;
}
</style>
