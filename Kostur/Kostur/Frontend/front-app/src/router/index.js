import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import FactoriesView from '../views/FactoriesView';
import FactoryView from '../views/FactoryView';
import AddChocolateView from '../components/AddChocolateView.vue';
import EditChocolateView from '../components/EditChocolateView.vue';

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
    path: "/add-chocolate", // putanja za stranicu dodavanja čokolade
    name: "addChocolate", // Naziv rute
    component: AddChocolateView,
  },
  {
    path: "/edit-chocolate/:id", // putanja za stranicu dodavanja čokolade
    name: "editChocolate", // Naziv rute
    component: EditChocolateView,
  },
  
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
