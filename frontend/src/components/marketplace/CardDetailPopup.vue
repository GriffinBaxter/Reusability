<template>
  <div>
    <!-- Modal -->
    <div class="modal fade" id="cardDetailPopUp" tabindex="-1" aria-labelledby="cardDetailPopUpLabel"
         aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable modal-lg">
        <div class="modal-content">

          <!--section-->
          <div class="modal-header" style="padding: 20px 40px 15px">
            <h2 id="cardDetailPopUpLabel" style="margin: 0px">
              {{ section }}
            </h2>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>

          <div class="modal-body" style="padding: 15px 25px 30px">
            <div class="card">
              <div class="card-body">

                <!--title-->
                <h4 class="card-subtitle mb-2">{{ title }}</h4>
                <!--show link work-->{{ id }}<!--delete after data populate-->

                <!--description-->
                <p class="card-text">
                  {{ description }}
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  <br>
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  <br>
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  <br>
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  <br>
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  <br>
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                  Some quick example text to build on the card title and make up the bulk of the card's
                  content.
                </p>

                <hr style="margin: 16px 0px 12px">

                <!--creator info-->
                <div class="row">
                  <div class="col">
                    <h6 class="text-muted">
                      Created: {{ created }}
                    </h6>
                  </div>
                  <div class="col" align="right">
                    <h6 class="text-muted">
                      {{ address }}
                    </h6>
                  </div>
                </div>

                <hr style="margin: 5px 0px 10px">

                <!--user's detail-->
                <div style="vertical-align:middle; font-size:15px;">
                  <img :src="avatar" class="rounded-circle" id="avatar-image" alt="User Avatar"/>
                  <a style="font-size: 17px"> {{ creator }} </a>
                </div>

              </div>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Api from "@/Api";

export default {
  name: "CardDetail",
  data() {
    return {
      avatar: require("../../../public/sample_profile_image.jpg"),
      section: "",
      title: "",
      description: "",
      keyword: [],
      created: "",
      creator: "",
      address: "",
    }
  },
  props: {
    id: {
      type: Number,
      default: 0,
      required: true
    },
    // //
    // section: {
    //   type: String,
    //   default: null,
    //   required: true,
    // }
  },
  methods: {
    convertSection(section) {
      switch (section) {
        case 'ForSale':
          return "For Sale";
        case 'Wanted':
          return "Wanted";
        case 'Exchange':
          return "Exchange";
      }
    },
  /**
   * populate data from back end
   */
  populateData(data){
    this.section = this.convertSection(data.section);
    this.title = data.title;
    this.description = data.description;
    this.created = data.created; //TODO: length Error
    this.address = [data.creator.homeAddress.suburb, data.creator.homeAddress.city].join(" ");
    this.creator = [data.creator.firstName, data.creator.middleName, data.creator.lastName].join(" ")
  },
  retrieveCardDetail(id) {
    Api.retrieveCardDetail(id).then(response => (this.populateData(response.data))).cache((error) => {
      if (error.require && !error.response) {
        this.$router.push({path: '/timeout'});
      } else if (error.response.status === 400) {
        this.$router.push({path: '/pageDoesNotExist'}); //TODO: add error page
      } else if (error.response.status === 401) {
        this.$router.push({path: '/invalidtoken'});
      } else if (error.response.status === 406) {
        this.$router.push({path: '/noCard'});
      } else {
        this.$router.push({path: '/noCard'});
        console.log(error.message);
      }
    })
  }
  },
  mounted() {
  },
  beforeMount() {

  }
}
</script>

<style scoped>
#avatar-image {
  width: 30px;
}
</style>