<template>
  <div>
    <ProfileHeader/>
    <div class="container p-5 mt-3" id="profileContainer">

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
                <img class="rounded-circle img-fluid" src="../../public/sample_profile_image.jpg" alt="Profile Image"/>
              <div class="mt-3">
                <h4>{{nickname}}</h4>
              </div>
            </div>
          </div>

          <div v-if="actionErrorMessage" class="card text-white bg-danger shadow-sm mt-3">
            <div class="card-header">Something went wrong with your action...</div>
            <div class="card-body">{{actionErrorMessage}}</div>
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
                <div class="spinner-border spinner-border-sm text-danger" v-if="loadingGaaAction"></div>
                <button type="button" class="btn btn-lg btn-outline-danger" v-else @click="revokeUserGAA">Revoke admin rights</button>
              </div>

              <div v-else>
                <div class="spinner-border spinner-border-sm text-success" v-if="loadingGaaAction"></div>
                <button type="button" class="btn btn-lg btn-outline-success" v-else @click="grantUserGAA">Grant admin rights</button>
              </div>
            </div>
          </div>

          <!--             For later use:-->
          <!--          <div class="card text-center shadow-sm mt-3">-->
          <!--            <div class="card-body">-->
          <!--              <button class="btn btn-lg text-secondary" id="editProfileButton">Edit Profile</button>-->
          <!--            </div>-->
          <!--          </div>-->

        </div>
        <div class="col">
          <div class="card shadow-sm">
            <div class="card-body">
              <div class="row">
                <div class="col-md-3">
                  <h6>Bio: </h6>
                </div>
                <div class="col">
                  <div class="text-secondary">
                    {{bio}}
                  </div>
                </div>
              </div>
              <hr>
              <div class="row">
                <div class="col-md-3">
                  <h6>Name:</h6>
                </div>
                <div class="col">
                  <div class="text-secondary">
                    {{firstName}} {{middleName}} {{lastName}}
                  </div>
                </div>
              </div>
              <hr>
              <div class="row">
                <div class="col-md-3">
                  <h6>Email:</h6>
                </div>
                <div class="col">
                  <div class="text-secondary">
                    {{email}}
                  </div>
                </div>
              </div>
              <hr>
              <div class="row" id="dateOfBirthRow">
                <div class="col-md-3">
                  <h6>Date of Birth:</h6>
                </div>
                <div class="col">
                  <div class="text-secondary">
                    {{dateOfBirth}}
                  </div>
                </div>
              </div>
              <hr id="dateHR">
              <div class="row" id="phoneRow">
                <div class="col-md-3">
                  <h6>Phone number:</h6>
                </div>
                <div class="col">
                  <div class="text-secondary">
                    {{phoneNumber}}
                  </div>
                </div>
              </div>
              <hr id="phoneHR">
              <div class="row">
                <div class="col-md-3">
                  <h6>Address:</h6>
                </div>
                <div class="col">
                  <div class="text-secondary" v-for="lines in address" :key="lines.line">
                    {{lines.line}}
                  </div>
                </div>
              </div>
              <hr>
              <div class="row">
                <div class="col-md-3">
                  <h6>Joined:</h6>
                </div>
                <div class="col">
                  <div class="text-secondary">
                    {{joined}}
                  </div>
                </div>
              </div>
            </div>
          </div>
          <button class="btn btn-outline-primary float-end mt-4" id="logoutButton" @click="logout()">Sign Out</button>
        </div>
      </div>
      <Footer></Footer>
    </div>
  </div>
</template>

<script>
import ProfileHeader from "../components/ProfileHeader";
import Api from '../Api';
import Cookies from 'js-cookie';
import Footer from "../components/Footer";
import {UserRole} from '../components/User'

