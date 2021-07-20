<template>

  <div id="mySidenav" class="sidenav">

    <!-- side nav bar close button with 'x' icons -->
    <a href="javascript:void(0)" class="closebtn" @click="() => closeSideNav()">&times;</a>

    <!-- default page links -->
    <li class="nav-item">
      <router-link :class="['nav-link ', isActivePath('/home')]" to="/home" tabindex="1">

        <div class="row">
          <div class="col-sm-4">
            <i class="side-nav-link-icon fas fa-home"></i>
          </div>
          <div class="col-md-1 side-nav-link-text">
            Home
          </div>
        </div>

      </router-link>
    </li>
    <li class="nav-item" v-if="actAsId === null">
      <router-link :class="['nav-link', isActivePath('/profile')]" to="/profile" tabindex="2">

        <div class="row">
          <div class="col-sm-4">
            <i class="side-nav-link-icon fas fa-user-alt"></i>
          </div>
          <div class="col-md-1 side-nav-link-text">
            Profile
          </div>
        </div>

      </router-link>
    </li>
    <li class="nav-item" v-if=actAsId>
      <router-link :class="['nav-link', isActivePath('/businessProfile/' + actAsId)]"
                   :to="'/businessProfile/' + actAsId" tabindex="2">

        <div class="row">
          <div class="col-sm-4">
            <i class="side-nav-link-icon fas fa-user-alt"></i>
          </div>
          <div class="col-md-1 side-nav-link-text">
            Profile
          </div>
        </div>

      </router-link>
    </li>
    <li class="nav-item">
      <router-link :class="['nav-link', isActivePath('/marketplace')]" to="/marketplace" tabindex="3">

        <div class="row">
          <div class="col-sm-4">
            <i class="side-nav-link-icon fas fa-store"></i>
          </div>
          <div class="col-md-1 side-nav-link-text">
            Marketplace
          </div>
        </div>

      </router-link>
    </li>

    <!--- Business specific account links -->
    <li class="nav-item dropdown" v-if="isActAsBusiness">

      <!-- Navbar toggle drop down -->
      <a class="nav-link dropdown-toggle" role="button" tabindex="4"
         @click="() => {toggleBusinessDropdown()}"
         @keyup.enter="() => {toggleBusinessDropdown()}">

        <div class="row">
          <div class="col-sm-4">
            <i class="side-nav-link-icon fas fa-briefcase"></i>
          </div>
          <div class="col-md-1 side-nav-link-text">
            Business
          </div>
        </div>

      </a>

      <!-- Dropdown links-->
      <div id="business-dropdown-links-wrapper-side">
        <ul class="dropdown-menu show" id="business-dropdown-links-side">
          <li class="nav-item">
            <router-link
                :class="['nav-link ', isActivePath('/businessProfile/' + businessAccountId + '/listings')]"
                :to="'/businessProfile/' + businessAccountId + '/listings'" tabindex="-1">
              Listings
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
                :class="['nav-link', isActivePath('/businessProfile/' + businessAccountId + '/inventory')]"
                :to="'/businessProfile/' + businessAccountId + '/inventory'" tabindex="-1">
              Inventory
            </router-link>
          </li>
          <li class="nav-item">
            <router-link
                :class="['nav-link', isActivePath('/businessProfile/' + businessAccountId + '/productCatalogue')]"
                :to="'/businessProfile/' + businessAccountId + '/productCatalogue'" tabindex="-1">
              Catalogue
            </router-link>
          </li>

        </ul>
      </div>

      <!-- Log out link-->
    <li class="nav-item">
      <a class="nav-link" style="cursor: pointer" tabindex="5" @click="e =>logout(e)" @keyup.enter="e =>logout(e)">

        <div class="row">
          <div class="col-sm-4">
            <i class="side-nav-link-icon fas fa-sign-out-alt"></i>
          </div>
          <div class="col-md-1 side-nav-link-text">
            Logout
          </div>
        </div>

      </a>
    </li>

  </div>

</template>

<script>
import Cookies from "js-cookie";
import Api from "../../Api";

