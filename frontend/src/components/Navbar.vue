<template>
  <div id="navbar-wrapper">
    <nav id="navbar">
      <div id="navbar-content">
        <div id="hamburger-icon-wrapper">
        <button id="hamburger-icon" @click="toggleNavbar">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" class="bi bi-list" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M2.5 12a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5z"/>
          </svg>
        </button>
      </div>
        <router-link to="/home" tabindex="-1" style="justify-content: center; align-items: center; display: flex">
          <img src="../../public/logo_only_med.png" alt="Logo" id="logo-image-nav">
        </router-link>
        <div>
          <button id="act-as-wrapper">
            <img src="../../public/profile_icon_default.png"
                 class="rounded-circle img-fluid" id="act-as-image" alt="Acting as image"/>
            <div id="act-as-name">
              {{actAsOmit}}
            </div>
          </button>
        </div>
      </div>
      <ul id="links-list" v-if="showNavbar">
        <li class="a-nav-item" >
          <router-link to="/home" class="router-nav-link" active-class="active-link">Home</router-link>
        </li>

        <li class="a-nav-item" v-if="actAsId">
          <router-link :to="'/businessProfile/' + actAsId" class="router-nav-link" active-class="active-link">Profile</router-link>
        </li>

        <li class="a-nav-item" v-else>
          <router-link to="/profile" class="router-nav-link" active-class="active-link">Profile</router-link>
        </li>

        <li class="a-nav-item" v-if="!isActAsBusiness">
          <router-link to="/marketplace" class="router-nav-link" active-class="active-link">Marketplace</router-link>
        </li>

        <li class="a-nav-item" v-if="isActAsBusiness">
          <button class="router-nav-link add-dropdown-icon" @click="toggleBusinessDropdown">Business Pages</button>
          <ul id="business-dropdown" class="is-dropdown" v-if="showBusinessDropdown">
            <li class="a-nav-item">
              <router-link :to="'/businessProfile/' + businessAccountId + '/listings'" class="router-nav-link" active-class="active-link">Listings</router-link>
            </li>
            <li class="a-nav-item">
              <router-link :to="'/businessProfile/' + businessAccountId + '/inventory'" class="router-nav-link" active-class="active-link">Inventory</router-link>
            </li>
            <li class="a-nav-item">
              <router-link :to="'/businessProfile/' + businessAccountId + '/productCatalogue'" class="router-nav-link" active-class="active-link">Catalogue</router-link>
            </li>
          </ul>
        </li>


        <li class="a-nav-item" >
          <button @click="logout" class="router-nav-link" active-class="active-link">Logout</button>
        </li>
      </ul>
      <ul>

      </ul>
    </nav>

  </div>
</template>

<script>
import Cookies from "js-cookie";
import Api from "../Api";

export default {

  name: "NewNavbar",
  props: {

    /** Used to determine if a login is required to access this part of the page.*/
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
      actAsOmit: "Johnny",

      // Watch window width
      screenWidth: document.body.clientWidth,
      maxNameLength: 30,
      omitPoint: 10
    }
  },
  methods: {
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
     *
     */
    toggleBusinessDropdown() {
      this.showBusinessDropdown = !this.showBusinessDropdown;
    },
    toggleNavbar() {
      if (this.showNavbar) {
        this.showBusinessDropdown = false;
      }
      this.showNavbar = !this.showNavbar;
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
  },
  async beforeMount() {
    if (this.loginRequired) {
      await this.ensureLoggedIn();
    }

    this.businessAccountId = Cookies.get("actAs");
    this.isActAsBusiness = (this.businessAccountId !== null && this.businessAccountId !== undefined);

  }
}

</script>

<style >

  #navbar-wrapper {
    height: 6rem;

  }

  @media (min-width: 921px) {
    #navbar-wrapper {
      display: none;
    }
  }

  #navbar {
    position: fixed;
    left: 0;
    top: 0;
    min-height: 5rem;
    width: 100vw;
    background-color: white;
    padding: 0.5rem 1.75rem;
    box-shadow: 0.4rem 0 1.75rem #00000030;
    display: flex;
    flex-direction: column;
  }

  #navbar-content {
    padding-top: 1rem;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  #hamburger-icon-wrapper {
    min-height: 5rem;
    display: flex;
    align-items: center;
    border: none;
    outline: none;
    box-shadow: none;
    background-color: transparent;
  }

  #hamburger-icon {
    width: fit-content;
    height: fit-content;
    display: flex;
    justify-content: center;
    align-items: center;

    border: #2eda77 solid 2px;
    color: #2eda77;
    background-color: transparent;
    cursor: pointer;

    transition: 150ms ease-in-out all;
    border-radius: 0.4rem;
  }

  #hamburger-icon:hover {
    border: #2eda77 solid 2px;
    color: white;
    background-color: #2eda77;
  }

  #hamburger-icon:focus {
    border: #2eda77 solid 2px;
    color: white;
    background-color: #2eda77;
  }

  #hamburger-icon:focus-visible {
    border: #2eda77 solid 2px;
    color: white;
    background-color: #2eda77;
  }

  #links-list {
    margin: 0;
    list-style: none;
    padding: 0;

    display: flex;
    flex-direction: column;
    overflow: hidden;
    transition: all 3000ms ease;
  }


  .a-nav-item {
    width: 100%;
    margin: 0.55rem 0;
    cursor: pointer;
  }

  .router-nav-link{
    color: #fff;
    background-color: #19b092;
    text-decoration: none;
    transition: 150ms ease-in-out all;
    border: transparent ;

    width: 100%;
    height: 100%;

    display: flex;
    justify-content: center;
    align-items: center;

    border-radius: 15px;
    margin: 0;
    padding: 0.5em;
  }

  .active-link {
    color: #fff;
    background-color: #ef5e33;
    border: none;
    outline: none;
    box-shadow: none;
  }

  .router-nav-link:hover {
    color: #fff;
    background-color: #ef5e33;
    border: none;
    outline: none;
    box-shadow: none;
  }

  .router-nav-link:focus {
    color: #fff;
    background-color: #ef5e33;
    border: none;
    outline: none;
    box-shadow: none;
  }

  .router-nav-link:focus-visible {
    color: #fff;
    background-color: #ef5e33;
    border: none;
    outline: none;
    box-shadow: none;
  }

  .add-dropdown-icon::after {
    display: inline-block;
    margin-left: 0.255em;
    vertical-align: 0.255em;
    content: "";
    border-top: .3em solid;
    border-right: .3em solid transparent;
    border-bottom: 0;
    border-left: .3em solid transparent;
  }

  .is-dropdown {
    list-style: none;
    border-top: black solid 1px;
    border-bottom: black solid 1px;

    padding: 0;
    margin: 0.55rem auto 0;
    max-width: 70%;

    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
  }

  #logo-image-nav {
    max-width: 90px;
    width: 100%;
  }

  #act-as-wrapper {
    background-color: transparent;
    border: none;

    height: 100%;
    width: 100%;
    cursor: pointer;
    transition: all 300ms ease-in-out;
    outline: transparent;
  }

  #act-as-image {
    border: 1px solid #d3d3d3;
    height: 55px;
    width: auto;
  }

  #act-as-wrapper:hover {
    outline: solid 1px #1eba8c;
    border: none;
    box-shadow: none;
  }

  #act-as-wrapper:focus {
    outline: solid 1px #1eba8c;
    border: none;
    box-shadow: none;
  }

  #act-as-wrapper:focus-visible {
    border: none;
    outline: solid 1px #1eba8c;
    box-shadow: none;
  }

</style>