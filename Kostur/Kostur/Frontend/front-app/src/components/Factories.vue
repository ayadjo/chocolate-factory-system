<template>
    <div>
      <div v-for="factory in factories" :key="factory.id">
        <div class="card">
            <h2>{{ factory.name }}</h2>
            <img :src="factory.image" alt="Factory Image">
            <div class="location">
              <p>{{ factory.location.address }}</p>
              <p>{{ factory.location.longitude }}, </p>
              <p>{{ factory.location.latitude }}</p>
            </div>
            <div class="rating">{{ factory.rating }}</div>
          </div>
      </div>
    </div>
  </template>

<script setup>
import axios from 'axios';
import { ref } from 'vue';

const factories = ref([]);

</script>

<style scoped>
.card {
    border: 1px solid #ccc;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 16px;
  }
  
  .location {
    position: absolute;
    bottom: 0;
    left: 0;
  }
  
  .rating {
    position: absolute;
    bottom: 0;
    right: 0;
  }
</style>