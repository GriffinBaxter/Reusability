<!--https://stackoverflow.com/questions/45000510/vue-js-error-component-template-should-contain-exactly-one-root-element-->
<!--https://stackoverflow.com/questions/51516084/how-do-i-add-a-google-font-to-a-vuejs-component/51517799-->
<!--<link rel="preconnect" href="https://fonts.gstatic.com">-->
<!--<link href="https://fonts.googleapis.com/css2?family=Oswald&display=swap" rel="stylesheet">-->
<!--<link rel="preconnect" href="https://fonts.gstatic.com">-->
<!--<link href="https://fonts.googleapis.com/css2?family=Oswald&display=swap" rel="stylesheet">-->

<template>
  <nav class="navbar sticky-top navbar-expand-lg shadow" style="background-color: white">
      <div class="container mt-2 my-lg-3 mx-auto">

        <!-- Logo image -->
        <router-link class="navbar-brand" to="/home">
          <img src="../../public/logo.png" alt="Logo" class="img-fluid d-inline-block" id="logoImage">
          <!--          <p class="company-name-main">Reusability</p>-->
          <!--          <p class="company-name-sub-heading"> - Share & Save - </p>-->
        </router-link>

        <!-- hamburger icon -->
        <button class="navbar-toggler" type="button" @click="() => toggleNavbar()">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar links -->
        <div class="navbar-collapse" id="navbarId">
          <div id="navbarInnerId" class="navbar-nav mb-2 mb-lg-0   py-3   mx-auto me-lg-0 ms-lg-auto">
            <ul class="navbar-nav nav-fill flex-column flex-lg-row">
              <li class="nav-item">
                <router-link :class="['nav-link ', isActivePath('Home')]" to="/home">Home</router-link>
              </li>
              <li class="nav-item">
                <router-link :class="['nav-link', isActivePath('Profile')]" to="/profile">Profile</router-link>
              </li>
              <li class="nav-item">
                <a class="nav-link" style="cursor: pointer" @click="e =>logout(e)">Log out</a>
              </li>
            </ul>
          </div>
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
      navbarMaxHeight: null,  // max hieght of the navbar pixels
      navbarMinHeight: 0   ,  // min hieght of the navbar pixels
      STYLE_DEFAULT: `transition: max-height ease-in-out 300ms;`// overflow: hidden`
    }
  },
  methods: {
    /**
     *
     * */
    getNavbarMaxHeight() {
      let result = null;

      // Only runs if there is a navbar item existing. Otherwise we return null to avoid accessing
      // a non existant attribute
      if (document.getElementById("navbarInnerId")) {
        result = document.getElementById("navbarInnerId").offsetHeight
      }
      return result
    },
    /**
     *
     * @return
     */
    toggleNavbar(initCall = false) {

      // Only if the element exists
      if (document.getElementById("navbarId")) {

        // Update the max height before applying any transitions
        this.navbarMaxHeight = this.getNavbarMaxHeight()

        // If init call don't toggle the height
        if (!initCall) {
          this.showNavbar = !this.showNavbar
        }
        // Determine the target height
        let targetHeight = this.navbarMinHeight
        if (this.showNavbar) targetHeight = this.navbarMaxHeight

        // Assign the target height to the navbar
        document.getElementById("navbarId").setAttribute("style", `max-height: ${targetHeight}px; ${this.STYLE_DEFAULT}`)
      }
    },
    /**
     *
     * @param pathName -
     * @return
     */
    isActivePath(pathName) {
      return this.$route.name === pathName ? "active" : ""
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
      Cookies.remove('name', {path: '/'}); // removed!
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

    // Set navbar to inital height!
    this.toggleNavbar(true)
  }

}
</script>

<style scoped>
  #logoImage {
    max-width: 200px;
    margin-right: 40px;
  }

  .nav-link {
    color: white;
    background: #19b092;  /* fallback for old browsers */
    /*background: -webkit-linear-gradient(to right, #a8e063, #56ab2f);  !* Chrome 10-25, Safari 5.1-6 *!*/
    /*background: linear-gradient(to right, #199164, #24e09a); !* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ *!*/

    margin: 10px 0;
    border-radius: 15px;
    text-align: center;
    font-size: large;
    width: auto;
  }

  .nav-link:hover {
    background: #ef5e33;
  }

.navbar-toggler {
  color: rgba(25,176,146, 0.55);
  border-color: rgba(0, 0, 0, 0.2);
  border-width: 2px;
  border-radius: 0.6rem;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%2825,176,146, 0.55%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
}
  #navbarId {
    overflow: hidden;
  }


/*LG Break point*/
@media(min-width: 992px) {
  #navbarId {
    overflow: visible;
  }

  .nav-link {
    width: 120px;
    margin: 10px;
  }
}


  /*.company-name-main {*/
  /*  font-family: 'Oswald', sans-serif;*/
  /*}*/

  /*.company-name-sub-heading {*/
  /*  font-family: 'Oswald', sans-serif;*/
  /*}*/
</style>
