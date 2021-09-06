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

          <router-link to="/home" tabindex="-1" style="justify-content: center; align-items: center; display: flex; text-decoration: none; color: black">
            <!-- Logo -->
            <img src="../../public/logo_only_med.png" alt="Logo" id="logo-image-nav">

            <!-- Brand name-->
            <div id="brand-name">
              REUSEABILITY
            </div>
          </router-link>
        </div>


        <!-- Contains the user icon, role, mid-screen notification and name. -->
        <div id="user-section">

          <div>
            <svg style="margin: 0 0.4em; cursor: pointer" xmlns="http://www.w3.org/2000/svg" width="28" height="28" fill="currentColor" class="bi bi-chat" viewBox="0 0 16 16" @click="toggleMessages" v-if="!isActAsBusiness">
              <path d="M2.678 11.894a1 1 0 0 1 .287.801 10.97 10.97 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8.06 8.06 0 0 0 8 14c3.996 0 7-2.807 7-6 0-3.192-3.004-6-7-6S1 4.808 1 8c0 1.468.617 2.83 1.678 3.894zm-.493 3.905a21.682 21.682 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a9.68 9.68 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9.06 9.06 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105z"/>
            </svg>

            <transition name="expand">
              <Messages v-if="showMessages"/>
            </transition>
          </div>

          <!-- Mid screen notification icon -->
          <div>
            <div
                type="button"
                @click="switchNotificationBox()">

              <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-bell-fill" viewBox="0 0 16 16" v-if="newNotification">
                <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zm.995-14.901a1 1 0 1 0-1.99 0A5.002 5.002 0 0 0 3 6c0 1.098-.5 6-2 7h14c-1.5-1-2-5.902-2-7 0-2.42-1.72-4.44-4.005-4.901z"/>
              </svg>

              <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-bell" viewBox="0 0 16 16" v-else>
                <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zM8 1.918l-.797.161A4.002 4.002 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4.002 4.002 0 0 0-3.203-3.92L8 1.917zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5.002 5.002 0 0 1 13 6c0 .88.32 4.2 1.22 6z"/>
              </svg>
            </div>
            <Notification v-if="openNotificationBox" />
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
      <transition name="links" >
        <ul id="links-list" v-if="showNavbar">
        <li class="a-nav-item" >
          <router-link to="/home" class="router-nav-link" active-class="active-link">
            <i class="side-nav-link-icon fas fa-home"></i>Home
          </router-link>
        </li>

        <li class="a-nav-item" v-if="actAsId">
          <router-link :to="'/businessProfile/' + actAsId" class="router-nav-link" exact-active-class="active-link">
            <i class="side-nav-link-icon fas fa-user-alt"></i>Profile
          </router-link>
        </li>

        <li class="a-nav-item" v-else>
          <router-link to="/profile" class="router-nav-link" active-class="active-link">
            <i class="side-nav-link-icon fas fa-user-alt"></i>Profile
          </router-link>
        </li>

        <li class="a-nav-item">
          <router-link to="/browseListings" class="router-nav-link" active-class="active-link">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-basket3-fill side-nav-link-icon" viewBox="0 0 16 16">
              <path d="M5.757 1.071a.5.5 0 0 1 .172.686L3.383 6h9.234L10.07 1.757a.5.5 0 1 1 .858-.514L13.783 6H15.5a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5H.5a.5.5 0 0 1-.5-.5v-1A.5.5 0 0 1 .5 6h1.717L5.07 1.243a.5.5 0 0 1 .686-.172zM2.468 15.426.943 9h14.114l-1.525 6.426a.75.75 0 0 1-.729.574H3.197a.75.75 0 0 1-.73-.574z"/>
            </svg>
            Browse Listings
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
          <transition name="expand" >
            <ul class="is-dropdown" v-if="showBusinessDropdown">
              <li class="a-nav-item">
                <router-link :to="'/businessProfile/' + businessAccountId + '/listings'" class="router-nav-link" active-class="active-link">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-cart-fill side-nav-link-icon" viewBox="0 0 16 16">
                    <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                  </svg>
                  Listings
                </router-link>
              </li>
              <li class="a-nav-item">
                <router-link :to="'/businessProfile/' + businessAccountId + '/inventory'" class="router-nav-link" active-class="active-link">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-archive-fill side-nav-link-icon" viewBox="0 0 16 16">
                    <path d="M12.643 15C13.979 15 15 13.845 15 12.5V5H1v7.5C1 13.845 2.021 15 3.357 15h9.286zM5.5 7h5a.5.5 0 0 1 0 1h-5a.5.5 0 0 1 0-1zM.8 1a.8.8 0 0 0-.8.8V3a.8.8 0 0 0 .8.8h14.4A.8.8 0 0 0 16 3V1.8a.8.8 0 0 0-.8-.8H.8z"/>
                  </svg>
                  Inventory
                </router-link>
              </li>
              <li class="a-nav-item">
                <router-link :to="'/businessProfile/' + businessAccountId + '/productCatalogue'" class="router-nav-link" active-class="active-link">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-phone-landscape-fill side-nav-link-icon" viewBox="0 0 16 16">
                    <path d="M2 12.5a2 2 0 0 1-2-2v-6a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v6a2 2 0 0 1-2 2H2zm11-6a1 1 0 1 0 0 2 1 1 0 0 0 0-2z"/>
                  </svg>
                  Catalogue
                </router-link>
              </li>
              <li class="a-nav-item">
                <router-link :to="'/businessProfile/' + businessAccountId + '/saleHistory'" class="router-nav-link" active-class="active-link">
                  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bar-chart-line-fill side-nav-link-icon" viewBox="0 0 16 16">
                    <path d="M11 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v12h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1v-3a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v3h1V7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v7h1V2z"/>
                  </svg>
                  Sale History
                </router-link>
              </li>
            </ul>
          </transition>
        </li>
      </ul>
      </transition>

      <!-- User information -->
      <transition name="expand">
        <ul v-if="showInteractMenu" id="interact-as-menu">


        <li class="a-nav-item mt-4 mb-0 mobile-only">
          <h6>User information:</h6>
          <hr class="mb-0">
        </li>

        <!-- Mobile user section -->
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

        <!-- Logout button -->
        <li class="a-nav-item" style="margin-top: 2rem">
          <button @click="logout" class="router-nav-link" id="logout-button">Logout</button>
        </li>

        <li class="a-nav-item mt-4 mb-0">
          <h6>Act As</h6>
          <hr class="mb-0">
        </li>

        <!-- Options for replacing who you are acting as (business or user) -->
        <li class="a-nav-item mt-0" v-for="(act, index) in showOmitName ? interactAsOmit : interactAs" :key="index" tabindex="-1"
            @click="() => itemClicked(index)">
          <h6 v-if="index===0"><br>User</h6>
          <div v-else-if="index===1">
            <h6>Businesses</h6>
          </div>
          <button class="router-nav-link" tabindex="0">{{ act.name }}</button>
        </li>
      </ul>
      </transition>

    </nav>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import Api from "../Api";
