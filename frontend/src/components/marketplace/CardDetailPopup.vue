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

                <!--description-->
                <p class="card-text">
                  {{ description }}
                </p>

                <br>

                <!--keywords-->
                <p class="btn btn-outline-secondary"
                   v-for="keyword in keywords"
                   v-bind:key="keyword.id"
                   style="padding: 0px 3px; margin: 3px 5px">
                  # {{ keyword.name }}
                </p>
                <hr style="margin: 16px 0px 12px">

                <!--creator info-->
                <div class="row">
                  <div class="col">
                    <h6 class="text-muted">
                      Created: {{ created }}
                    </h6>
                  </div>
                  <div class="col" style="float:right; text-align: right">
                    <h6 class="text-muted">
                      {{ address }}
                    </h6>
                  </div>
                </div>

                <hr style="margin: 5px 0px 10px">

                <!--user's detail-->
                <div style="vertical-align:middle; font-size:15px;">
                  <img :src="avatar" class="rounded-circle" id="avatar-image" alt="User Avatar"/>
                  <a v-bind:title="creator" style="font-size: 17px"> {{ displayCreator }} </a>
                  <button v-if="deletePermissionCheck()" class="btn btn-outline-success"
                          style="float:right"
                          @click="openEdit"
                          data-bs-dismiss="modal">Edit</button>
                  <button v-if="deletePermissionCheck()"
                          id="remove-card-button"
                          type="button"
                          class="btn btn-outline-danger"
                          style="float:right"
                          @click="removeCurrentCard()"
                          data-bs-dismiss="modal"
                          aria-label="Close" >Remove</button>
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
import Api from "../../Api";
import {formatDate} from "../../dateUtils";
import Cookies from 'js-cookie';

export default {
  name: "CardDetail",
  data() {
    return {
      avatar: require("../../../public/sample_profile_image.jpg"),
      section: "",
      title: "",
      description: "",
      keywords: [],
      created: "",
      creator: "",
      displayCreator: "",
      address: "",
      currentUserRole: "",
      creatorId: null
    }
  },
  props: {
    id: {
      type: Number,
      default: 0,
      required: true
    }
  },
  methods: {
    /**
     * convert section with space
     */
    convertSection(section) {
      switch (section) {
        case 'FORSALE':
          return "For Sale";
        case 'WANTED':
          return "Wanted";
        case 'EXCHANGE':
          return "Exchange";
      }
    },
    /**
     * populate data from back end
     */
    populateData(data) {
      this.section = this.convertSection(data.section);
      this.title = data.title;
      this.description = data.description;
      this.created = formatDate(data.created);
      this.address = [data.creator.homeAddress.suburb, data.creator.homeAddress.city].join(" ");
      this.creator = [data.creator.firstName, data.creator.middleName, data.creator.lastName].join(" ");
      this.creatorId = data.creator.id;
      if (this.creator.length >= 40) {
        this.displayCreator = this.creator.slice(0, 40) + '...';
      } else {
        this.displayCreator = this.creator;
      }
      this.keywords = [];
      data.keywords.forEach(keyword => {
        this.keywords.push({id: keyword.id, name: keyword.name});
      })
    },
    /**
     * retrieve card detail by given id
     */
    retrieveCardDetail(id) {
      Api.getDetailForACard(id).then(response => (this.populateData(response.data))).catch((error) => {
        if (error.require && !error.response) {
          this.$router.push({path: '/timeout'});
        } else if (error.response.status === 400) {
          this.$router.push({path: '/pageDoesNotExist'});
        } else if (error.response.status === 401) {
          this.$router.push({path: '/invalidtoken'});
        } else if (error.response.status === 406) {
          this.$router.push({path: '/noCard'});
        } else {
          this.$router.push({path: '/noCard'});
          console.log(error.message);
        }
      })
    },
    /**
     * remove current card
     */
    removeCurrentCard() {
      Api.deleteACard(this.id).then(
          response => {
            if (response.status === 200) {
              this.$router.go(this);
            }
          }
      ).catch((error) => {
        console.log(error)
      })
    },
    /**
     * check current user's permission of deletion
     * @returns {boolean} permission of deletion
     */
    deletePermissionCheck() {
      let flag;
      let currentUserId = Cookies.get('userID');
      if (currentUserId == this.creatorId){
        flag =  true;
      } else {
        Api.getUser(currentUserId).then(response => {
          this.currentUserRole = response.data.role;
        }).catch((error) => {
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
        flag = (this.currentUserRole !== "USER")
      }
      return flag;
    },
    /**
     * Opens the edit modal
     */
    openEdit() {
      this.$parent.$refs.editCardModal.showModal(this.id)
    }
  },
  /**
   * watch the id change of selected card
   */
  watch: {
    id: {
      deep: true,
      handler(newVal) {
        if (newVal !== 0) {
          this.retrieveCardDetail(newVal)
        }
      }
    }
  }
}
</script>

<style scoped>
#avatar-image {
  width: 30px;
}
</style>