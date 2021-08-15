<template>
  <div id="navbar-wrapper">
    <nav id="navbar">
      <div id="navbar-content">

        <div id="left-content">
          <!-- Hamburger icon (SVG) -->
          <div id="hamburger-icon-wrapper">
            <button id="hamburger-icon" @click="toggleNavbar">
              <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-list" viewBox="0 0 16 16">
                <path fill-rule="evenodd" d="M2.5 12a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5z"/>
              </svg>
            </button>
          </div>


          <!-- Logo -->
          <router-link to="/home" tabindex="-1" style="justify-content: center; align-items: center; display: flex">
            <img src="../../public/logo_only_med.png" alt="Logo" id="logo-image-nav">
          </router-link>
        </div>


        <!-- Contains the user icon, role, mid-screen notification and name. -->
        <div id="user-section">

          <!-- Mid screen notification icon -->
          <div
              type="button"
              @click="switchNotificationBox()">
            <img v-if="newNotification"
                 alt="notification"
                 src="../../public/notification.png"
                 height="43"
                 width="43"/>
            <img v-else type="button"
                 alt="notification"
                 src="../../public/notification_new.png"
                 height="43"
                 width="43"/>
            <Notification v-if="openNotificationBox" style="position: absolute; right: 25px; z-index: 999"/>
          </div>

          <!-- Act as section -->
          <div id="act-as-name">
            {{showOmitName ? actAsOmit : actAs}}
          </div>

          <button id="act-as-wrapper" @click="toggleInteractAs">
            <img src="../../public/profile_icon_default.png"
                 class="rounded-circle img-fluid" id="act-as-image" alt="Acting as image"/>
          </button>


          <!-- Role labels for admins -->
          <div id="user-section-admin-label">
            <!-- These messages will appear for GAA accounts -->
            <div class="admin-label" v-if="isGAA(role)">
              Admin (GAA)
            </div>
            <!-- These messages will appear for DGAA accounts -->
            <div class="admin-label" v-if="isDGAA(role)">
              Admin (DGAA)
            </div>
          </div>

        </div>
      </div>

      <!-- Links to other pages -->
      <ul id="links-list" v-if="showNavbar">
        <li class="a-nav-item" >
          <router-link to="/home" class="router-nav-link" active-class="active-link">
            <i class="side-nav-link-icon fas fa-home"></i>Home
          </router-link>
        </li>

        <li class="a-nav-item" v-if="actAsId">
          <router-link :to="'/businessProfile/' + actAsId" class="router-nav-link" active-class="active-link">
            <i class="side-nav-link-icon fas fa-user-alt"></i>Profile
          </router-link>
        </li>

        <li class="a-nav-item" v-else>
          <router-link to="/profile" class="router-nav-link" active-class="active-link">
            <i class="side-nav-link-icon fas fa-user-alt"></i>Profile
          </router-link>
        </li>

        <li class="a-nav-item" v-if="!isActAsBusiness">
          <router-link to="/marketplace" class="router-nav-link" active-class="active-link">
            <i class="side-nav-link-icon fas fa-store"></i>Marketplace
          </router-link>
        </li>

        <li class="a-nav-item" v-if="isActAsBusiness">
          <button class="router-nav-link add-dropdown-icon" @click="toggleBusinessDropdown">
            <i class="side-nav-link-icon fas fa-briefcase"></i>Business
          </button>
          <ul id="business-dropdown" class="is-dropdown" v-if="showBusinessDropdown">
            <li class="a-nav-item">
              <router-link :to="'/businessProfile/' + businessAccountId + '/listings'" class="router-nav-link" active-class="active-link">
                Listings
              </router-link>
            </li>
            <li class="a-nav-item">
              <router-link :to="'/businessProfile/' + businessAccountId + '/inventory'" class="router-nav-link" active-class="active-link">
                Inventory
              </router-link>
            </li>
            <li class="a-nav-item">
              <router-link :to="'/businessProfile/' + businessAccountId + '/productCatalogue'" class="router-nav-link" active-class="active-link">
                Catalogue
              </router-link>
            </li>
          </ul>
        </li>
      </ul>

      <!-- User information -->
      <ul v-if="showInteractMenu" id="interact-as-menu">


        <li class="a-nav-item mt-4 mb-0 mobile-only">
          <h6>User information:</h6>
          <hr class="mb-0">
        </li>

        <!-- Mobile user sectopm -->
        <li id="interact-as-mobile-user-section" class="a-nav-item mobile-only" style="float: contour; text-align: center; vertical-align:middle">
          <div id="act-as-small-size-user-section">
            <!-- Notification Icon -->
            <div>
              {{showOmitName ? actAsOmit : actAs}}
            </div>

            <!-- Role labels for admins -->
            <div>
              <!-- These messages will appear for GAA accounts -->
              <div class="admin-label" v-if="isGAA(role)">
                Admin (GAA)
              </div>
              <!-- These messages will appear for DGAA accounts -->
              <div class="admin-label" v-if="isDGAA(role)">
                Admin (DGAA)
              </div>
            </div>
          </div>
        </li>

        <li class="a-nav-item mt-4 mb-0">
          <h6>Actions:</h6>
          <hr class="mb-0">
        </li>

        <!-- Logout button -->
        <li class="a-nav-item" >
          <button @click="logout" class="router-nav-link">Logout</button>
        </li>

        <li class="a-nav-item mt-4 mb-0">
          <h6>Choose who to act as:</h6>
          <hr class="mb-0">
        </li>

        <!-- Options for replacing who you are acting as -->
        <li class="a-nav-item mt-0" v-for="(act, index) in showOmitName ? interactAsOmit : interactAs" :key="index" tabindex="-1"
            @click="itemClicked(index)">
          <h6 v-if="index===0"><br>User</h6>
          <div v-else-if="index===1">
            <hr>
            <h6>Businesses</h6>
          </div>
          <button class="router-nav-link" tabindex="0">{{ act.name }}</button>
        </li>
      </ul>

    </nav>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import Api from "../Api";
