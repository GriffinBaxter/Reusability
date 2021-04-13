<!--This file creates the navigation (nav) bar. It is used on all pages, excluding the login page and registration
    page.-->
<!--There is a navigation bar specific for an individual and a business.-->
<!--The individual nav bar contains links to the Home, Profile, and Logout pages.-->
<!--The business nav bar contains the same links as the individual page with the addition of a drop down menu which
    contains-->
<!--links to the business' listings, inventory and catalogue pages.-->
<!--Bootstrap has been used to build these nav bars.-->


<!-------------------------------------------- Navigation Bar --------------------------------------------------------->

<template>
  <nav class="navbar sticky-top navbar-expand-lg shadow text-font" style="background-color: white">
      <div class="container mt-2 my-lg-3 mx-auto">

        <!-- Logo image -->
        <div class="logo-container text-center">
          <router-link class="navbar-brand " to="/home">
<!--            class="img-fluid d-inline-block"-->
            <img src="../../public/logo_only_med.png" alt="Logo" id="logo-image">
          </router-link>
          <p class="company-name-main">REUSABILITY</p>
          <p class="company-name-sub-heading"> - Share & Save - </p>
        </div>

        <!-- Hamburger icon -->
        <button class="navbar-toggler" type="button" @click="() => toggleNavbar()">
          <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar links -->
        <div class="navbar-collapse" id="navbar-id">
          <!-- navbar inner is required for the animation -->
          <div id="navbar-inner-id" class="navbar-nav mb-2 mb-lg-0   py-3   mx-auto me-lg-0 ms-lg-auto">
            <ul class="navbar-nav nav-fill flex-column flex-lg-row">

              <!-- default page links -->
              <li class="nav-item">
                <router-link :class="['nav-link ', isActivePath('/home')]" to="/home">Home</router-link>
              </li>
              <li class="nav-item">
                <router-link :class="['nav-link', isActivePath('/profile')]" to="/profile">Profile</router-link>
              </li>

              <!--- Business specific account links -->
              <li class="nav-item dropdown" v-if="isBusinessAccount">

                <!-- Navbar toggle drop down -->
                <a class="nav-link dropdown-toggle" role="button" @click="() => {
                  this.showBusinessDropdown = toggleDropdownAnimated('business-dropdown-links', 'business-dropdown-links-wrapper', this.showBusinessDropdown)
                }">
                  Business Pages
                </a>

                <!-- Dropdown links-->
                <div id="business-dropdown-links-wrapper">
                 <ul class="dropdown-menu show" id="business-dropdown-links">
                      <li class="nav-item">
                        <router-link :class="['nav-link ', isActivePath('/')]" to="/">Business Listings</router-link>
                      </li>
                      <li class="nav-item">
                        <router-link :class="['nav-link', isActivePath('/')]" to="/">Inventory</router-link>
                      </li>
                      <li class="nav-item">
                        <router-link :class="['nav-link', isActivePath('/')]" to="/">Catalogue</router-link>
                      </li>
                 </ul>
                </div>

              </li>

              <!-- Log out link-->
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
  name: "Navbar",
  props: {

    // Defines if to show or hide the business account specific links
    isBusinessAccount: {
      type: Boolean,
      default: false,
      required: false
    },

    // Dictates the transition animation time
    msTransitionDelay: {
      type: Number,
      default: 300,
      required: false
    },

    // Determines if you are required to be logged in to view the current page.
    loginRequired: {
      type: Boolean,
      default: true,
      required: false
    }
  },

  data() {
    return {

      // business dropdown variables
      showBusinessDropdown: false,

      // navbar required variables
      showNavbar: false,
      navbarMaxHeight: null,                                     // max height of the navbar pixels
      navbarMinHeight: 0   ,                                     // min height of the navbar pixels
      STYLE_DEFAULT: `transition: max-height ease-in-out ${this.msTransitionDelay}ms;`
      // Default styling for the navbar, which allows the transition to occur. NO CHANGES HERE PLEASE!
    }
  },

  methods: {
    /**
     * Calculates the target maximum height for the navbar once it needs to open.
     * @return Returns the max-height in pixels that is required for the links to appear on the screen.
     * */
    getNavbarMaxHeight() {
      let result = null;

      // Only runs if there is a navbar item existing. Otherwise we return null to avoid accessing
      // a non-existent attribute
      if (document.getElementById("navbar-inner-id")) {
        result = document.getElementById("navbar-inner-id").offsetHeight
      }
      return result
    },

    /**
     * Animates a slide up and down on the height of an element and it's wrapper. This is used to make the dropdown
     * appear more nicely.
     * @param dropdownId - The id of the dropdown element.
     * @param dropdownWrapperId - The ide of the dropdown wrapper element.
     * @param toggleVariable - The variable that the dropdown depends on.
     * @param preventToggle - Gives the option to prevent the variable from being toggled. This defaults to false.
     * @param minHeight - Gives the option to give a custom minimum height. But zero if a good default.
     *
     * @return - Returns the new value for the toggle variable so it can be updated (cannot update directly as far as I know).
     */
    toggleDropdownAnimated(dropdownId, dropdownWrapperId, toggleVariable,  preventToggle = false, minHeight = 0) {
      // To save accessing time
      let dropdownElement =  document.getElementById(dropdownId);
      let wrapperElement =  document.getElementById(dropdownWrapperId);

      // Only toggle it if you can find it!
      if (dropdownElement && wrapperElement) {
        // Gets the maximum height from the offset!
        const maxHeight = dropdownElement.offsetHeight

        // Toggle the variable unless not instructed to!
        if (!preventToggle) {
          toggleVariable = !toggleVariable;
        }

        // Determining the height we want to reach
        let targetHeight = minHeight;
        if (toggleVariable) targetHeight = maxHeight;

        // Update the target height for the component
        wrapperElement.setAttribute("style", `max-height: ${targetHeight}px; ${this.STYLE_DEFAULT}`)

        // Update the navbar to accommodate the changes
        this.toggleNavbar(true, targetHeight);

        // So the toggle variable can be updated
        return toggleVariable;
      }
    },

    /**
     * Toggles the navbar in mobile form which shows the links to the user. This targets "navbarId".
     * The function also controls the animation for showing and hiding the links in the navbar.
     *
     * @param preventToggle - Determines if to prevent the toggling action. This is useful in some cases.
     * @param extraMaxPixels - Determines additional pixels to add to the maximum height.
     */
    toggleNavbar(preventToggle = false, extraMaxPixels = 0) {

      // Only if the element exists
      if (document.getElementById("navbar-id")) {

        // Update the max height before applying any transitions
        this.navbarMaxHeight = this.getNavbarMaxHeight() + extraMaxPixels;

        // If init call don't toggle the height
        if (!preventToggle) {
          this.showNavbar = !this.showNavbar
        }

        // Determine the target height
        let targetHeight = this.navbarMinHeight
        if (this.showNavbar) targetHeight = this.navbarMaxHeight

        // Assign the target height to the navbar
        document.getElementById("navbar-id").setAttribute("style", `max-height: ${targetHeight}px; ${this.STYLE_DEFAULT}`)
      }
    },
    /**
     * Determines if the current route path matches the path given.
     * @param path - the path you want to test against the route path.
     * @return Returns "active" class keywrod if the paths match. Otherwise "" for an empty class.
     */
    isActivePath(path) {
      return this.$route.path === path ? "active" : ""
    },
    /**
     * Logs the user out of the their account by deleting the cookies of the user, and sending them to
     * the login page.
     * @param event - The button event when it is called from the button press.
     */
    async logout(event) {
      /*
      Logs the user out of the site by deleting the relevant cookies and redirecting to the login page.
       */
      event.preventDefault();

      // Reason for this not working is because it is HttpOnly, which doesn't allow the browser/ JS to
      // delete this cookie.
      Cookies.remove('JSESSIONID', {path: '/'});
      Cookies.remove('userID');
      await this.$router.push({name: 'Login'});
    },
    /**
     * The function when called ensure the user is logged in. Otherwise takes you to the login page.
     */
    async ensureLoggedIn() {
      const userIdCookie = await Cookies.get('userID');
      // There is no way to check the JSESSIONID cookie without having an API call. This is because this is
      // a HttpOnly cookie, which means we cannot do anything with it on the client side via Javascript.
      //const jSessionIdCookie = await Cookies.get('JSESSIONID');

      // If either of the cookies are missing this means that the user is not logged in.
      // Then we logout the user, which takes them to the login page and deletes their cookies.
      if (userIdCookie === undefined) {
        await this.logout(new Event("Not logged in"));
      }
    }
  },

  beforeMount() {
    // If it is required to be logged in. The user will be checked.
    if (this.loginRequired) {
      this.ensureLoggedIn();
    }
  },
  mounted() {

    // Sample the navbar max height at mounting
    this.navbarMaxHeight = this.getNavbarMaxHeight();

    // Otherwise keep sampling until you get a valid result (i.e. not null)
    if (this.navbarMaxHeight == null) {
      while (this.navbarMaxHeight == null) this.navbarMaxHeight = this.getNavbarMaxHeight();
    }

    // Set the inital height for the navbar and the dropdown
    this.toggleDropdownAnimated('business-dropdown-links', 'business-dropdown-links-wrapper', this.showBusinessDropdown, true);
    this.toggleNavbar(true)
  }

}
</script>

