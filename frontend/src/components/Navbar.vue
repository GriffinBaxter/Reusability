<!--https://stackoverflow.com/questions/45000510/vue-js-error-component-template-should-contain-exactly-one-root-element-->
<!--<link rel="preconnect" href="https://fonts.gstatic.com">-->
<!--<link href="https://fonts.googleapis.com/css2?family=Oswald&display=swap" rel="stylesheet">-->
<!--<link rel="preconnect" href="https://fonts.gstatic.com">-->
<!--<link href="https://fonts.googleapis.com/css2?family=Oswald&display=swap" rel="stylesheet">-->

<template>
  <nav class="navbar sticky-top  navbar-expand-lg navbar-light shadow" style="background-color: white">

      <div class="container my-3 mx-auto">

        <!-- Logo image -->
        <router-link class="navbar-brand " to="/home">
          <img src="../../public/logo.png" alt="Logo" class="img-fluid d-inline-block" id="logoImage">
<!--          <p class="company-name-main">Reusability</p>-->
<!--          <p class="company-name-sub-heading"> - Share & Save - </p>-->
        </router-link>

        <!-- hamburger icon -->
        <button class="navbar-toggler" type="button" @click="showNavbar = !showNavbar">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar links -->
        <div :class="['navbar-collapse', 'collapse', toggleNavbar()]" style="transition: all ease-in-out 300ms">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <router-link :class="['nav-link', isActivePath('Home')]" aria-current="page" to="">Home</router-link>
            </li>
            <li class="nav-item">
              <router-link :class="['nav-link', isActivePath('Profile')]" to="/profile">Profile</router-link>
            </li>

          </ul>
          <ul class="nav navbar-nav ">
            <li class="nav-item">
              <a class="nav-link" style="cursor: pointer" @click="e =>logout(e)">Log out</a>
            </li>
          </ul>
        </div>

      </div>
  </nav>
</template>

<script>
import Cookies from "js-cookie";

export default {
  name: "individualNavbar",
  data() {
    return {
      showNavbar: false,
    }
  },
  methods: {
    /**
     *
     * @return
     */
    toggleNavbar() {
      if (this.showNavbar) {
        return "show"
      } else {
        return ""
      }
    },

    /**
     *
     * @param pathName -
     * @return
     */
    isActivePath(pathName) {
      if (this.$route.name === pathName) {
        return "active"
      } else {
        return ""
      }
    },
    /**
     *
     * @param event
     */
    async logout(event) {
      /*
      Logs the user out of the site by deleting the relevant cookies and redirecting to the login page.
       */
      event.preventDefault();
      Cookies.remove('name', { path: '/' }); // removed!
      Cookies.remove('userID');
      this.$router.push({name: 'Login'});
    }
  }
}
</script>

<style scoped>
  #logoImage {
    max-width: 200px;
  }

  .company-name-main {
    font-family: 'Oswald', sans-serif;
  }

  .company-name-sub-heading {
    font-family: 'Oswald', sans-serif;
  }
</style>