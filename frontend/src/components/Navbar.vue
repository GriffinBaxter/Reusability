<template>
  <div>
    <nav class="navbar sticky-top navbar-expand-lg shadow text-font" style="background-color: white">
        <div class="container mt-2 my-lg-3 mx-auto">

          <!-- Logo image -->
          <div class="logo-container text-center">
            <router-link class="navbar-brand " to="/home">
  <!--            class="img-fluid d-inline-block"-->
              <img src="../../public/logo_only_med.png" alt="Logo" id="logoImage">
            </router-link>
            <p class="company-name-main">REUSABILITY</p>
            <p class="company-name-sub-heading"> - Share & Save - </p>
          </div>

          <!-- hamburger icon -->
          <button class="navbar-toggler" type="button" @click="() => toggleNavbar()">
            <span class="navbar-toggler-icon"></span>
          </button>

          <!-- Navbar links -->
          <div class="navbar-collapse" id="navbarId">
            <!-- navbar inner is required for the animation -->
            <div id="navbarInnerId" class="navbar-nav mb-2 mb-lg-0   py-3   mx-auto me-lg-0 ms-lg-auto align-items-center justify-content-center flex-column-reverse flex-lg-row">
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
                    this.showBusinessDropdown = toggleDropdownAnimated('businessDropdownLinks', 'businessDropdownLinksWrapper', this.showBusinessDropdown)
                  }">
                    Business Pages
                  </a>

                  <!-- Dropdown links-->
                  <div id="businessDropdownLinksWrapper">
                   <ul class="dropdown-menu show" id="businessDropdownLinks">
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

              <ul class="navbar-nav nav-fill flex-column flex-lg-row">
                <!-- Interact As -->
                <li id="interactDrop">
                  <a role="button" @click="() => {
                    this.showInteractMenu = toggleDropdownAnimated('interactDropdownLinks', 'interactDropdownLinksWrapper', this.showInteractMenu)
                    }">

                    <img src="../../public/profile_icon_default.png" width="27px" class="rounded-circle img-fluid act-as-image" alt="Acting as image" id="actAsImg"/> {{actAs}}
                  </a>

                  <div id="interactDropdownLinksWrapper">
                    <ul class="dropdown-menu show" id="interactDropdownLinks" >
                      <li class="nav-item" v-for="(act, index) in interactAs" :key = "index" @click="itemClicked(index)" >
                        <a class="nav-link">{{act.name}}</a>
                      </li>
                    </ul>
                  </div>
                </li>
              </ul>



            </div>
          </div>
        </div>

    </nav>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import Api from "@/Api";

