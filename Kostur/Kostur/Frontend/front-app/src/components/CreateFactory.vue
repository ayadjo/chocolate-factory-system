<template>
  <div>
    <div v-if="currentView === 'map'">
      <h2 class="title">
        Choose Factory Location
        <i class="fas fa-map-marker-alt"></i>
      </h2>
      <div id="map" ref="mapContainer"></div>
      <p class="address">{{ selectedAddress }}</p>
      <button class="next-button" @click="saveLocation">Next</button>
    </div>

    <div v-if="currentView === 'form'" class="form-page">
      <h2 class="title">
        Factory Information
        <i class="fas fa-info-circle"></i>
      </h2>
      <div class="form-container">
        <form @submit.prevent="submitFactoryDetails" class="factory-form">
          <div class="form-group">
            <input type="text" id="name" placeholder="Factory Name" v-model="name" required />
          </div>
          <div class="form-group">
            <label for="startTime">Start Time</label>
            <input type="time" id="startTime" placeholder="Start Time" v-model="startTime" required />
          </div>
          <div class="form-group">
            <label for="endTime">End Time</label>
            <input type="time" id="endTime" placeholder="End Time" v-model="endTime" required />
          </div>
          <div class="form-group">
            <input type="text" id="logo" placeholder="Logo" v-model="logo" required />
          </div>
          <div class="form-group">
            <img :src="logo" alt="Logo" class="logo-image">
          </div>
          <div class="form-group">
            <label for="location">Location</label>
            <input type="text" id="location" v-model="selectedAddress" disabled />
          </div>
          <button class="submit-button" type="submit">Next</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import axios from 'axios';

delete L.Icon.Default.prototype._getIconUrl;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png'
});

const mapContainer = ref(null);
const map = ref(null);
let marker = ref(null);
const selectedAddress = ref('');
const savedLocation = ref(null);
const currentView = ref('map');

const name = ref('');
const startTime = ref('');
const endTime = ref('');
const logo = ref('');

onMounted(() => {
  map.value = L.map(mapContainer.value).setView([45.2671, 19.8335], 13);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map.value);

  map.value.on('click', async function(e) {
    if (marker.value) {
      map.value.removeLayer(marker.value);
    }

    const { lat, lng } = e.latlng;

    marker.value = L.marker([lat, lng]).addTo(map.value);

    const address = await getAddress(lat, lng);
    selectedAddress.value = address;
    console.log(`Latitude: ${lat}, Longitude: ${lng}, Address: ${address}`);
  });
});

async function getAddress(lat, lng) {
  try {
    const response = await axios.get(`https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lng}`);
    const data = response.data;
    const address = `${data.address.road || ''} ${data.address.house_number || ''}, ${data.address.city || data.address.town || data.address.village || ''}, ${data.address.country || ''}`;
    return address.trim().replace(/^,|,$/g, ''); 
  } catch (error) {
    console.error('Error fetching address:', error);
    return 'Address not found';
  }
}

async function saveLocation() {
  try {
    const response = await axios.post('http://localhost:8080/WebShopAppREST/rest/locations/', {
      latitude: marker.value.getLatLng().lat,
      longitude: marker.value.getLatLng().lng,
      address: selectedAddress.value
    });

    console.log('Saved location:', response.data);
    savedLocation.value = response.data;
    alert("Location successfully saved!");
    currentView.value = 'form';

  } catch (error) {
    console.error('Error saving location:', error);
  }
}

function submitFactoryDetails() {
  const factoryDetails = {
    name: name.value,
    startTime: startTime.value,
    endTime: endTime.value,
    logo: logo.value,
    location: savedLocation.value 
  };

  console.log('Factory Details:', factoryDetails);
}
onBeforeUnmount(() => {
  if (map.value) {
    map.value.off();
    map.value.remove();
  }
});
</script>

<style scoped>
.title {
  text-align: center;
  font-weight: 100;
  font-size: 1.3vw;
  color: #201d0e;
  margin-top: 50px;
}
#map {
  margin-top: 60px;
  margin-left: 150px;
  border-radius: 10px;
  border: 1px solid black;
  height: 500px;
  width: 80%;
}
.address {
  text-align: center;
  font-size: 1.1em;
  margin-top: 20px;
  color: #201d0e;
}
.next-button {
  background-color: #201d0e;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  width: 10%;
  height: 40px;
  margin-top: 30px;
}
.next-button:hover {
  background-color: white;
  color: black;
  border: 1px solid #201d0e;
}
.form-page {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.form-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 90vh;
  width: 100%;
}
.factory-form {
  background-color: rgb(252, 244, 234);
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  width: 40%;
  min-width: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.form-group {
  margin: 15px 0;
  width: 100%;
}
.form-group label {
  display:flex;
  margin-left: 60px;
  margin-bottom: 5px;
  color: #201d0e;
}
.form-group input {
    width: 80%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 15px;
}
.submit-button {
  background-color: #201d0e;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  width: 20%;
  height: 40px;
  margin-top: 20px;
}
.submit-button:hover {
  background-color: white;
  color: black;
  border: 1px solid #201d0e;
}

.logo-image{
  width: 150px;
  height: auto;
  margin: 0 auto;
  border-radius: 10px;
}
</style>
