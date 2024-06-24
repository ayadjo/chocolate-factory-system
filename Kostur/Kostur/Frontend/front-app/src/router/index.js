import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import FactoriesView from '../views/FactoriesView';
import FactoryView from '../views/FactoryView';
import AddChocolateView from '../components/AddChocolateView.vue';
import EditChocolateView from '../components/EditChocolateView.vue';
import Registration from '../views/RegistrationView.vue';
import Login from '../views/LoginView.vue';
import CreateFactory from '../views/CreateFactoryView.vue';
import Basket from '../components/Basket.vue';
import Profile from '../views/ProfileView.vue';
import EditProfile from '../views/EditProfileView.vue';
import FactoryPurchases from '../components/FactoryPurchases.vue';

const routes = [
  {
    path: "/",
    name: "home",
    component: FactoriesView,
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },

  {
    path: "/factory/:id",
    name: "factory",
    component: FactoryView,
    props: true,
  },

  {
    path: "/add-chocolate/:id", 
    name: "addChocolate", 
    component: AddChocolateView,
  },

  {
    path: "/edit-chocolate/:id",
    name: "editChocolate", 
    component: EditChocolateView,
  },

  {
    path: "/registration", 
    name: "registration", 
    component: Registration,
  },

  {
    path: "/login", 
    name: "login", 
    component: Login,
  },

  {
    path: "/new-factory",
    name: "newFactory", 
    component: CreateFactory,
  },

  {
    path: "/basket", 
    name: "basket", 
    component: Basket,
  },
  
  {
    path: "/profile",
    name: "profile", 
    component: Profile,
  },

  {
    path: '/edit-profile/:id',
    name: 'editProfile',
    component: EditProfile,
    props: true 
  },

  {
    path: '/factory-purchases/:id',
    name: 'factoryPurchases',
    component: FactoryPurchases,
    props: true 
  }
  
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
