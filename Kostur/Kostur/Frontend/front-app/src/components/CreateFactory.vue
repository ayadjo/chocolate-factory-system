<template>
    <h2 class="title">
      Choose Factory Location
      <i class="fas fa-map-marker-alt"></i>
    </h2>
    <div id="map" ref="mapContainer"></div>
    <p class="address">{{ selectedAddress }}</p>
    <button class="next-button" @click="saveLocation">Next</button>
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
  const selectedAddress = ref('');  // Dodali smo promenljivu za čuvanje odabrane adrese
  
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
      selectedAddress.value = address;  // Postavljamo odabranu adresu
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
    alert("Location successffully saved!")

  } catch (error) {
    console.error('Error saving location:', error);
  }
}
  
  onBeforeUnmount(() => {
    if (map.value) {
      map.value.off();
      map.value.remove(); // Remove map instance
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
  </style>
  