<!-------------------------------------------- Navigation Bar Styling ------------------------------------------------->

<style scoped>

  .logo-container {
    position: center;
  }

  #logo-image {
    max-width: 200px;
    margin-left: 28px;
    margin-right: 10px;
    width: 100%;
  }

  .nav-link {
    color: white;
    background: #19b092;

    /* fallback for old browsers */
    background: -webkit-linear-gradient(to right, #a8e063, #56ab2f);  /* Chrome 10-25, Safari 5.1-6 */
    background: linear-gradient(to right, #199164, #24e09a); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */

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
    #navbar-id {
      overflow: hidden;
    }

  #business-dropdown-links-wrapper{
    position: relative;
    overflow: hidden;
  }

  .active {
    background-color: #2eda77;
  }

  .dropdown-menu {
    border-right-width: 0;
    border-left-width: 0;
    padding: 0 5rem;
    /* margin: 1.2rem 0; Margins cannot be calculated in pixels :( */
  }

  /*LG Break point*/
  @media(min-width: 992px) {
    #navbar-id {
      overflow: visible;
    }

    #business-dropdown-links-wrapper{
      position: absolute;
    }

    .navbar-expand-lg .navbar-nav .dropdown-menu {
      padding: 0;
      margin: 0;
      border-right-width: 1px;
      border-left-width: 1px;
      position: unset;
    }

    .navbar-expand-lg .navbar-nav .nav-link {
      margin: 10px;
      padding-left: 1em;
      padding-right: 1em;
    }

}

</style>