export default {
  name: "Profile",
  components: {
    Footer,
    ProfileHeader,

  },
  data() {
    return {
      actionErrorMessage: "",
      loadingGaaAction: false,
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
      otherUser: false,
      role: null
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
    // --------------------------------------------------------------------------------------------------------------------
    getCreatedDate(createdDate) {
      /*
      Calculates the months between the given date and the current date, then formats the given date and months.
      Finally it sets the join date on the page to the formatted string.
       */

      const dateJoined = new Date(createdDate);

      const currentDate = new Date();
      let months = (currentDate.getFullYear() - dateJoined.getFullYear()) * 12
          + (currentDate.getMonth()-dateJoined.getMonth());

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
      if (this.loadingGaaAction) return;

      if (this.urlID == null) {
        this.actionErrorMessage = "Sorry, but something went wrong..."
        return
      }

      this.loadingGaaAction = true;

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


      this.loadingGaaAction = false;

    },
    /**
     * Performs the action that revokes GAA from the (page) user and handles all errors
     * specified in the API spec.
     */
    async revokeUserGAA() {

      // If the process is already running return.
      if (this.loadingGaaAction) return;

      if (this.urlID == null) {
        this.actionErrorMessage = "Sorry, but something went wrong..."
        return
      }

      this.loadingGaaAction = true;

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

      this.loadingGaaAction = false;

    },
    retrieveUser(userID) {
      /*
      Sends a get request to the backend, calling populatePage upon success with the returned data.
      If the request was unsuccessful, the page is not populated and appropriate error messages logged.
       */
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
    formatAge(ageString) {
      /*
      Formats the given age string using a Date object and removes the day from the result.
      Returns a formatted string.
       */
      let array = (new Date(ageString)).toDateString().split(" ");
      array.shift();
      return array.join(' ')
    },
    populatePage(data) {
      /*
      Populates all display fields on the profile page with the given data.
      The address is a special case as its components are stored semi-colon separated,
      so it must be 'unpacked' and formatted.
       */
      if (data.homeAddress.city) {
        this.city = data.homeAddress.city;
      }
      if (data.homeAddress.region) {
        this.region = data.homeAddress.region;
      }
      if (data.homeAddress.country) {
        this.country = data.homeAddress.country;
      }

      if (this.otherUser) {
        document.getElementById('phoneRow').remove();
        document.getElementById('dateOfBirthRow').remove();
        document.getElementById('phoneHR').remove();
        document.getElementById('dateHR').remove();

        if (this.city !== "") {
          this.address.push({line: this.city});
        }
        if (this.region !== "" && this.country !== ""){
          this.address.push({line: this.region + ", " + this.country});
        } else {
          this.address.push({line: this.region + this.country});
        }

      } else {
        this.dateOfBirth = this.formatAge(data.dateOfBirth);
        this.phoneNumber = data.phoneNumber;

        if (data.homeAddress.streetNumber) {
          this.streetNumber = data.homeAddress.streetNumber;
        }
        if (data.homeAddress.streetName) {
          this.streetName = data.homeAddress.streetName;
        }
        if (data.homeAddress.city) {
          this.city = data.homeAddress.city;
        }
        if (data.homeAddress.postcode) {
          this.postcode = data.homeAddress.postcode;
        }

        if (this.streetNumber !== "" && this.streetName !== ""){
          this.address.push({line: this.streetNumber + " " + this.streetName});
        } else {
          this.address.push({line: this.streetNumber + this.streetName});
        }
        if (this.city !== "" && this.postcode !== ""){
          this.address.push({line: this.city + ", " + this.postcode});
        } else {
          this.address.push({line: this.city + this.postcode});
        }
        if (this.region !== "" && this.country !== ""){
          this.address.push({line: this.region + ", " + this.country});
        } else {
          this.address.push({line: this.region + this.country});
        }

      }

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
    logout() {
      /*
      Logs the user out of the site by deleting the relevant cookies and redirecting to the login page.
       */
      Cookies.remove('userID');
      Cookies.remove('JSESSIONID');
      this.$router.push({name: 'Login'});
    }
  },
  mounted() {
    /*
    When mounted, initiate population of page.
    If cookies are invalid or not present, redirect to login page.
     */
    const currentID = Cookies.get('userID');
    // TODO Implement when we agree on a JSESSIONID spec with backend team
    // Cookies.get('JSESSIONID');
    const validJSESSIONID = true;
    if (currentID && validJSESSIONID) {

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

<style scoped>

</style>
