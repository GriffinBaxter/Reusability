<template>
  <div class="container">

      <div class="col">
        <div class="row ">
          <div class="fas fa-bars px-0" @click="showMenu = !showMenu"><font-awesome-icon icon="bars" /></div>
        </div>

        <div class="row">
          <ul class="nav flex-column" v-if="showMenu">
            <li class="nav-item my-1" v-for="link in links" :key="link.name">
              <router-link :to="link.to">
                <button class="btn btn-primary" v-if="$route.path === link.to">
                  {{link.name}}
                </button>
                <button class="btn btn-outline-primary" v-else>
                  {{link.name}}
                </button>
              </router-link>
            </li>
          </ul>
        </div>
      </div>

  </div>
</template>

<script>
import {NavLink} from "./linkClass"

export default {
  name: "burgerMenu",
  data () {
    return {
      showMenu: true,
      links: [
        new NavLink("Registration", "/registration"),
        new NavLink("Login", "/"),
        new NavLink("Profile", "/profile"),
      ]
    }
  }
}
</script>

<style scoped>

</style>