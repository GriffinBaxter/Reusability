<template>
  <div>
    <ProfileHeader/>
    <div class="container p-5 mt-3" id="profileContainer">
      <div class="row">
        <div class="col-xl-3 mb-3">
          <div class="card text-center shadow-sm">
            <div class="card-body">
              <div></div>
                <img class="rounded-circle img-fluid" src="../../public/sample_profile_image.jpg" alt="Profile Image"/>
              <div class="mt-3">
                <h4>{{nickname}}</h4>
              </div>
            </div>
          </div>
          <!--   For later use:   -->
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
                  <div class="text-secondary">
                    {{homeAddress}}
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
import ProfileHeader from "@/components/ProfileHeader";
import Api from '../Api';
import Cookies from 'js-cookie';
import Footer from "@/components/Footer";

export default {
  name: "Profile",
  components: {
    Footer,
    ProfileHeader,
  },
  data() {
    return {
      firstName: "",
      lastName: "",
      middleName: "",
      nickname: "",
      bio: "",
      email: "",
      dateOfBirth: "",
      phoneNumber: "",
      homeAddress: "",
      created: "",
      joined: "",
      otherUser: false,
    }
  },
  methods: {
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
      if (this.otherUser) {
        document.getElementById('phoneRow').remove();
        document.getElementById('dateOfBirthRow').remove();
        document.getElementById('phoneHR').remove();
        document.getElementById('dateHR').remove();

        let address = data.homeAddress.split(';');
        address = address.slice(2, address.length);
        address = address.join(", ");
        this.homeAddress = address;

      } else {
        this.dateOfBirth = this.formatAge(data.dateOfBirth);
        this.phoneNumber = data.phoneNumber;
        this.homeAddress = data.homeAddress.replaceAll(";", ", ");
      }

      this.firstName = data.firstName;
      this.middleName = data.middleName;
      this.lastName = data.lastName;
      this.nickname = data.nickname;
      this.bio = data.bio;
      this.email = data.email;



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
      const urlID = url.substring(url.lastIndexOf('/') + 1);
      if (currentID === urlID || urlID === 'profile') {
        this.retrieveUser(currentID);
      } else {
        // Another user
        this.retrieveUser(urlID);
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
