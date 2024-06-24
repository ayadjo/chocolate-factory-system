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

    <div v-if="currentView === 'availableManagers'">
      <h2 class="title">
        Available Managers
        <i class="fas fa-users"></i>
      </h2>
      <div class="available-managers">
      <div class="manager-card" v-for="manager in availableManagers" :key="manager.id">
        <img class="manager-image" src="../assets/manager.png" alt="Manager Image">
        <b class="manager-username">{{ manager.username }}</b>
        <p class="manager-name">{{ manager.firstName }} {{ manager.lastName }}</p>
        <button @click="chooseManager(manager.id)" class="choose-button">Choose</button>
      </div>
      </div>
    </div>
    <div v-if="currentView === 'addManagerForm'"  class="form-page">
      <h2 class="title">
        Add New Manager
        <i class="fas fa-user-plus"></i>
      </h2>
      <div class="form-container">
      <form @submit.prevent="submitManagerForm" class="factory-form">
        <div class="form-group">
          <input type="text" id="username" v-model="managerForm.username" placeholder="Username" required />
        </div>
        <div class="form-group">
          <input type="password" id="password" v-model="managerForm.password" placeholder="Password" required />
        </div>
        <div class="form-group">
          <input type="password" id="confirmPassword" v-model="managerForm.confirmPassword" placeholder="Confirm Password" required />
        </div>
        <div class="form-group">
          <input type="text" id="firstName" v-model="managerForm.firstName" placeholder="First Name" required />
        </div>
        <div class="form-group">
          <input type="text" id="lastName" v-model="managerForm.lastName" placeholder="Last Name" required />
        </div>
        <div class="form-group">
          <label class="gender-label">Gender</label>
          <div class="radio-group">
            <input type="radio" id="male" value="Male" v-model="managerForm.gender" required />
            <label for="male">Male</label>
          </div>
          <div class="radio-group">
            <input type="radio" id="female" value="Female" v-model="managerForm.gender" required />
            <label for="female">Female</label>
          </div>
          <div class="radio-group">
            <input type="radio" id="other" value="Other" v-model="managerForm.gender" required />
            <label for="other">Other</label>
          </div>
        </div>
        <div class="form-group">
          <label for="birthday">Birth Date</label>
          <input type="date" id="birthday" v-model="managerForm.birthday" required />
        </div>
        <button type="submit" class="finish-button">Finish</button>
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
import { useRouter } from 'vue-router';

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
const availableManagers = ref([]);

const savedFactoryId = ref(null);

const managerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  firstName: '',
  lastName: '',
  gender: '',
  birthday: '',
  role: 'Manager'
});

const router = useRouter();

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
    const response = await axios.get(`https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lng}&accept-language=sr-Latn`);
    const data = response.data;
    const address = `${data.address.road || ''} ${data.address.house_number || ''}, ${data.address.city || data.address.town || data.address.village || ''}, ${data.address.country || ''}`;
    return address.trim().replace(/^,|,$/g, ''); 
  } catch (error) {
    console.error('Error fetching address:', error);
    return 'Address not found';
  }
}


async function saveLocation() {
  if (!selectedAddress.value) {
    alert("Please select a location on the map before proceeding.");
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/WebShopAppREST/rest/locations/', {
      latitude: marker.value.getLatLng().lat,
      longitude: marker.value.getLatLng().lng,
      address: selectedAddress.value
    });

    console.log('Saved location:', response.data);
    savedLocation.value = response.data.id; 
    currentView.value = 'form';

  } catch (error) {
    console.error('Error saving location:', error);
  }
}

async function submitFactoryDetails() {
  if (!savedLocation.value) {
    alert("Please save the location before submitting factory details.");
    return;
  }

  try {
    const response = await axios.post(`http://localhost:8080/WebShopAppREST/rest/factories/${savedLocation.value}`, {
      name: name.value,
      startTime: startTime.value,
      endTime: endTime.value,
      logo: logo.value,
    });

    console.log('Saved factory:', response.data);
    savedFactoryId.value = response.data.id;
    checkAvailableManagers();

  } catch (error) {
    console.error('Error saving factory:', error);
  }
}

