<template>
    <div class="all-employees">
        <div v-if="allEmployees.length === 0" class="no-employees">
            <button @click="newEmployee" class="new-employee-button">New Employee</button>
            <img class="empty-image" src="../assets/EMPTY.jpg" alt="No Employees Found">
            <p class="no-employees-message">Ups!... no employees found</p>
        </div>
        <div v-else>
            <button @click="newEmployee" class="new-employee-button">New Employee</button>
            <h2 class="title">
                Employees
                <i class="fas fa-users"></i>
              </h2>
        <div class="employees">
            <div class="employee-card" v-for="employee in allEmployees" :key="employee.id">
                <img class="employee-image" src="../assets/manager.png" alt="Employee Image">
                <b class="employee-username">{{ employee.username }}</b>
                <p class="employee-name">{{ employee.firstName }} {{ employee.lastName }}</p>
            </div>
        </div>
        </div>
    </div>
</template>



<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const factoryId = route.params.id;
const allEmployees = ref([]);

const fetchEmployees = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/WebShopAppREST/rest/users/byFactory/${factoryId}`);
        allEmployees.value = response.data;
    } catch (error) {
        console.error('Error fetching employees:', error);
    }
};

onMounted(() => {
    fetchEmployees();
});

const newEmployee = () => {
  if (factoryId) {
    router.push('/new-employee/' + factoryId);
  } else {
    console.error('Error fetching factoryId!');
  }
};
</script>


<style scoped>
.title {
    text-align: center;
    font-weight: 100;
    font-size: 1.3vw;
    color: #201d0e;
    margin-top: -20px;
  }

.employees {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    justify-content: center;
}

.no-employees {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 40px;
}

.empty-image {
    width: 50%;
    height: auto;
    margin-bottom: 10px;
}

.no-employees-message {
    font-size: 20px;
    color: #666;
    text-align: center;
}

.employee-card {
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

.employee-card:hover {
    transform: scale(1.05);
}

.employee-image {
    width: 60%;
    height: auto;
    border-radius: 50%;
    margin-bottom: 10px;
}

.employee-username,
.employee-name {
    font-size: 16px;
    margin: 5px 0;
}

.new-employee-button {
    background-color: #f0f0f0; 
    color: #201d0e;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    margin-top: 20px;
    margin-left: 10px;
    transform: translateX(400%);
    width: 10%;
  }
  
  .new-employee-button:hover {
    background-color: white; 
    color: black;
    border: 1px solid #8f0710;
  }
</style>
