<!--https://stackoverflow.com/questions/45000510/vue-js-error-component-template-should-contain-exactly-one-root-element-->
<!--<link rel="preconnect" href="https://fonts.gstatic.com">-->
<!--<link href="https://fonts.googleapis.com/css2?family=Oswald&display=swap" rel="stylesheet">-->
<!--<link rel="preconnect" href="https://fonts.gstatic.com">-->
<!--<link href="https://fonts.googleapis.com/css2?family=Oswald&display=swap" rel="stylesheet">-->

<template>
  <nav class="navbar sticky-top  navbar-expand-lg navbar-light shadow" style="background-color: white">

      <div class="container mt-2 my-lg-3 mx-auto">

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
        <div :class="['navbar-collapse', 'collapse', toggleNavbar()]" id="navbarId">
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
      prevShowNavbar: false,
      navbarMaxHeight: null, // max hieght of the navbar pixels
      navbarMinHeight: 0,    // min hieght of the navbar pixels
      transitionDelay: 350,  // transition delay in ms
      transitioning: false
    }
  },
  methods: {
    /**
     *
     * */
    getNavbarMaxHeight() {
      let result = null;

      // Ensures that the navbar is actually there
      if (document.getElementById("navbarId")) {
        // Shows the navbar just so we can sample the height.
        document.getElementById("navbarId").classList.add("show")

        // Stores the result
        result = document.getElementById("navbarId").clientHeight

        // Hides the navbar quickly
        document.getElementById("navbarId").classList.remove("show")
      }
      return result
    },
    /**
     *
     * @return
     */
    toggleNavbar() {

      const STYLE_DEFAULT = `; transition: height ease ${this.transitionDelay}`;
      const HEIGHT_CHANGE_OFFSET = 20 // ms

      if (!document.getElementById("navbarId")) {
        // Are we transitiong
        if (this.prevShowNavbar !== this.showNavbar) {
          this.transitioning = true
          // Are we transition from max --> min or min --> max
          if (this.showNavbar) {
            // min --> max
            document.getElementById("navbarId").setAttribute("style", `height: ${this.navbarMinHeight}${STYLE_DEFAULT}`)

            setTimeout( () => {
              document.getElementById("navbarId").setAttribute("style", `height: ${this.navbarMaxHeight}${STYLE_DEFAULT}`)
            }, HEIGHT_CHANGE_OFFSET)

            setTimeout( () => {
              return  this.showNavbar ? "show" : "";
            })
          } else {
            // max --> min
            return  this.showNavbar ? "show" : "";
          }
        } else {
          return  this.showNavbar ? "show" : "";
        }
      } else {
        // Designate whether or not to show the navbar
        return this.showNavbar ? "show" : "";
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
      await this.$router.push({name: 'Login'});
    }
  },
  mounted() {
    // Sample the navbar max height at mounting
    this.navbarMaxHeight = this.getNavbarMaxHeight();

    // Otherwise keep sampling until you get a valid result (i.e. not null)
    if (this.navbarMaxHeight == null) {
      while (this.navbarMaxHeight == null) this.navbarMaxHeight = this.getNavbarMaxHeight();
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