import {UserRole} from "../configs/User";
import Notification from "./main/Notification";
import Messages from "./messages/Messages";

export default {

  name: "NewNavbar",
  components: {
    Messages,
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
      maxNameLength: 15,
      omitPoint: 10,

      // notice for new notifications
      newNotification: false,
      openNotificationBox: false,

      // Admin rights
      role: null,

      // Messages
      showMessages: false,
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
      this.newNotification = false;
      this.openNotificationBox = !this.openNotificationBox;
      this.showInteractMenu = false;
      this.showMessages = false;
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
      if (Cookies.get('actAs') === undefined) {
        Api.getNotifications()
          .then(response => this.newNotification = (response.data.length > 0))
          .catch((error) => {
            if (error.response.status === 401) {
              // Missing or invalid token
              this.$router.push({path: '/invalidtoken'});
            } else {
              console.log(error)
            }
          });
      } else {
      Api.getBusinessNotifications(Cookies.get('actAs'))
          .then(response => this.newNotification = (response.data.length > 0))
          .catch((error) => {
            if (error.response.status === 401) {
              // Missing or invalid token
              this.$router.push({path: '/invalidtoken'});
            } else {
              console.log(error)
            }
          });
      }
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
     * Toggles the messages menu.
     */
    toggleMessages() {
      this.showMessages = !this.showMessages;
      this.openNotificationBox = false;
      this.showInteractMenu = false;
    },
    /**
     * Closes the interact as and its inner parts.
     */
    closeInteractMenu() {
      this.openNotificationBox = false;
      this.showInteractMenu = false;
      this.showMessages = false;
    },
    /**
     * Closes the navbar and its inner parts.
     */
    closeNavbar() {
      this.showBusinessDropdown = false;
      this.showMessages = false;
      this.showNavbar = false;
    },
    /**
     * Toggles the business dropdown
     */
    toggleBusinessDropdown() {
      this.showBusinessDropdown = !this.showBusinessDropdown;
    },
    /**
     * Toggles the navbar menu
     */
    toggleNavbar() {
      this.showBusinessDropdown = false;
      this.closeInteractMenu();
      this.showNavbar = !this.showNavbar;
    },
    /**
     * Toggles the interact as menu
     */
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
    z-index: 999;
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

  #brand-name {
    display: none;
  }

  #links-list {
    margin: 0;
    list-style: none;
    padding: 0;
    max-height: 100vh;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    transition: height ease-in-out 150ms;
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

    display: flex;
    justify-content: center;
    align-items: center;

    border-radius: 15px;
    margin: 0;
    padding: 0.5em;
  }

  .active-link {
    color: #fff;
    background-color: #2eda77;
    border: none;
    outline: none;
    box-shadow: none;
  }

  #logout-button {
    color: #fff;
    background-color: #ef5e33;
    border: 1px solid #ef5e33;
  }

  #logout-button:hover {
    color: #ef5e33;
    background-color: #fff;
    border: 1px solid #ef5e33;
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
    display: none;
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
    max-height: 100vh;
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

  .expand-enter-active {
    animation: dropAnimation 250ms ease-in-out;
    overflow: hidden;
  }

  .expand-leave-active {
    animation: dropAnimation 250ms reverse ease-in-out;
    overflow: hidden;
  }

  .links-enter-active {
    animation: dropAnimation 250ms ease-in-out;
    overflow: hidden;
  }

  .links-leave-active {
    animation: dropAnimation 250ms reverse ease-in-out;
    overflow: hidden;
  }

  @keyframes dropAnimation {
    0% {
      max-height: 0;
    }
    100% {
      max-height: 100vh;
    }
  }

  @media screen and (min-width: 350px){
    #logo-image-nav {
      display: block;
    }
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
      min-width: 16rem;
      box-shadow: -2px 10px 1rem #00000030;
    }

    #links-list .a-nav-item .router-nav-link {
      justify-content: left;
      height: fit-content;
      padding: 0.50em 1em;
    }

    .is-dropdown {
      max-width: 100%;
      height: 100%;
      align-items: flex-end;
      margin-left: 0;
    }
  }

  @media screen and (min-width: 900px){

    #brand-name {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 40px;
      font-family: 'Merriweather Sans', sans-serif;
      margin: 0;
      padding-left: 1rem;
    }

  }

</style>