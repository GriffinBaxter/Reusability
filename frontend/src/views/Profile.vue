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

    <!--profile header, contains user search bar-->
    <div id="profile-header-div">
      <ProfileHeader/>
    </div>

    <!--profile container-->
    <div class="container p-5 mt-3 all-but-footer text-font" id="profile-container">

      <!-- These messages will appear for GAA accounts -->
      <div class="row" v-if="hasAdminRights(role) && isGAA(role)">
        <div class="col-xl-12 mb-5 text-center mx-auto">
          <div class="display-5" v-if="otherUser">This user has application admin rights!</div>
          <div class="display-5" v-else>You have application admin rights!</div>
        </div>
      </div>

      <!-- These messages will appear for DGAA accounts -->
      <div class="row" v-if="hasAdminRights(role) && isDGAA(role)">
        <div class="col-xl-12 mb-5 text-center mx-auto">
          <div class="display-5" v-if="otherUser">This user has default application admin rights!</div>
          <div class="display-5" v-else>You have default application admin rights!</div>
        </div>
      </div>

      <div class="row">

        <div class="col-xl-3 mb-3">
          <div class="card text-center shadow-sm">
            <div class="card-body">

              <!--user's profile image-->
              <div id="imageDiv">
                <img class="rounded-circle img-fluid" :src="require('/public/sample_profile_image.jpg')" alt="Profile Image"/>
              </div>

              <!--      Note that this is commented out as the image storage story has not been implemented        -->
              <!-- Button trigger modal -->
<!--              <button type="button" class="btn green-button" @click="showFileUpload(true)" id="upload-button" v-if="!otherUser">-->
<!--                Upload Image-->
<!--              </button>-->

              <!--user's nickname and bio-->
              <div class="mt-3">
                <h4>{{ nickname }}</h4>
                <div class="text-secondary">{{ bio }}</div>
              </div>

            </div>
          </div>

          <!--   For later use:   -->
<!--          <div class="card text-center shadow-sm mt-3">-->
<!--            <div class="card-body">-->
<!--              <button class="btn btn-lg text-secondary" id="edit-profile-button">Edit Profile</button>-->
<!--            </div>-->
<!--          </div>-->

          <div v-if="actionErrorMessage" class="card text-white bg-danger shadow-sm mt-3">
            <div class="card-header">Something went wrong with your action...</div>
            <div class="card-body">{{ actionErrorMessage }}</div>
          </div>

          <!--make/remove business administrator button-->
          <div class="card text-center  shadow-sm mt-3" v-if="actingBusinessId && otherUser">
            <div class="card-body">
              <div v-if="!isBusinessAdministrator">
                <div class="spinner-border spinner-border-sm text-primary" v-if="loadingAction"></div>
                <button type="button" class="btn btn-md btn-outline-primary" v-else @click="activeAsAdministrator()">
                  Grant Business Administrator Status
                </button>
              </div>

              <div v-else>
                <div class="spinner-border spinner-border-sm text-warning" v-if="loadingAction"></div>
                <button type="button" class="btn btn-md btn-outline-warning" v-else @click="removeActiveAdministrator()">
                  Revoke Business Administrator Status
                </button>
              </div>
            </div>
          </div>
          <!--
          This only works under the assumption that only the DGAA can see the roles of others. Otherwise this will break. This is
          because then isValidRole(role) will return true, which means that these buttons will appear on other users profile pages
          but the backend will prevent this from occuring.

          The error can currently be shown on your own profile if you are a GAA. This is done by changing your userID cookie to
          another user's id.
          -->
          <div class="card text-center shadow-sm mt-3" v-if="isValidRole(role) && otherUser && !isDGAA(role)">
            <div class="card-body">
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
                    <div class="text-secondary" align="right">
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
                    <div class="text-secondary" align="right">
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
                    <div class="text-secondary" align="right">
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
                    <div class="text-secondary" align="right">
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
                    <div class="text-secondary" v-for="lines in address" :key="lines.line" align="right">
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
                    <div class="text-secondary" align="right">
                      {{ joined }}
                    </div>
                  </div>
                </div>
              </div>
              <hr v-if="businessesAdministeredExist()">
              <div class="container" v-if="businessesAdministeredExist()">
                <div class="row justify-content-between">
                  <div class="col-md-3">
                    <h6>Businesses Administered:</h6>
                  </div>
                  <div class="col">
                    <div class="spinner-border spinner-border-sm text-dark" v-if="loadingAction"></div>
                    <div v-else>
                      <div class="text-secondary businesses-administered" v-for="business in businessesAdministered" :key="business.name"
                           align="right" @click="pushToBusiness(business.id)">
                        {{ business.name }}
                      </div>
                    </div>

                  </div>
                </div>
              </div>

            </div>
          </div>

          <!--register business button-->
          <div align="right" id="registerBusinessRow" v-if="!otherUser">
          <button class="btn btn-outline-primary float-end mt-4 green-button" @click="$router.push('/businessRegistration')">Register Business</button>
          </div>

        </div>
      </div>
    </div>
    <!-- File Upload -->
    <div v-if="showUpload" id="FileUpload" class="modal">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Upload Image</h5>
          <button type="button" class="btn-close" @click="showFileUpload(false)"></button>
        </div>
        <div class="modal-body">
          <input type="file">
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="showFileUpload(false)">Close</button>
          <button type="button" class="btn green-button" @click="showFileUpload(false)">Save changes</button>
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
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import {UserRole} from '../configs/User'

