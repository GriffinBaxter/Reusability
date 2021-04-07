<template>
  <div>
    <ProfileHeader/>
    <div class="container p-5 mt-3" id="profileContainer">

      <div class="row">
        <div class="col-xl-3 mb-3">
          <div class="card text-center shadow-sm">
            <div class="card-body">
              <img class="rounded-circle img-fluid" src="../../public/sample_profile_image.jpg" alt="Profile Image"/>
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
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Name:</h6>
                  </div>
                  <div class="col-8">
                    <h6 align="right">{{name}}</h6>
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
                    <h6 align="right">{{businessType}}</h6>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Created Time:</h6>
                  </div>
                  <div class="col-8 -align-right">
                    <h6 align="right">{{created}}</h6>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Address:</h6>
                  </div>
                  <div class="col-8">
                    <div class="row">
                      <h6 align="right">{{streetNumber}} {{streetName}}</h6>
                      <h6 align="right">{{city}}, {{postcode}}</h6>
                      <h6 align="right">{{region}}, {{country}}</h6>
                    </div>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Description:</h6>
                  </div>
                  <div class="col-8">
                    <h6 align="right">{{description}}</h6>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Primary Administrator:</h6>
                  </div>
                  <div class="col-8">
                    <h6 align="right">{{primaryAdministrator}}</h6>
                  </div>
                </div>
              </div>
              <hr>
              <div class="container">
                <div class="row justify-content-between">
                  <div class="col-4 -align-left">
                    <h6>Administrators:</h6>
                  </div>
                  <div class="col-8">
                    <h6 align="right">{{administrators}}</h6>
                  </div>
                </div>
              </div>
              <hr>
            </div>
          </div>

          <div align="right">
            <button class="btn btn-outline-primary float-end mt-4" id="editProfileButton">Edit Profile</button>
          </div>

        </div>

      </div>

    </div>



    <Footer></Footer>
  </div>
</template>

<script>
import ProfileHeader from "@/components/ProfileHeader";
import Footer from "@/components/Footer";

export default {
  name: "Profile",
  components: {
    Footer,
    ProfileHeader,
  },
  data() {
    return{
      name: "Lumbridge General Store",
      description: "A one-stop shop for all your adventuring needs",
      streetNumber: "3/24",
      streetName: "Ilam Road",
      city: "Christchurch",
      region: "Canterbury",
      country: "New Zealand",
      postcode: "90210",
      businessType: "Accommodation and Food Services",
      created: "2020-07-14T14:52:00Z",
      primaryAdministrator: "exmaple",
      administrators: "some account"
    }
  },

}
</script>

<style>

</style>
