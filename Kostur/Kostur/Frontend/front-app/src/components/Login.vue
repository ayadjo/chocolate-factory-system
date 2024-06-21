<template>
    <div class="login-container">
      <h2>Sign In</h2>
      <img src="../assets/user-login.png" alt="Logo" class="logo">
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <input type="text" id="username" placeholder="Username" v-model="username" required>
        </div>
        <div class="form-group">
          <input type="password" id="password" placeholder="Password" v-model="password" required>
        </div>
        <button type="submit">Submit</button>
      </form>
    </div>
  </template>
  
  <script setup>
    import { ref } from 'vue';
  
    const username = ref('');
    const password = ref('');
  
    const handleSubmit = () => {
      console.log('Submitting:', username.value, password.value);
    };
  </script>
  
  <style scoped>
    .login-container {
      max-width: 400px;
      height: 370px;
      margin: 100px auto;
      padding: 2rem;
      background: #201d0e;
      border-radius: 8px;
      color: white;
      text-align: center;
    }

    .login-container h2 {
        text-align: center;
        margin-bottom: 1rem;
        font-weight: 100;
        font-size: 1.3vw;
      }
  
    .logo {
      width: 100px;
      height: auto;
      margin-bottom: 20px;
    }
  
    .form-group {
      margin-bottom: 15px;
    }
  
    label {
      font-weight: bold;
      display: block;
      margin-bottom: 5px;
    }
  
    input[type="text"],
    input[type="password"],
    button {
        width: 80%;
        padding: 0.5rem;
        border: 1px solid #ccc;
        border-radius: 15px;
        background-color: rgb(252, 244, 234);
    }
  
    button{
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
        margin-top: 40px;
      }
      
      button:hover {
        background-color: white;
        color: black;
        border: 1px solid #8f0710;
      }
  </style>
  