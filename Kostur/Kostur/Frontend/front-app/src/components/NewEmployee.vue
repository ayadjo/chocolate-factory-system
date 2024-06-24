<template>
    <div  class="form-page">
        <h2 class="title">
          Add New Employee
          <i class="fas fa-user-plus"></i>
        </h2>
        <div class="form-container">
        <form @submit.prevent="submitEmployee" class="factory-form">
          <div class="form-group">
            <input type="text" id="username" v-model="employeeForm.username" placeholder="Username" required />
          </div>
          <div class="form-group">
            <input type="password" id="password" v-model="employeeForm.password" placeholder="Password" required />
          </div>
          <div class="form-group">
            <input type="password" id="confirmPassword" v-model="employeeForm.confirmPassword" placeholder="Confirm Password" required />
          </div>
          <div class="form-group">
            <input type="text" id="firstName" v-model="employeeForm.firstName" placeholder="First Name" required />
          </div>
          <div class="form-group">
            <input type="text" id="lastName" v-model="employeeForm.lastName" placeholder="Last Name" required />
          </div>
          <div class="form-group">
            <label class="gender-label">Gender</label>
            <div class="radio-group">
              <input type="radio" id="male" value="Male" v-model="employeeForm.gender" required />
              <label for="male">Male</label>
            </div>
            <div class="radio-group">
              <input type="radio" id="female" value="Female" v-model="employeeForm.gender" required />
              <label for="female">Female</label>
            </div>
            <div class="radio-group">
              <input type="radio" id="other" value="Other" v-model="employeeForm.gender" required />
              <label for="other">Other</label>
            </div>
          </div>
          <div class="form-group">
            <label for="birthday">Birth Date</label>
            <input type="date" id="birthday" v-model="employeeForm.birthday" required />
          </div>
          <button type="submit" class="finish-button">Finish</button>
        </form>
        </div>
      </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const factoryId = route.params.id;

const employeeForm = ref({
    username: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    gender: '',
    birthday: '',
    role: 'Employee'
});

const submitEmployee = async () => {
    try {
        if (employeeForm.value.password !== employeeForm.value.confirmPassword) {
            alert('Passwords do not match');
            return;
        }
        const response = await axios.post(`http://localhost:8080/WebShopAppREST/rest/users/addEmployee/${factoryId}`, employeeForm.value);
        console.log('Response from server:', response.data);
        alert("New Employee successfully added!")
        router.push('/employees/' + factoryId);
        clearForm();
    } catch (error) {
        console.error('Error submitting employee:', error);
        alert("Error occured, please try again.")
    }
};

const clearForm = () => {
    employeeForm.value = {
        username: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        gender: '',
        birthday: ''
    };
};
</script>


<style scoped>

.title {
    text-align: center;
    font-weight: 100;
    font-size: 1.3vw;
    color: #201d0e;
    margin-top: 50px;
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

</style>