export default {
  name: "Navbar",
  props: {
    // Defines if to show or hide the business acount specific links
    isBusinessAccount: {
      type: Boolean,
      default: false,
      required: false
    },
    msTransitionDelay: {
      type: Number,
      default: 300,
      required: false
    }
  },
  data() {
    return {

      // business dropdown variables
      showBusinessDropdown: false,
      // Interact as Menu
      showInteractMenu: false,
      businesses: [],
      interactAs: [],
      actAsId: null,
      actAs: "",
      currentUser: null,
      // navbar required variables
      showNavbar: false,
      navbarMaxHeight: null,                                     // max hieght of the navbar pixels
      navbarMinHeight: 0   ,                                     // min hieght of the navbar pixels
      STYLE_DEFAULT: `transition: max-height ease-in-out ${this.msTransitionDelay}ms;` // Default styling for the navbar, which allows the transition to occur. NO CHANGES HERE PLEASE!
    }
  },
  methods: {
    /**
     * Gets information about the current logged in user
     */
    getUserData() {
      const currentID = Cookies.get('userID');
      Api.getUser(currentID).then(response => (this.setCurUser(response.data))).catch((error) => {

        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noUser'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else {
          this.$router.push({path: '/noUser'});
          console.log(error.message);
        }
      })
    },
    /**
     * Calculates the target maximum height for the navbar once it needs to open.
     * @return Returns the max-height in pixels that is required for the links to appear on the screen.
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
     * Animates a slide up and down on the height of an element and it's wrapper. This is used to make the dropdown
     * appear more nicely.
     * @param dropdownId - The id of the dropdown element.
     * @param dropdownWrapperId - The ide of the dropdown wrapper element.
     * @param toggleVariable - The varaible that the dropdown depends on.
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

        // Update the navbar to accomidate the changes
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
      if (document.getElementById("navbarId")) {

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
        document.getElementById("navbarId").setAttribute("style", `max-height: ${targetHeight}px; ${this.STYLE_DEFAULT}`)
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
      Cookies.remove('name', {path: '/'}); // removed!
      Cookies.remove('userID');
      await this.$router.push({name: 'Login'});
    },
    /**
     * Shows Interact As Dropdown menu
     */
    showInteract() {
      this.showInteractMenu = !this.showInteractMenu;
    },
    /**
     * Refreshes dropdown list for interact as
     */
    refreshDropdown() {
      if (this.currentUser.nickname == null)
      {
        this.interactAs = [ {id:this.currentUser.id, name:this.currentUser.firstName} ];
      } else {
        this.interactAs = [ {id:this.currentUser.id, name:this.currentUser.nickname} ];
      }
      for (let i=0; i<this.businesses.length; i++) {
        this.interactAs.push(this.businesses[i]);
      }
    },
    /**
     * Sets who the user is interacting as
     * @param index of dropdown clicked
     */
    itemClicked(index) {
      this.showInteractMenu = this.toggleDropdownAnimated('interactDropdownLinks', 'interactDropdownLinksWrapper', this.showInteractMenu)
      if (index === 0)
      {
        // Delete Cookie
        Cookies.remove('actAs');
        //
        this.actAsId = null;
        if (this.currentUser.nickname) {
          this.actAs = this.currentUser.nickname;
        } else {
          this.actAs = this.currentUser.firstName;
        }
      } else {
        this.thumbnail = null;
        Cookies.set('actAs', this.interactAs[index].id);
        this.actAsId = this.interactAs[index].id;
        this.actAs = this.interactAs[index].name;
      }
    },
    setCurUser(response) {
      this.currentUser = response;
      if (Cookies.get('actAs')) {
        this.actAsId = Cookies.get('actAs');
      } else {
        if (response.nickname == null) {
          this.actAs = response.firstName;
        } else {
          this.actAs = response.nickname;
        }
      }
      this.businesses = response.businessesAdministered;
      this.refreshDropdown();
    }
  },
  mounted() {
    this.getUserData();
    // Sample the navbar max height at mounting
    this.navbarMaxHeight = this.getNavbarMaxHeight();

    // Otherwise keep sampling until you get a valid result (i.e. not null)
    if (this.navbarMaxHeight == null) {
      while (this.navbarMaxHeight == null) this.navbarMaxHeight = this.getNavbarMaxHeight();
    }

    // Set the inital height for the navbar and the dropdown
    this.toggleDropdownAnimated('businessDropdownLinks', 'businessDropdownLinksWrapper', this.showBusinessDropdown, true);
    this.toggleDropdownAnimated('interactDropdownLinks', 'interactDropdownLinksWrapper', this.showInteractMenu, true);
    this.toggleNavbar(true)
  }

}
</script>

<style scoped>

  .logo-container {
    position: center;
  }

  #interactDropdownLinksWrapper {
    margin-top: 60px; /* height between profile image and drop down buttons*/
    width: auto;
  }

  #interactDrop {
    display: flex;
    flex-flow: column wrap;
    align-items: center;
    max-width: 180px;
    height: auto;
    margin-left: 50px;
    padding-left: 1em;
  }

  #interactDrop a {
    display: flex;
    flex-flow: row wrap;
    justify-content: center;
    align-items: center;
  }

  .act-as-image {
    height: 55px;
    width: auto;
    border: 1px lightgrey solid;
    margin-right: 15px;
  }

  #actAsImg {
    float: none;
  }

  #logoImage {
    max-width: 200px;
    margin-left: 28px;
    margin-right: 10px;
    width: 100%;
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

  #businessDropdownLinksWrapper, #interactDropdownLinksWrapper{
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
    /*margin: 1.2rem 0; Margins cannot be calculated in pxiels :( */
  }

  /*LG Break point*/
  @media(min-width: 992px) {
    #navbarId {
      overflow: visible;
    }
    #actAsImg {
      float: left;
    }
    #businessDropdownLinksWrapper, #interactDropdownLinksWrapper{
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

.active {
  background-color: #2eda77;
}

</style>