export default {
  name: "Profile",
  components: {
    Footer,
    ProfileHeader,
    Navbar
  },

  data() {
    return {
      actionErrorMessage: "",
      loadingAction: false,
      urlID: null,
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
      showUpload: false
    }
  },
  methods: {

    /**
     * Calculates the months between the given date and the current date, then formats the given date and months.
     * Finally it sets the join date on the page to the formatted string.
     * @param createdDate
     */
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
    businessesAdministeredExist() {
      return this.businessesAdministered.length !== 0;
    },

    // --------------------------------------------------------------------------------------------------------------------

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
      Api.getUser(userID).then(response => (this.populatePage(response.data))).catch((error) => {

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
      this.dateOfBirth = this.formatAge(data.dateOfBirth);
      this.phoneNumber = data.phoneNumber;

      //address unpack
      if (data.homeAddress.streetNumber) {
        this.streetNumber = data.homeAddress.streetNumber;
      }
      if (data.homeAddress.streetName) {
        this.streetName = data.homeAddress.streetName;
      }
      if (data.homeAddress.city) {
        this.city = data.homeAddress.city;
      }
      if (data.homeAddress.region) {
        this.region = data.homeAddress.region;
      }
      if (data.homeAddress.country) {
        this.country = data.homeAddress.country;
      }
      if (data.homeAddress.postcode) {
        this.postcode = data.homeAddress.postcode;
      }

      if (this.streetNumber !== "" && this.streetName !== "") {
        this.address.push({line: this.streetNumber + " " + this.streetName});
      } else {
        this.address.push({line: this.streetNumber + this.streetName});
      }
      if (this.city !== "" && this.postcode !== "") {
        this.address.push({line: this.city + ", " + this.postcode});
      } else {
        this.address.push({line: this.city + this.postcode});
      }
      if (this.region !== "" && this.country !== "") {
        this.address.push({line: this.region + ", " + this.country});
      } else {
        this.address.push({line: this.region + this.country});
      }

      // businesses administered unpack
      this.actingBusinessId = Cookies.get("actAs");
      data.businessesAdministered.forEach(business => {
        if (business !== null) {
          if (business.id == this.actingBusinessId) {
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

      this.getCreatedDate(data.created);
    },

    /**
     * push user to an business profile page
     */
    pushToBusiness(id) {//TODO:change name
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
      Cookies.remove('userID');
      Cookies.remove('actAs');
      Api.signOut().then(() => {
        this.$router.push({ name: 'Login' })
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

        await Api.makeAdministrator(Cookies.get("actAs"), this.urlID).then(response => {
              if (response.status !== 200) {
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
        });
        //add the business
        Api.getBusiness(this.actingBusinessId).then(response => {
          this.businessesAdministered.push({name: response.data.name, id: response.data.id});
        })
        this.isBusinessAdministrator = true;
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

        await Api.removeAdministrator(Cookies.get("actAs"), this.urlID).then(response => {
              if (response.status !== 200) {
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
        });
        //pop the business which has been removed
        Api.getBusiness(this.actingBusinessId).then(response => {
          const newBusinessesAdministered = [];
          this.businessesAdministered.forEach(business => {
            if (business.id != response.data.id) {
              newBusinessesAdministered.push({name: response.data.name, id: response.data.id});
            }
          })
          this.businessesAdministered = newBusinessesAdministered;
        })
        this.isBusinessAdministrator = false;
        this.loadingAction = false;
      }
    },
    showFileUpload(x) {
      this.showUpload = x;
    },
  },

  /**
   * When mounted, initiate population of page.
   * If cookies are invalid or not present, redirect to login page.
   */
  mounted() {

    const currentID = Cookies.get('userID');
    this.getLoginRole(currentID);

    if (currentID) {

      const url = document.URL
      this.urlID = url.substring(url.lastIndexOf('/') + 1);

      if (currentID === this.urlID || this.urlID === 'profile') {
        this.retrieveUser(currentID);
      } else {
        // Another user
        this.retrieveUser(this.urlID);
        this.otherUser = true;
      }

    } else {
      this.$router.push({name: 'Login'});
    }
  }
}
</script>

<!----------------------------------------------- Profile Page Styling ------------------------------------------------>

<style scoped>

#upload-button {
  margin: 5px 0;
}

.modal {
  display: block; /* Hidden by default */
  position: fixed; /* Stay in place */
  z-index: 1; /* Sit on top */
  width: 100%;
  overflow: auto; /* Enable scroll if needed */
  background-color: rgb(0,0,0); /* Fallback color */
  background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}
/* Modal Content/Box */
.modal-content {
  background-color: #fefefe;
  margin: 15% auto; /* 15% from the top and centered */
  padding: 20px;
  border: 1px solid #888;
  width: 60%; /* Could be more or less, depending on screen size */
}

#profile-container {
  margin-bottom: 5%;
}

#imageDiv {
  width:100%;
  padding: 2px;
}

.businesses-administered:hover {
  color: #1EBA8C !important;
  cursor: pointer;
}
</style>
