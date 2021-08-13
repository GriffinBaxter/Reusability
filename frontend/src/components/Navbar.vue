<template>
  <div id="navbar-wrapper">
    <nav id="navbar">
      <div id="hamburger-icon-wrapper">
        <div id="hamburger-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" fill="currentColor" class="bi bi-list" viewBox="0 0 16 16">
            <path fill-rule="evenodd" d="M2.5 12a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5z"/>
          </svg>
        </div>
      </div>
      <ul id="links-list">
        <li class="a-nav-item">
          <router-link to="/" class="router-nav-link">Home</router-link>
        </li>

        <li class="a-nav-item">
          <router-link to="/" class="router-nav-link">Profile</router-link>
          <ul class="is-dropdown">
            <li class="a-nav-item">
              <router-link to="/" class="router-nav-link">Marketplace</router-link>
            </li>
            <li class="a-nav-item">
              <router-link to="/" class="router-nav-link">Marketplace</router-link>
            </li>
            <li class="a-nav-item">
              <router-link to="/" class="router-nav-link">Marketplace</router-link>
            </li>
          </ul>
        </li>


        <li class="a-nav-item">
          <a class="router-nav-link">Business Pages</a>
        </li>

        <li class="a-nav-item">
          <router-link to="/" class="router-nav-link">Marketplace</router-link>
        </li>

        <li class="a-nav-item">
          <router-link to="/" class="router-nav-link">Logout</router-link>
        </li>

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
      actAsOmit: "",

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

<style scoped >

  #navbar-wrapper {
    height: 6rem;
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
  }

  #hamburger-icon-wrapper {
    min-height: 5rem;
    display: flex;
    align-items: center;
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

  #links-list {
    margin: 0;
    list-style: none;
    padding: 0;
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

    width: 100%;
    height: 100%;

    display: flex;
    justify-content: center;
    align-items: center;

    border-radius: 15px;
    margin: 0;
    padding: 0.5em;
  }

  .router-nav-link:hover {
    color: #fff;
    background-color: #ef5e33;
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


</style>