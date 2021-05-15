<template>
  <div>
    <!-- Modal -->
    <div class="modal fade" id="cardDetailPopUp" tabindex="-1" aria-labelledby="cardDetailPopUpLabel"
         aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable modal-lg">
        <div class="modal-content">

          <!--section-->
          <div class="modal-header" style="padding: 20px 40px 15px">
            <h2 id="cardDetailPopUpLabel" style="margin: 0px">{{ section }}</h2>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>

          <div class="modal-body" style="padding: 15px 25px 30px">
            <div class="card">
              <div class="card-body">

                <!--title-->
                <h4 class="card-subtitle mb-2">{{ title }}</h4>

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
                      {{ city }}
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
export default {
  name: "CardDetail",
  data() {
    return {
      avatar: require("../../../public/sample_profile_image.jpg"),
      section: "ForSale",
      title: "1982 Lada Samara",
      description: "Beige, suitable for a hen house. Fair condition. Some rust. As is, where is. Will swap for budgerigar.",
      keyword: [],
      created: "2021-07-15T05:10:00Z",
      firstName: "John",
      lastName: "Smith",
      middleName: "Hector",
      creator: "",
      city: "Christchurch",
    }
  },
  mounted() {
    this.creator = this.firstName + " " + this.middleName + " " + this.lastName
  }
}
</script>

<style scoped>
#avatar-image {
  width: 30px;
}
</style>