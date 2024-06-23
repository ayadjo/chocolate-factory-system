<template>
    <div v-if="user" class="edit-user-profile">
      <div class="header-section">
        <div class="profile-pic">
          <img src="../assets/user.png" alt="User Image" />
        </div>
      </div>
      <div class="personal-info">
        <h2>Edit Personal Information</h2>
        <div class="info-item">
          <span class="info-label">USERNAME</span>
          <input v-model="user.username" type="text" class="info-input" />
        </div>
        <div class="info-item">
          <span class="info-label">NAME</span>
          <input v-model="user.firstName" type="text" class="info-input" />
        </div>
        <div class="info-item">
          <span class="info-label">SURNAME</span>
          <input v-model="user.lastName" type="text" class="info-input" />
        </div>
        <div class="info-item">
          <span class="info-label">GENDER</span>
          <div class="gender-options">
            <div class="radio-group">
              <input type="radio" id="male" v-model="user.gender" :checked="user.gender === 'Male'" value="Male" />
              <label for="male">Male</label>
            </div>
            <div class="radio-group">
              <input type="radio" id="female" v-model="user.gender" :checked="user.gender === 'Female'" value="Female" />
              <label for="female">Female</label>
            </div>
            <div class="radio-group">
              <input type="radio" id="other" v-model="user.gender" :checked="user.gender === 'Other'" value="Other" />
              <label for="other">Other</label>
            </div>
          </div>
        </div>
        <div class="info-item">
          <span class="info-label">BIRTHDAY</span>
          <input v-model="user.birthday" type="date"  class="info-input" />
        </div>
        <div class="save-button-container">
          <button @click="saveChanges" class="save-button">Save Changes</button>
        </div>
      </div>
    </div>
    <div v-else>
      <p>Loading user information...</p>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import { format, parseISO } from 'date-fns';
  import enUS from 'date-fns/locale/en-US';
  
  const user = ref(null);
  const userId = localStorage.getItem('loggedUserId');
  const router = useRouter();
  

const fetchUser = async () => {
    try {
      const response = await fetch(`http://localhost:8080/WebShopAppREST/rest/users/getUser/${userId}`);
      if (response.ok) {
        const userData = await response.json();
        if (userData.birthday) {
          const dateParts = userData.birthday.split('T')[0].split('-'); // Razdvaja datum na dijelove
          const formattedDate = `${dateParts[0]}-${dateParts[1]}-${dateParts[2]}`; // Formatira datum u YYYY-MM-DD
          userData.birthday = formattedDate;
          user.value = userData;
        } else {
          console.error('Empty or null value received for birthday field');
        }
      } else {
        console.error('Failed to fetch user data:', response.status);
      }
    } catch (error) {
      console.error('Error fetching user data:', error);
    }
  };



  const saveChanges = async () => {
    try {
        const { username, firstName, lastName, gender, birthday } = user.value;
        const body = {
            username,
            firstName,
            lastName,
            gender,
            birthday
        };

        const response = await fetch(`http://localhost:8080/WebShopAppREST/rest/users/updateUser/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        });

        if (response.ok) {
            console.log('User updated successfully');
            alert("Profile updated successfully");
            router.push('/profile')
        } else {
            console.error('Failed to update user:', response.status);
        }
    } catch (error) {
        console.error('Error updating user:', error);
    }
};




  
  
  onMounted(() => {
    fetchUser();
  });
  </script>

  
  <style scoped>
  .edit-user-profile {
    text-align: center;
    font-family: Arial, sans-serif;
    border: 1px solid #000;
    padding: 20px;
    width: 500px;
    margin: 0 auto;
    margin-top: 50px;
    margin-bottom: 50px;
    border-radius: 20px;
  }
  
  .header-section {
    position: relative;
    background-color: rgb(252, 244, 234);
    color: #201d0e;
    padding: 20px;
    border-radius: 10px;
  }
  
  .profile-pic {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    margin: 0 auto;
    overflow: hidden;
  }
  
  .profile-pic img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
  
  .personal-info {
    font-size: 18px;
    margin-top: 20px;
  }
  
  .personal-info h2 {
    font-weight: 100;
    font-size: 1.3vw;
    color: #201d0e;
    margin-bottom: 20px;
    padding: 10px;
  }
  
  .info-item {
    display: flex;
    justify-content: space-between;
    margin: 10px 0;
    padding: 10px;
    border-bottom: 1px solid #ccc;
  }
  
  .info-label {
    padding: 6px;
    font-weight: 600;
    font-size: 1.1vw;
    color: #201d0e;
  }
  
  .info-value {
    font-size: 1.1vw;
    font-weight: normal;
    color: #201d0e;
  }
  
  .info-input {
    padding: 6px;
    font-size: 1.1vw;
    border-radius: 4px;
    width: 70%;
    border: 1px solid #ccc;
    border-radius: 15px;
    background-color: rgb(252, 244, 234);
  }
  
  .gender-options {
    display: flex;
    gap: 20px;
    margin-top: 5px;
  }
  
  .save-button-container {
    margin-top: 20px;
  }
  
  .save-button {
    background-color: #8f0710;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-size: 0.9em;
    transition: background-color 0.3s;
  }
  
  .save-button:hover {
    background-color: white;
    color: #503216;
    border: 1px solid #8f0710;
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
  
  </style>
  