export default {
  name: "SideNavBar",
  props: {
    msTransitionDelay: {
      type: Number,
      default: 300,
      required: false
    },
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
      navbarMaxHeight: null,                                     // max height of the navbar pixels
      navbarMinHeight: 0,                                     // min height of the navbar pixels
      STYLE_DEFAULT: `transition: max-height ease-in-out ${this.msTransitionDelay}ms;`,
      // Default styling for the navbar, which allows the transition to occur. NO CHANGES HERE PLEASE!
      isActAsBusiness: false,
      businessAccountId: null,

      // omit part of name
      showOmitName: null,
      interactAsOmit: [],
      actAsOmit: "",

      // Watch window width
      screenWidth: document.body.clientWidth,
      maxNameLength: 30,
      omitPoint: 10
    }
  },
  methods: {
    /**
     *  Set the width of the side navigation to 0 and the left margin of the page content to 0
     */
    closeSideNav() {
      document.getElementById("mySidenav").style.width = "0";
      document.getElementById("main").style.marginLeft = "0";
    },
    /**
     * Toggle the interactAs menu dropdown
     */
    toggleInteractAs() {
      this.showInteractMenu = this.toggleDropdownAnimated('interact-dropdown-links',
          'interact-dropdown-links-wrapper', this.showInteractMenu);
    },
    /**
     * Toggle the business menu dropdown
     */
    toggleBusinessDropdown() {
      this.showBusinessDropdown = this.toggleDropdownAnimated('business-dropdown-links-side',
          'business-dropdown-links-wrapper-side', this.showBusinessDropdown);
    },
    /**
     * omit name which length longer than max.
     */
    omitName(name, max) {
      if (name.length > max) {
        name = name.slice(0, max) + '...';
      }
      return name
    },
    /**
     * Gets information about the current logged in user
     */
    getUserData() {
      const currentID = Cookies.get('userID');
      if (currentID) {
        Api.getUser(currentID).then(response => (this.setCurUser(response.data))).catch((error) => {
          if (error.request && !error.response) {
            this.$router.push({path: '/timeout'});
          } else if (error.response.status === 401) {
            this.$router.push({path: '/invalidtoken'});
          } else {
            this.$router.push({path: '/noUser'});
            console.log(error.message);
          }
        })
      }
    },
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
     * @param dropdownWrapperId - The id of the dropdown wrapper element.
     * @param toggleVariable - The variable that the dropdown depends on.
     * @param preventToggle - Gives the option to prevent the variable from being toggled. This defaults to false.
     * @param minHeight - Gives the option to give a custom minimum height. But zero if a good default.
     *
     * @return - Returns the new value for the toggle variable so it can be updated (cannot update directly as far as I know).
     */
    toggleDropdownAnimated(dropdownId, dropdownWrapperId, toggleVariable, preventToggle = false, minHeight = 0) {
      // To save accessing time
      let dropdownElement = document.getElementById(dropdownId);
      let wrapperElement = document.getElementById(dropdownWrapperId);

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

      }


      // So the toggle variable can be updated
      return toggleVariable;
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
        document.getElementById("navbar-id")
            .setAttribute("style", `max-height: ${targetHeight}px; ${this.STYLE_DEFAULT}`)
      }
    },
    /**
     * Determines if the current route path matches the path given.
     * @param path - the path you want to test against the route path.
     * @return Returns "active" class keyword if the paths match. Otherwise "" for an empty class.
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

      Cookies.remove('userID');
      Cookies.remove('actAs');

      Api.signOut().then(() => {
        this.$router.push({name: 'Login'})
      })
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
      if (this.currentUser.nickname == null) {
        this.interactAs = [{
          id: this.currentUser.id,
          name: this.omitName(this.currentUser.firstName, this.maxNameLength)
        }];

        // store a list of name with an max length
        this.interactAsOmit = [{
          id: this.currentUser.id,
          name: this.omitName(this.currentUser.firstName, this.omitPoint)
        }];
      } else {
        this.interactAs = [{
          id: this.currentUser.id,
          name: this.omitName(this.currentUser.nickname, this.maxNameLength)
        }];

        // store a list of name with an max length
        this.interactAsOmit = [{
          id: this.currentUser.id,
          name: this.omitName(this.currentUser.nickname, this.omitPoint)
        }];
      }

      for (let i = 0; i < this.businesses.length; i++) {
        this.interactAs.push({
          id: this.businesses[i].id,
          name: this.omitName(this.businesses[i].name, this.maxNameLength)
        });
        this.interactAsOmit.push({
          id: this.businesses[i].id,
          name: this.omitName(this.businesses[i].name, this.omitPoint)
        });
      }
      this.$emit('getLinkBusinessAccount', this.businesses);
    },
    /**
     * Sets who the user is interacting as
     * @param index of dropdown clicked
     */
    itemClicked(index) {
      this.showInteractMenu = this.toggleDropdownAnimated('interact-dropdown-links',
          'interact-dropdown-links-wrapper', this.showInteractMenu)
      if (index === 0) {
        // Delete Cookie
        Cookies.remove('actAs');
        this.isActAsBusiness = false;
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
        this.businessAccountId = this.interactAs[index].id;
        this.isActAsBusiness = true;
        this.actAsId = this.interactAs[index].id;
        this.actAs = this.interactAs[index].name;
      }
      this.actAs = this.omitName(this.actAs, this.maxNameLength)
      this.actAsOmit = this.omitName(this.actAs, this.omitPoint)
      this.$router.go();
    },
    setCurUser(response) {
      this.currentUser = response;
      if (Cookies.get('actAs')) {
        this.actAsId = Cookies.get('actAs');
        // Checks if user is admin of business at id actAs
        let check = false;
        for (let i = 0; i < response.businessesAdministered.length; i++) {
          if (response.businessesAdministered[i] === null) {
            return
          }
          if (String(response.businessesAdministered[i].id) === this.actAsId) {
            this.actAs = response.businessesAdministered[i].name;
            check = true;
            i = response.businessesAdministered.length; // Ends for loop
          }
        }
        // If user not admin of business removes cookie
        if (check === false) {
          Cookies.remove('actAs');
          this.actAsId = null;
          if (response.nickname == null) {
            this.actAs = response.firstName;
          } else {
            this.actAs = response.nickname;
          }
        }
      } else {
        if (response.nickname == null) {
          this.actAs = response.firstName;
        } else {
          this.actAs = response.nickname;
        }
      }
      this.actAs = this.omitName(this.actAs, this.maxNameLength)
      this.actAsOmit = this.omitName(this.actAs, this.omitPoint);

      // Filters out the null businesses
      this.businesses = response.businessesAdministered.filter(
          (business) => business !== null
      )

      this.refreshDropdown();
    },
    onResize() {
      this.toggleNavbar(true);
    }
  },
  beforeMount() {
    // If it is required to be logged in. The user will be checked.
    if (this.loginRequired) {
      this.ensureLoggedIn();
    }
    this.businessAccountId = Cookies.get("actAs");
    this.isActAsBusiness = (this.businessAccountId !== null && this.businessAccountId !== undefined);
    this.showOmitName = (this.screenWidth > 1200);
  },
  mounted() {
    this.getUserData();

    // Sample the navbar max height at mounting
    this.navbarMaxHeight = this.getNavbarMaxHeight();

    // Otherwise keep sampling until you get a valid result (i.e. not null)
    if (this.navbarMaxHeight == null) {
      while (this.navbarMaxHeight == null) this.navbarMaxHeight = this.getNavbarMaxHeight();
    }

    // Set the initial height for the navbar and the dropdown
    this.toggleDropdownAnimated('business-dropdown-links-side', 'business-dropdown-links-wrapper-side',
        this.showBusinessDropdown, true);
    this.toggleDropdownAnimated('interact-dropdown-links', 'interact-dropdown-links-wrapper',
        this.showInteractMenu, true);
    this.toggleNavbar(true)

    // Adding an event listener for resizing
    window.addEventListener("resize", this.onResize);

    // Watch window width
    const that = this
    window.onresize = () => {
      return (() => {
        window.screenWidth = document.body.clientWidth
        that.screenWidth = window.screenWidth
      })()
    }
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.onResize)
  },
  watch: {
    screenWidth(val) {

      // use timer to reduce page freezes
      if (!this.timer) {
        this.screenWidth = val
        this.timer = true
        let that = this
        setTimeout(function () {
          // change the display name
          if (that.screenWidth >= 1200) {
            that.showOmitName = true;
          } else {
            that.showOmitName = false;
          }
          that.timer = false
        }, 400)
      }
    }
  }

}
</script>