async function checkAvailableManagers() {
  try {
    const response = await axios.get('http://localhost:8080/WebShopAppREST/rest/users/availableManagers');
    availableManagers.value = response.data;

    if (availableManagers.value.length > 0) {
      currentView.value = 'availableManagers';
      alert("Almost there...\n\nThe last step is to choose manager.")
    } else {
      currentView.value = 'addManagerForm';
      alert("Almost there...\n\nSorry, but right now there is no available managers, you have to create a new one.")
    }

  } catch (error) {
    console.error('Error fetching available managers:', error);
  }
}


async function submitManagerForm() {
  if (managerForm.value.password !== managerForm.value.confirmPassword) {
    alert("Passwords do not match!");
    return;
  }

  if (!savedFactoryId.value) {
    alert("Factory ID is not available.");
    return;
  }

  try {
    const response = await axios.post(`http://localhost:8080/WebShopAppREST/rest/users/createManager/${savedFactoryId.value}`, {
      username: managerForm.value.username,
      password: managerForm.value.password,
      firstName: managerForm.value.firstName,
      lastName: managerForm.value.lastName,
      gender: managerForm.value.gender,
      birthday: managerForm.value.birthday,
      role: 'Manager'
    });

    console.log('New manager added:', response.data);
    alert("New manager successfully assigned to the factory!");
    router.push("/");

    managerForm.value = {
      username: '',
      password: '',
      confirmPassword: '',
      firstName: '',
      lastName: '',
      gender: '',
      birthday: ''
    };
  } catch (error) {
    console.error('Error registering manager:', error);
  }
}

async function chooseManager(managerId) {
  if (!savedFactoryId.value) {
    alert("Please save the factory details before assigning a manager.");
    return;
  }

  try {
    const response = await axios.patch(`http://localhost:8080/WebShopAppREST/rest/users/${managerId}/${savedFactoryId.value}`);

    console.log('Assigned manager:', response.data);
    alert("Manager successfully assigned!");
    router.push("/");
  } catch (error) {
    console.error('Error assigning manager:', error);
  }
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

.radio-group {
  display: inline-block;
  margin-right: 1rem;
  font-weight: 100;
  font-size: 1.3vw;
}

.radio-group input[type="radio"] {
  display: none;
}

.radio-group label {
  cursor: pointer;
  font-weight: 100;
  font-size: 0.9vw;
}

.radio-group label:before {
  content: "";
  display: inline-block;
  width: 1em;
  height: 1em;
  border-radius: 50%;
  border: 2px solid #ccc;
  margin-right: 0.5em;
  position: relative;
  top: 0.2em;
  background-color: transparent;
}

.radio-group input[type="radio"]:checked + label:before {
  background-color: #8f0710;
  border-color: rgb(220, 204, 180);
}

.finish-button {
  background-color: #8f0710; 
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  width: 20%;
  height: 40px;
}

.finish-button:hover {
  background-color: white;
  color: black;
  border: 1px solid #8f0710;
}

.form-group .gender-label {
  margin-bottom: 0.5rem;
  font-weight: 120;
  font-size: 1.0vw;
  color: #201d0e;
  margin-left: 280px;
}

.available-managers {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  justify-content: center;
}

.manager-card {
  background-color: rgb(252, 244, 234);
  border: 1px solid #ccc;
  border-radius: 8px;
  width: 200px;
  padding: 16px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  margin-top: 40px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
}


.manager-image {
  width: 60%;
  height: auto;
  border-radius: 50%;
  margin-bottom: 10px;
}

.manager-username,
.manager-name {
  font-size: 16px;
  margin: 5px 0;
}

.choose-button {
  background-color: #8f0710;
  color: white;
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
  width: 80%;
  height: 40px;
  margin-top: 10px;
}

.choose-button:hover {
  background-color: white;
  color: black;
  border: 1px solid #201d0e;
}

</style>
