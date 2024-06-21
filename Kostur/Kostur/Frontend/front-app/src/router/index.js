import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import FactoriesView from '../views/FactoriesView';
import FactoryView from '../views/FactoryView';
import AddChocolateView from '../components/AddChocolateView.vue';
import EditChocolateView from '../components/EditChocolateView.vue';
import Registration from '../views/RegistrationView.vue';

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
    path: "/registration", // putanja za stranicu dodavanja čokolade
    name: "registration", // Naziv rute
    component: Registration,
  },
  
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