<style scoped>

/*Side nav styling*/

.sidenav {
  height: 100%;
  width: 0;
  position: fixed;
  z-index: 10000;
  top: 0;
  left: 0;
  background-color: #e7e5e5;
  overflow-x: hidden;
  transition: 0.5s;
  padding-top: 60px;
}

.sidenav a {
  padding: 8px 8px 8px 8px;
  text-decoration: none;
  font-size: 18px;
  /*color: #ffffff;*/
  display: block;
  transition: 0.3s;
}

/*.sidenav a:hover {*/
/*  color: #f1f1f1;*/
/*}*/

.sidenav .closebtn {
  position: absolute;
  top: 0;
  right: 25px;
  font-size: 36px;
  margin-left: 50px;
}

.closebtn {
  color: #545252;
}

@media screen and (max-height: 450px) {
  .sidenav {padding-top: 15px;}
  .sidenav a {font-size: 18px;}
}

/*!* Styling for smaller screen sizes begins *!*/

#interactDrop a {
  display: flex;
  flex-flow: row wrap;
  justify-content: center;
  align-items: center;
  padding: 10px 30px;
}

.nav-link {
  color: white;
  background: #19b092;
  margin: 4px 0;
  border-radius: 15px;
  text-align: center;
  font-size: large;
  width: auto;
  padding-right: 6px;
  padding-left: 6px;
}

.nav-link:hover, .nav-link:focus {
  background: #ef5e33;
  outline: none;
  cursor: pointer;
}

.navbar-toggler {
  color: rgba(25, 176, 146, 0.55);
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

#business-dropdown-links-wrapper-side, #interact-dropdown-links-wrapper {
  position: relative;
  overflow: hidden;
}

.active {
  background-color: #2eda77;
}

.dropdown-menu {
  position: static;
  border-right-width: 0;
  border-left-width: 0;
  /*padding: 0 5rem;*/
  background-color: transparent;
  /* margin: 1.2rem 0; Margins cannot be calculated in pixels :( */
}

.company-name-main-font {
  font-family: 'Merriweather Sans', sans-serif;

  /* centre text with navbar toggle */
  margin: 0;
  position: absolute;
  top: 35px;
  -ms-transform: translateY(-50%);
  transform: translateY(-50%);
}

.nav-item {
  min-width: 120px;
  list-style-type: none;
  padding: 0px 20px 4px;
}

.side-nav-link-icon {

}


</style>