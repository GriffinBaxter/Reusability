<!--This file creates the Profile page.-->
<!--It contains the container displaying the user's details.-->
<!--It current contains the navigation bar, container displaying the user's details, a user profile and nickname as well
    as a footer.-->
<!--Bootstrap has been used for creating and styling the elements.-->
<!--It is currently fully responsive.-->

<template>
  <div>
    <div id="main">
      <!--nav bar-->
      <Navbar></Navbar>

      <!--profile container-->
      <div class="container p-5 mt-3 all-but-footer text-font" id="profile-container">

        <!--profile header, contains user search bar-->
        <ProfileHeader id="profile-header"/>

        <div class="row">

          <!-- Update images modal -->
          <UpdateImagesModal ref="updateImagesModal" location="User" :id="userId" v-model="user"/>

          <div class="col-xl-3 mb-3">
            <div class="card text-center shadow-sm">
              <div class="card-body">

                <!--user's profile image-->
                <div id="imageDiv">
                  <img class="rounded-circle img-fluid" :src="getPrimaryImageSrc(user.data.images)"
                       alt="Profile Image"/>
                  <div id="change-profile-picture-button" style="padding-top: 10px" v-if="!otherUser">
                    <button type="button" style="width: 252px; max-width: 100%"
                            class="btn btn-md btn-outline-primary green-button" @click="(event) => {
                      this.$refs.updateImagesModal.showModel(event);
                    }">
                      Change Profile Picture
                    </button>
                  </div>
                </div>

                <!--user's nickname and bio-->
                <div class="mt-3">
                  <hr>
                  <h4 v-if="nickname.length !== 0">{{ nickname }}</h4>
                  <h4 v-else>{{ firstName }}</h4>
                  <div class="text-secondary">{{ bio }}</div>
                  <div id="edit-profile" style="padding-top: 10px" v-if="!otherUser">
                    <hr>
                    <button type="button" style="width: 252px; max-width: 100%"
                            class="btn btn-md btn-outline-primary green-button" @click="goToEdit()">
                      Edit Profile
                    </button>
                  </div>
                </div>

              </div>
            </div>

            <div v-if="actionErrorMessage" class="card text-white bg-danger shadow-sm mt-3">
              <div class="card-header">Something went wrong with your action...</div>
              <div class="card-body">{{ actionErrorMessage }}</div>
            </div>

            <div class="card text-center shadow-sm mt-3" v-if="populatedBox()">
              <!-- These messages will appear for GAA accounts -->
              <div class="card-body" v-if="hasAdminRights(role) && isGAA(role)">
                <div class="alert alert-info" role="alert">
                  Global Application Admin
                </div>
              </div>

              <!-- These messages will appear for DGAA accounts -->
              <div class="card-body" v-if="hasAdminRights(role) && isDGAA(role)">
                <div class="alert alert-info" role="alert">
                  Default Global Application Admin
                </div>
              </div>

              <!--make/remove business administrator button-->
              <div class="card-body" v-if="actingBusinessId && otherUser">
                <div v-if="!isBusinessAdministrator">
                  <div class="spinner-border spinner-border-sm text-primary" v-if="loadingAction"></div>
                  <button type="button" class="btn btn-md btn-outline-primary" v-else @click="activeAsAdministrator()">
                    Grant Business Administrator Status
                  </button>
                </div>

                <div v-else>
                  <div class="spinner-border spinner-border-sm text-warning" v-if="loadingAction"></div>
                  <button type="button" class="btn btn-md btn-outline-warning" v-else
                          @click="removeActiveAdministrator()">
                    Revoke Business Administrator Status
                  </button>
                </div>
              </div>

              <!-- This only works under the assumption that only the DGAA can see the roles of others. Otherwise this will break. This is
              because then isValidRole(role) will return true, which means that these buttons will appear on other users profile pages
              but the backend will prevent this from occurring.

              The error can currently be shown on your own profile if you are a GAA. This is done by changing your userID cookie to
              another user's id.
              -->
              <div class="card-body" v-if="isValidRole(role) && otherUser && !isDGAA(role)">
                <!-- If the current (page) user has admin rights. Then show the revoke message. Otherwise show the grant message.-->
                <div v-if="isGAA(role)">
                  <div class="spinner-border spinner-border-sm text-danger" v-if="loadingAction"></div>
                  <button type="button" class="btn btn-md btn-outline-danger" v-else @click="revokeUserGAA">
                    Revoke Global Application Admin
                  </button>
                </div>

                <div v-else>
                  <div class="spinner-border spinner-border-sm text-success" v-if="loadingAction"></div>
                  <button type="button" class="btn btn-md btn-outline-success" v-else @click="grantUserGAA">
                    Grant Global Application Admin
                  </button>
                </div>
              </div>

              <!--register business button-->
              <div class="card-body" v-if="!otherUser">
                <div id="registerBusinessRow" v-if="!otherUser">
                  <button type="button" style="width: 252px; max-width: 100%"
                          class="btn btn-md btn-outline-primary green-button"
                          @click="$router.push('/businessRegistration')">
                    Register Business
                  </button>
                </div>
              </div>
            </div>

          </div>

          <div class="col">
            <div class="card shadow-sm">
              <div class="card-body">

                <!--user's name-->
                <div class="container">
                  <div class="row justify-content-between">
                    <div class="col-4 -align-left">
                      <h6>Name:</h6>
                    </div>
                    <div class="col-8">
                      <div class="text-secondary" style="text-align: right">
                        {{ firstName }} {{ middleName }} {{ lastName }}
                      </div>
                    </div>
                  </div>
                </div>

                <!--user's email-->
                <hr>
                <div class="container">
                  <div class="row justify-content-between">
                    <div class="col-md-3">
                      <h6>Email:</h6>
                    </div>
                    <div class="col">
                      <div class="text-secondary" style="text-align: right">
                        {{ email }}
                      </div>
                    </div>
                  </div>
                </div>

                <!--user's date of birth-->
                <hr v-if="!otherUser || isDGAA(loginRole)">
                <div class="container" v-if="!otherUser || isDGAA(loginRole)">
                  <div class="row justify-content-between">
                    <div class="col-md-3">
                      <h6>Date of Birth:</h6>
                    </div>
                    <div class="col">
                      <div class="text-secondary" style="text-align: right">
                        {{ dateOfBirth }}
                      </div>
                    </div>
                  </div>
                </div>

                <!--user's phone number-->
                <hr v-if="!otherUser || isDGAA(loginRole)">
                <div class="container" v-if="!otherUser || isDGAA(loginRole)">

                  <div class="row justify-content-between">
                    <div class="col-md-3">
                      <h6>Phone number:</h6>
                    </div>
                    <div class="col">
                      <div class="text-secondary" style="text-align: right">
                        {{ phoneNumber }}
                      </div>
                    </div>
                  </div>
                </div>

                <!--user's home address-->
                <hr>
                <div class="container">
                  <div class="row justify-content-between">
                    <div class="col-md-3">
                      <h6>Address:</h6>
                    </div>
                    <div class="col">
                      <div class="text-secondary" v-for="lines in address" :key="lines.line" style="text-align: right">
                        {{ lines.line }}
                      </div>
                    </div>
                  </div>
                </div>

                <!--user's joined date-->
                <hr>
                <div class="container">
                  <div class="row justify-content-between">
                    <div class="col-md-3">
                      <h6>Joined:</h6>
                    </div>
                    <div class="col">
                      <div class="text-secondary" style="text-align: right">
                        {{ joined }}
                      </div>
                    </div>
                  </div>
                </div>
                <!--user's businesses administered-->
                <hr v-if="businessesAdministeredExist()">
                <div class="container" v-if="businessesAdministeredExist()">
                  <div class="row justify-content-between">
                    <div class="col-md-3">
                      <h6>Businesses Administered:</h6>
                    </div>
                    <div class="col">
                      <div class="spinner-border spinner-border-sm text-dark" v-if="loadingAction"></div>
                      <div v-else>
                        <div class="text-secondary businesses-administered" v-for="business in businessesAdministered"
                             :key="business.name"
                             style="text-align: right" @click="redirectToBusiness(business.id)">
                          {{ business.name }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!--user's cards-->
            <UserCardsComp class="mt-3" :users-cards="usersCards" :other-user="otherUser"/>
          </div>
        </div>
      </div>
    </div>
    <!--footer-->
    <Footer></Footer>
  </div>
</template>

<script>

import ProfileHeader from "../components/ProfileHeader";
import Api from '../Api';
import Cookies from 'js-cookie';
import Footer from "../components/main/Footer";
import Navbar from "../components/Navbar";
import {UserRole} from '../configs/User'
import {getFormattedAddress, checkNullity} from "../views/helpFunction"
import UserCardsComp from "../components/UserCardsComp";
import UpdateImagesModal from "../components/UpdateImagesModal";

export default {
  name: "Profile",
  components: {
    UserCardsComp,
    Footer,
    ProfileHeader,
    Navbar,
    UpdateImagesModal
  },

  data() {
    return {
      user: {
        data: {
          firstName: "",
          images: [],
          id: 0
        }
      },

      userId: 0,
      actionErrorMessage: "",
      loadingAction: false,
      urlID: null,
      currentID: null,
      firstName: "",
      lastName: "",
      middleName: "",
      nickname: "",
      bio: "",
      email: "",
      dateOfBirth: "",
      phoneNumber: "",

      address: [],
      streetNumber: "",
      streetName: "",
      city: "",
      postcode: "",
      region: "",
      country: "",

      created: "",
      joined: "",
      businessesAdministered: [],
      otherUser: false,
      role: null,

      loginRole: null,
      isBusinessAdministrator: false,
      actingBusinessId: null,

      // User card variables
      usersCards: [],

      images: []
    }
  },
  methods: {

    // ---------------------------------------- These functions probably belong in User.js But then they can't easily be used with the profile --------------------------
    /**
     * Determines if the role is of a valid type (e.g. not null, some other invalid string, etc).
     * @param role - Some role.
     * @return {boolean} Returns true if the role of the user is of the expected possible roles. Otherwise false.
     */
    isValidRole(role) {
      return role in UserRole;
    },
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
     * If a user is an administrator of one or more businesses they will need to be displayed.
     */
    businessesAdministeredExist() {
      return this.businessesAdministered.length !== 0;
    },

    // --------------------------------------------------------------------------------------------------------------------

    getPrimaryImageSrc(images) {
      if (images.length > 0) {
        for (let image of images) {
          if (image.isPrimary) {
            return Api.getServerURL() + "/" + image.thumbnailFilename;
          }
        }
      }
      return require('../../public/default-image.jpg')
    },

    /**
     * Calculates the months between the given date and the current date, then formats the given date and months.
     * Finally it sets the join date on the page to the formatted string.
     * @param createdDate
     */
    getCreatedDate(createdDate) {
      const dateJoined = new Date(createdDate);

      const currentDate = new Date();
      let months = (currentDate.getFullYear() - dateJoined.getFullYear()) * 12
          + (currentDate.getMonth() - dateJoined.getMonth());

      // getDate instead of getDay is important
      // "The value returned by getDay
      // is an integer corresponding to the day of the week:
      // 0 for Sunday, 1 for Monday, 2 for Tuesday, and so on."
      if (currentDate.getDate() < dateJoined.getDate()) {
        months -= 1;
      }

      const finalDate = this.formatAge(createdDate);
      this.joined = `${finalDate} (${months} months ago)`;
    },

    /**
     * Performs the action that grants GAA to the (page) user and handles all errors
     * specified in the API spec.
     */
    async grantUserGAA() {

      // If the process is already running return.
      if (this.loadingAction) return;

      if (this.urlID == null) {
        this.actionErrorMessage = "Sorry, but something went wrong..."
        return
      }

      this.loadingAction = true;

      await Api.makeAdmin(this.urlID).then(
          data => {
            if (data.status === 200) {
              // successful grant of admin rights!
              this.role = UserRole.GLOBALAPPLICATIONADMIN
            } else {
              this.actionErrorMessage = "Sorry, but something went wrong..."
            }
          }
      ).catch(error => {
        if (error.response) {
          // Code is not 2xx
          if (error.response.status === 401) {
            // Missing or invalid token
            this.$router.push({path: '/invalidtoken'});
          }

          if (error.response.status === 403) {
            // Lacks permissions
            this.actionErrorMessage = "Sorry, but you lack permissions to perform this action."
          }

          if (error.response.status === 406) {
            // Something is wrong with the requested route (not a 404).
            this.actionErrorMessage = "Sorry, but something went wrong..."
          }

        } else if (error.request) {
          // No response received. Timeout occurs
          this.$router.push({path: '/timeout'});
        } else {
          // Something went wrong with the request setup...
          this.actionErrorMessage = "Sorry, but something went wrong..."
        }
      })
      this.loadingAction = false;
    },

    /**
     * Performs the action that revokes GAA from the (page) user and handles all errors
     * specified in the API spec.
     */
    async revokeUserGAA() {

      // If the process is already running return.
      if (this.loadingAction) return;

      if (this.urlID == null) {
        this.actionErrorMessage = "Sorry, but something went wrong..."
        return
      }

      this.loadingAction = true;

      await Api.revokeAdmin(this.urlID).then(
          data => {
            if (data.status === 200) {
              // successful revoke of admin rights!
              this.role = UserRole.USER
            } else {
              this.actionErrorMessage = "Sorry, but something went wrong..."
            }
          }
      ).catch(error => {

        if (error.response) {
          // Code is not 2xx
          if (error.response.status === 401) {
            // Missing or invalid token
            this.$router.push({path: '/invalidtoken'});
          }

          if (error.response.status === 403) {
            // Lacks permissions
            this.actionErrorMessage = "Sorry, but you lack permissions to perform this action."
          }

          if (error.response.status === 406) {
            // Something is wrong with the requested route (not a 404).
            this.actionErrorMessage = "Sorry, but something went wrong..."
          }

          if (error.response.status === 409) {
            // DGAA attempting to remove his admin status
            this.actionErrorMessage = "Sorry, but as DGAA you cannot remove your admin status."
          }

        } else if (error.request) {
          // No response received. Timeout occurs
          this.$router.push({path: '/timeout'});
        } else {
          // Something went wrong with the request setup...
          this.actionErrorMessage = "Sorry, but something went wrong..."
        }
      })
      this.loadingAction = false;
    },

    /**
     * Sends a get request to the backend, calling populatePage upon success with the returned data.
     * If the request was unsuccessful, the page is not populated and appropriate error messages logged.
     * @param userID
     */
    retrieveUser(userID) {
      Api.getUser(userID).then(response => (this.populatePage(response.data))).catch((error) => this.processUserInfoError(error));
    },

    /**
     * Formats the given age string using a Date object and removes the day from the result.
     * Returns a formatted string.
     * @param ageString
     * @returns {string}
     */
    formatAge(ageString) {
      let array = (new Date(ageString)).toDateString().split(" ");
      array.shift();
      return array.join(' ')
    },

    /**
     * Populates all display fields on the profile page with the given data.
     The address is a special case as its components are stored semi-colon separated,
     so it must be 'unpacked' and formatted.
     */
    populatePage(data) {
      /*
      Populates all display fields on the profile page with the given data.
      The address is a special case as its components are stored semi-colon separated,
      so it must be 'unpacked' and formatted.
       */
      //basic unpack
      this.userId = data.id;
      this.dateOfBirth = this.formatAge(data.dateOfBirth);
      this.phoneNumber = data.phoneNumber;

      //address unpack
      this.streetNumber = checkNullity(data.homeAddress.streetNumber);
      this.streetName = checkNullity(data.homeAddress.streetName);
      this.suburb = checkNullity(data.homeAddress.suburb);
      this.city = checkNullity(data.homeAddress.city);
      this.region = checkNullity(data.homeAddress.region);
      this.country = checkNullity(data.homeAddress.country);
      this.postcode = checkNullity(data.homeAddress.postcode);

      this.address = getFormattedAddress(this.streetNumber, this.streetName, this.suburb, this.city, this.postcode, this.region, this.country);

      // businesses administered unpack
      this.actingBusinessId = Cookies.get("actAs");
      data.businessesAdministered.forEach(business => {
        if (business !== null) {
          if (business.id.toString() === this.actingBusinessId) {
            this.isBusinessAdministrator = true;
          }
          this.businessesAdministered.push({name: business.name, id: business.id});
        }
      })

      //basic unpack
      this.firstName = data.firstName;
      this.middleName = data.middleName;
      this.lastName = data.lastName;
      this.nickname = data.nickname;
      this.bio = data.bio;
      this.email = data.email;

      if (data.role) {
        this.role = data.role;
      }
      if (data.images) {
        this.images = data.images
      }

      this.user = {
        data: {
          firstName: this.firstName,
          id: this.id,
          images: this.images
        }
      }
      this.getCreatedDate(data.created);
    },

    /**
     * Redirect the user to a business profile page.
     */
    redirectToBusiness(id) {
      this.$router.push({name: 'BusinessProfile', params: {id}});
    },

    /**
     * get role of given id
     */
    getLoginRole(id) {
      Api.getUser(id).then(response => (this.loginRole = response.data.role))
    },

    /**
     * Logs the user out of the site by deleting the relevant cookies and redirecting to the login page.
     */
    logout() {
      Cookies.remove('userID', {sameSite: 'strict'});
      Cookies.remove('actAs', {sameSite: 'strict'});
      Api.signOut().then(() => {
        this.$router.push({name: 'Login'})
      })
    },

    /**
     * make select user become one of administrator of current active business
     */
    async activeAsAdministrator() {
      // If the process is already running return.
      if (this.loadingAction) return;

      if (this.urlID == null) {
        this.actionErrorMessage = "Sorry, but something went wrong..."
        return
      }

      if (this.otherUser) {
        this.loadingAction = true;
        let success = true;
        await Api.makeAdministrator(Cookies.get("actAs"), this.urlID).then(response => {
              if (response.status !== 200) {
                this.actionErrorMessage = "Sorry, but something went wrong..."
              }
            }
        ).catch(error => success = this.processUpdateAdministratorError(error));
        //add the business if makeAdministrator successful
        if (success) {
          Api.getBusiness(this.actingBusinessId).then(response => {
            this.businessesAdministered.push({name: response.data.name, id: response.data.id});
          })
          this.actionErrorMessage = "" // resets error message
          this.isBusinessAdministrator = true;
        }
        this.loadingAction = false;
      }
    },

    /**
     * remove select user from administrators of current active business
     */
    async removeActiveAdministrator() {
      // If the process is already running return.
      if (this.loadingAction) return;

      if (this.urlID == null) {
        this.actionErrorMessage = "Sorry, but something went wrong..."
        return
      }

      if (this.otherUser) {
        this.loadingAction = true;
        let success = true;

        await Api.removeAdministrator(Cookies.get("actAs"), this.urlID).then(response => {
              if (response.status !== 200) {
                this.actionErrorMessage = "Sorry, but something went wrong..."
                success = false
              }
            }
        ).catch(error => success = this.processUpdateAdministratorError(error));
        //pop the business which has been removed if successful
        if (success) {
          this.actionErrorMessage = "" // resets error message
          let index = 0;
          this.businessesAdministered.forEach(business => {
            if (business.id.toString() === this.actingBusinessId) {
              this.businessesAdministered.splice(index);
            }
            index++
          })
          this.isBusinessAdministrator = false;
        }
        this.loadingAction = false;
      }
    },
    /**
     * If a user goes to update whether another user is an administrator of their business or not and the "request" fails
     * then the appropriate error message will need to be displayed.
     * @param error an error, which includes an error message, and error status code (such as 401).
     * @return success a boolean value which is always false since an error occurred.
     */
    processUpdateAdministratorError(error) {
      let success = false;
      if (error.response) {
        // Code is not 2xx
        if (error.response.status === 401) {
          // Missing or invalid token
          this.$router.push({path: '/invalidtoken'});
        }

        if (error.response.status === 403) {
          // Lacks permissions
          this.actionErrorMessage = "Sorry, but you lack permissions to perform this action."
        }

        if (error.response.status === 406) {
          // Something is wrong with the requested route (not a 404).
          this.actionErrorMessage = "Sorry, but something went wrong..."
        }

      } else if (error.request) {
        // No response received. Timeout occurs
        this.$router.push({path: '/timeout'});
      } else {
        // Something went wrong with the request setup...
        this.actionErrorMessage = "Sorry, but something went wrong..."
      }
      return success;
    },
    /**
     * Sends a get request to the backend to retrieve the cards belonging to the current user's profile you are viewing.
     * If the request was unsuccessful, the cards are not populated on the page and appropriate error messages logged.
     * @param userId the id of the user's profile you are viewing.
     */
    retrieveUsersCards(userId) {
      Api.getUsersCards(userId).then(response => {
        this.usersCards = response.data;
        this.usersCards.sort(this.compareCards);
      }).catch((error) => this.processUserInfoError(error));
    },
    /**
     * This method is used to compare two cards' sections when sorting a user's cards by section.
     * @param card1 the user's first card used for comparison.
     * @param card2 the user's second card used for comparison.
     *
     * Preconditions:  card1 is a non-null JSON representing a MarketplaceCard.
     *                 card2 is a non-null JSON representing a MarketplaceCard.
     * Postconditions: an integer value representing the comparison outcome:
     *                 1. -1 if the section of card1 is "less" than the section of card2.
     *                 2. 1 if the section of card1 is "greater" than the section of card2.
     *                 3. 0 if the card sections are equal.
     */
    compareCards(card1, card2) {
      if (card1.section < card2.section) {
        return -1;
      }
      if (card1.section > card2.section) {
        return 1;
      }
      return 0;
    },
    /**
     * If a request is made to the backend for user info (profile information or cards) and an error occurs
     * then the appropriate error message will need to be displayed.
     * @param error an error which includes an error message.
     */
    processUserInfoError(error) {
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
    },
    /**
     * This method checks to see if there is anything within the inner html class (box). If there is then the class will
     * be displayed, otherwise it is hidden.
     */
    populatedBox() {
      return (this.hasAdminRights(this.role) && this.isGAA(this.role)) ||
          (this.hasAdminRights(this.role) && this.isDGAA(this.role)) ||
          (this.actingBusinessId && this.otherUser) ||
          (this.isValidRole(this.role) && this.otherUser && !this.isDGAA(this.role)) ||
          (!this.otherUser);
    },
    /**
     * Takes the user to the edit profile page
     */
    goToEdit() {
      let id
      if (this.urlID === "profile") {
        id = this.currentID
      } else {
        id = this.urlID
      }
      this.$router.push({name: "EditProfile", params: {id}})
    }
  },

  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  mounted() {

    this.currentID = Cookies.get('userID');

    if (this.currentID) {
      this.getLoginRole(this.currentID);

      const url = document.URL
      this.urlID = url.substring(url.lastIndexOf('/') + 1);

      if (this.currentID === this.urlID || this.urlID === 'profile') {
        this.retrieveUser(this.currentID);
        this.retrieveUsersCards(this.currentID);
      } else {
        // Another user
        this.retrieveUser(this.urlID);
        this.retrieveUsersCards(this.urlID);
        this.otherUser = true;
      }
    }
  }
}
</script>

<!----------------------------------------------- Profile Page Styling ------------------------------------------------>

<style scoped>

#profile-container {
  margin-bottom: 5%;
}

#imageDiv {
  width: 100%;
  padding: 2px;
}

.businesses-administered:hover {
  color: #1EBA8C !important;
  cursor: pointer;
}

/* Needed because bootstrap alert had "padding" causing the message
   to not be centered. */
.alert {
  margin-bottom: 0;
}

#user-cards {
  padding-top: 2%;
}

</style>
