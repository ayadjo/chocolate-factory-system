<template>
    <div class="registration-form">
        <div class="form-container">
          <div class="form-content">
            <h2>Sign Up</h2>
            <form @submit.prevent="submitForm">
              <div class="form-group">
                <input type="text" id="username" v-model="form.username" placeholder="Username" required />
              </div>
              <div class="form-group">
                <input type="password" id="password" v-model="form.password" placeholder="Password" required />
              </div>
              <div class="form-group">
                <input type="password" id="confirmPassword" v-model="form.confirmPassword" placeholder="Confirm Password" required />
              </div>
              <div class="form-group">
                <input type="text" id="firstName" v-model="form.firstName" placeholder="First Name" required />
              </div>
              <div class="form-group">
                <input type="text" id="lastName" v-model="form.lastName" placeholder="Last Name" required />
              </div>
              <div class="form-group">
                <label>Gender</label>
                <div class="radio-group">
                  <input type="radio" id="male" value="Male" v-model="form.gender" required />
                  <label for="male">Male</label>
                </div>
                <div class="radio-group">
                  <input type="radio" id="female" value="Female" v-model="form.gender" required />
                  <label for="female">Female</label>
                </div>
                <div class="radio-group">
                  <input type="radio" id="other" value="Other" v-model="form.gender" required />
                  <label for="other">Other</label>
                </div>
              </div>
              <div class="form-group">
                <label for="birthday">Birth Date</label>
                <input type="date" id="birthday" v-model="form.birthday" required />
              </div>
              <button type="submit">Register</button>
            </form>
          </div>
          <div class="image-container">
            <img src="../assets/amico.png" alt="Registration Image" />
          </div>
        </div>
      </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import axios from 'axios';
  
  const form = ref({
    username: "",
    password: "",
    confirmPassword: "",
    firstName: "",
    lastName: "",
    gender: "",
    birthday: "",
    role: 'Customer'
  });
  
  const submitForm = async () => {
  if (form.value.password !== form.value.confirmPassword) {
    alert("Passwords do not match!");
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/WebShopAppREST/rest/users/createCustomer', {
      username: form.value.username,
      password: form.value.password,
      firstName: form.value.firstName,
      lastName: form.value.lastName,
      gender: form.value.gender,
      birthday: form.value.birthday,
      role: "Customer"
    });

    console.log("Server response:", response.data);
    // Optionally, handle success response here (e.g., show a success message)
  } catch (error) {
    console.error("Error submitting form:", error);
    // Optionally, handle error here (e.g., show an error message)
  }
};
  </script>
  
  <style scoped>
  .registration-form {
    max-width: 800px; /* Povećajte širinu prema potrebi */
    margin: 10px auto;
    padding: 2rem;
    background: #201d0e;
    border-radius: 8px;
    color: white;
  }
  
  .form-container {
    display: flex;
    justify-content: space-between;
  }
  
  .form-content {
    flex: 1;
  }
  
  .registration-form h2 {
    text-align: center;
    margin-bottom: 1rem;
    font-weight: 100;
    font-size: 1.3vw;
  }
  
  .form-group {
    margin-bottom: 1rem;
  }
  
  .form-group label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: 100;
    font-size: 1.0vw;
  }
  
  .form-group input,
  .form-group select {
    width: 80%;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 15px;
    background-color: rgb(252, 244, 234);
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
  
  button[type="submit"] {
    background-color: #8f0710; /* Primary color */
    color: white;
    border: none;
    border-radius: 20px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
    width: 80%;
    height: 40px;
  }
  
  button[type="submit"]:hover {
    background-color: white; /* Darker shade of primary color */
    color: black;
    border: 1px solid #8f0710;
  }

  .image-container {
    flex: 1;
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
  
  .image-container img {
    max-width: 100%;
    height: auto;
    border-radius: 8px;
  }
  </style>