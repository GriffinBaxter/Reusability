<template>
  <div>
    <!--nav bar-->
    <Navbar></Navbar>

    <!--profile header, contains user search bar-->
    <div id="profile-header-div">
      <ProfileHeader/>
    </div>

    <div class="container p-5 mt-3" id="profileContainer">
      <div class="row">
        <div class="col-xl-3 mb-3">
          <div class="card text-center shadow-sm">
            <div class="card-body">
              <img class="rounded-circle img-fluid" :src="require('../../public/sample_profile_image.jpg')" alt="Profile Image"/>
              <div class="mt-3">
                <h5>{{name}}</h5>
                <div class="text-secondary">{{description}}</div>
              </div>
            </div>
            <!--            <div class="card text-center shadow-sm-3">-->
            <!--              <div class="card-body">-->
            <!--                <button class="btn btn-lg text-secondary" id="editProfileButton">Edit Profile</button>-->
            <!--              </div>-->
            <!--            </div>-->
          </div>
        </div>
        <div class="col">
          <div class="card shadow-sm">
            <div class="card-body">
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Name:</h6>
                  </div>
                  <div class="col-8">
                    <div class="text-secondary" align="right">{{name}}</div>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Business Type:</h6>
                  </div>
                  <div class="col-8">
                    <div class="text-secondary" align="right">{{businessType}}</div>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Created Time:</h6>
                  </div>
                  <div class="col-8">
                    <div class="text-secondary" align="right">{{created}}</div>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Address:</h6>
                  </div>
                  <div class="col-8" >
                    <div class="row">
                      <div class="text-secondary" v-for="lines in address" :key="lines.line" align="right">
                        {{lines.line}}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <hr id="primaryAdministratorHr">
              <div class="container" id="primaryAdministratorRow">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Primary Administrator:</h6>
                  </div>
                  <div class="col-8">
                    <div class="text-secondary" align="right" @click="pushToUser(primaryAdministratorId)">
                      {{primaryAdministrator}}
                    </div>
                  </div>
                </div>
              </div>
              <hr id="administratorsHr">
              <div class="container" id="administratorsRow">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Administrators:</h6>
                  </div>
                  <div class="col-8">
                    <div class="text-secondary" v-for="nameOfAdministrator in nameOfAdministrators" :key="nameOfAdministrator.name"
                         align="right" @click="pushToUser(nameOfAdministrator.id)">
                      {{nameOfAdministrator.name}}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div align="right" id="signOutRow">
            <button class="btn btn-outline-primary float-end mt-4" id="signOutButton" @click="logout()">Sign Out</button>
          </div>
        </div>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script>
import ProfileHeader from "@/components/ProfileHeader";
import Footer from "@/components/Footer";
import Navbar from "@/components/Navbar";
import Api from "@/Api";
import Cookies from 'js-cookie';

export default {
  name: "BusinessProfile",
  components: {
    Footer,
    ProfileHeader,
    Navbar
  },
  data() {
    return {
      name: "",
      description: "",
      businessType: "",
      created: "",
      primaryAdministrator: "",
      primaryAdministratorId: "",

      address: [],
      streetNumber: "",
      streetName: "",
      city: "",
      region: "",
      country: "",
      postcode: "",

      nameOfAdministrators: [],

      isAdministrator: false
    }
  },
  methods: {
    formatAge(ageString) {
      /*
      Formats the given age string using a Date object and removes the day from the result.
      Returns a formatted string.
       */
      let array = (new Date(ageString)).toDateString().split(" ");
      array.shift();
      return array.join(' ')
    },
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
      this.created = `${finalDate} (${months} months ago)`;
    },
    retrieveBusiness (businessID){
      /*
      try to send a request to backend, and use populatePage() function to unpack data returned,
      if fail, push to a page show the error.
      */
      Api.getBusiness(businessID).then(response => (this.populatePage(response.data))).catch((error) =>{
        if (error.request && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noBusiness'});
        } else {
          this.$router.push({path: '/noBusiness'});
          console.log(error.message);
        }
      })
    },
    populatePage(data) {
      /*
      unpack data
      */
      if (this.otherUser){
        document.getElementById('primaryAdministratorHr').remove();
        document.getElementById('primaryAdministratorRow').remove();
        document.getElementById('administratorsHr').remove();
        document.getElementById('administratorsRow').remove();
        document.getElementById('signOutRow').remove();
      } else {
        // administrators unpack
        this.primaryAdministratorId = data.primaryAdministratorId;
        data.administrators.forEach(anUser => {
          if (anUser.id === this.primaryAdministratorId){
            this.primaryAdministrator = anUser.firstName + " " + anUser.middleName + " " + anUser.lastName;
          }
          this.nameOfAdministrators.push({name: anUser.firstName + " " + anUser.middleName + " " + anUser.lastName, id: anUser.id})
        })
      }

      //basic data unpack
      this.name = data.name;
      this.description = data.description;
      this.businessType = data.businessType.replaceAll("_", " ");
      this.getCreatedDate(data.created);

      // address unpack
      this.streetNumber = data.address.streetNumber;
      this.streetName = data.address.streetName;
      this.city = data.address.city;
      this.region = data.address.region;
      this.country = data.address.country;
      this.postcode = data.address.postcode;
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
    },
    pushToUser(id){
      this.$router.push({name:'Profile', params: {id}});
    },
    logout(){
      /*
      Logs the user out of the site by deleting the relevant cookies and redirecting to the login page.
       */
      Cookies.remove('userID');
      Cookies.remove('JSESSIONID');
      this.$router.push({name: 'Login'});
    }
  },
  mounted() {
    const currentID = Cookies.get('userID');
    if (currentID) {
      const url = document.URL;
      const urlID = url.substring(url.lastIndexOf('/') + 1);

      this.nameOfAdministrators.forEach((name, id) =>{
        if (id === currentID) {
          this.isAdministrator = true;
        }
      })

      if (urlID === 'businessProfile') {
        this.$router.push({path: '/noBusiness'});
      } else {
        this.retrieveBusiness(urlID);
      }
    }
  }
}
</script>

<style scoped>

</style>