import {UserRole} from "@/configs/User";
import Notification from "./main/Notification";

export default {

  name: "NewNavbar",
  components: {
    Notification,
  },
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
      isActAsBusiness: false,
      businessAccountId: null,

      // omit part of name
      showOmitName: null,
      interactAsOmit: [],
      actAsOmit: "",
      maxNameLength: 30,
      omitPoint: 10,

      // notice for new notifications
      newNotification: false,
      openNotificationBox: false,

      // Admin rights
      role: null,
    }
  },
  methods: {    // ---------------------------------------- Admin Rights --------------------------------

    /** Given a role we test it against two of the possible admin roles. To determine if the role is of type admin.
     * @param role - A given role of some user.
     * @return {boolean} Returns true if the role is of type admin. Otherwise false.
     */
    hasAdminRights(role) {
      return role === UserRole.DEFAULTGLOBALAPPLICATIONADMIN || role === UserRole.GLOBALAPPLICATIONADMIN;
    },
    /**
     * Determines whether a role is DGAA or not
     * @param role - A given role.
     * @return {boolean} Returns true if you are a DGAA. Otherwise return false.
     */
    isDGAA(role) {
      return role === UserRole.DEFAULTGLOBALAPPLICATIONADMIN;
    },
    /**
     * Determines whether a role is GAA or not
     * @param role - A given role.
     * @return {boolean} Returns true if you are a GAA. Otherwise return false.
     */
    isGAA(role) {
      return role === UserRole.GLOBALAPPLICATIONADMIN;
    },
    /**
     * get role of given id
     */
    getLoginRole(id) {
      Api.getUser(id).then(response => (this.role = response.data.role))
    },

    // -------------------------------------------------------------------------------------
    /**
     * switch Notification Box
     */
    switchNotificationBox() {
      this.openNotificationBox = !this.openNotificationBox;
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
     * update image for bell
     */
    updateNotificationState() {
      Api.getNotifications()
          .then(response => this.newNotification = (response.data.length === 0))
          .catch((error) => {
            if (error.status === 401) {
              // Missing or invalid token
              this.$router.push({path: '/invalidtoken'});
            } else {
              console.log(error)
            }
          });
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
    closeInteractMenu() {
      this.openNotificationBox = false;
      this.showInteractMenu = false;
    },
    closeNavbar() {
      this.showNavbar = false;
      this.showBusinessDropdown = false;
    },
    toggleBusinessDropdown() {
      this.showBusinessDropdown = !this.showBusinessDropdown;
    },
    toggleNavbar() {
      this.showBusinessDropdown = false;
      this.closeInteractMenu();
      this.showNavbar = !this.showNavbar;
    },
    toggleInteractAs() {
      this.closeNavbar();
      this.openNotificationBox = false;
      this.showInteractMenu = !this.showInteractMenu;
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
    /**
     * Sets who the user is interacting as
     * @param index of dropdown clicked
     */
    itemClicked(index) {
      this.showInteractMenu = false;

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
    /**
     * T/F When user is acting as a business checks user is allowed on page
     * NOTE: Currently just Marketplace
     */
    canGoToPage() {
      return this.$route.name !== "Marketplace";
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

    // This is for using URL when acting as
    if (this.isActAsBusiness) {
      if (!this.canGoToPage()) {
        await this.$router.push({name: "BusinessProfile", params: {id: this.businessAccountId}})
      }
    }

    // update notifications
    this.updateNotificationState();
  },
  mounted() {
    const currentID = Cookies.get('userID');

    if(currentID) {
      this.getLoginRole(currentID);
    }

    this.getUserData();
  }
}

</script>

<style >

  #navbar-wrapper {
    height: 10rem;

  }

  #navbar {
    position: fixed;
    left: 0;
    top: 0;
    min-height: 7rem;
    width: 100vw;
    background-color: white;
    padding: 1.75rem 1.75rem;
    box-shadow: 0.4rem 0 1.75rem #00000030;
    display: flex;
    flex-direction: column;
  }

  #navbar-content {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  #left-content {
    display: flex;
    align-items: center;
  }

  #hamburger-icon-wrapper {
    min-height: 5rem;
    display: flex;
    align-items: center;
    border: none;
    outline: none;
    box-shadow: none;
    background-color: transparent;
    margin-right: 0.8rem;
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
    transition: width ease-in-out 150ms;
  }


  .a-nav-item {
    width: 100%;
    margin: 0.55rem 0;
  }

  .router-nav-link{
    color: #fff;
    background-color: #19b092;
    text-decoration: none;
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
    outline: transparent;
  }

  #act-as-wrapper:focus {
    outline: none;
    border: none;
    box-shadow: none;
  }

  #act-as-wrapper:focus-visible {
    outline: none;
    border: none;
    box-shadow: none;
  }

  #act-as-image {
    border: 1px solid #d3d3d3;
    max-height: 55px;
    height: 100%;
    width: auto;
  }

  #act-as-image:hover {
    outline: none;
    border: solid 1px #1eba8c;
    box-shadow: none;
  }

  #act-as-image:focus {
    outline: none;
    border: solid 1px #1eba8c;
    box-shadow: none;
  }

  #act-as-image:focus-visible {
    outline: none;
    border: solid 1px #1eba8c;
    box-shadow: none;
  }

  #user-section {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  #interact-as-menu {
    list-style: none;
    padding: 0;
  }

  .admin-label {
    background-color: #fd5050;
    color: white;
    border-radius: 6px;
    padding: 6px;
    max-width: 120px;
    margin:12px auto
  }

  #act-as-small-size-user-section {
    display: flex;
    justify-content: space-evenly;
    width: 100%;
    align-items: center;
  }

  #user-section-admin-label {
    display: none;
  }

  #act-as-name {
    display: none;
  }

  .side-nav-link-icon {
    margin-right: 0.25rem !important;
  }

  .mobile-only {
    cursor: unset;
  }

  @media screen and (min-width: 612px){
    #user-section-admin-label {
      display: block;
    }

    #act-as-name {
      display: block;
      margin-left: 0.5rem;
    }

    .mobile-only {
      display: none;
    }

    #act-as-wrapper {
      display: flex;
      align-items: center;
      flex-direction: row;
      width: auto;
    }

    #user-section {
      flex-direction: row;
      align-items: center;
    }

    #links-list {
      position: fixed;
      background-color: white;
      box-shadow: 2px 10px 1rem #00000030;
      padding: 1rem 3rem 0 1.75rem;

      height: 100vh;
      top: 8.5rem;
      left: 0;
    }

    #interact-as-menu {
      position: fixed;
      height: 100vh;
      background-color: white;
      padding: 0 2rem 2rem;
      top: 8.5rem;
      right: 0;
      max-width: fit-content;
      box-shadow: -2px 10px 1rem #00000030;
    }

    #links-list .a-nav-item .router-nav-link {
      justify-content: left;
      height: fit-content;
      padding: 0.50em 1em;
    }

    .is-dropdown {
      max-width: 100%;
      align-items: flex-end;
      margin-left: 0;
    }

    .is-dropdown .a-nav-item {
      max-width: 85%;
    }
  }

</style>