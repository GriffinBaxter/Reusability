<template>
  <div>
    <div class="container py-5">

      <!-- First Row -->
      <h2 class="font-weight-bold mb-2">{{ section }}</h2>

      <!---------------------------------------------- ordering menu ------------------------------------------------>

      <div id="ordering-options-menu-container">
        <OrderingOptionsMenu @new-card-created="(e) => $emit('new-card-created', e)" v-bind:type="section"></OrderingOptionsMenu>
      </div>

      <!------------------------------------------------ cards ------------------------------------------------------>

      <div class="row pb-5 mb-4" v-if="hasDataLoaded">
        <!-- Card-->
        <div class="col-md-6 col-xl-4 mb-4 mb-lg-0"
             style="padding: 12px"
             v-for="card in sectionCards"
             v-bind:key="card.index">
          <div type="button"
               @click="selectACard(card.id)"
               data-bs-toggle="modal"
               data-bs-target="#cardDetailPopUp">
            <Card v-bind:index="card.index"
                  v-bind:title="card.title"
                  v-bind:description="card.description"
                  v-bind:created="styleDate(card.created)"
                  v-bind:expires="styleDate(card.displayPeriodEnd)"
                  v-bind:creator="card.creator"
                  v-bind:creatorImage="getPrimaryImageSrc(card.creator.images)"
                  v-bind:address="combineSuburbAndCity(card.creator.homeAddress.suburb, card.creator.homeAddress.city)"
            />
          </div>
        </div>
      </div>
      <div v-else>
        <LoadingDots></LoadingDots>
      </div>

      <!---------------------------------------------- page buttons ------------------------------------------------>

      <div id="page-button-container">
        <PageButtons
          v-bind:totalPages="totalPages"
          v-bind:currentPage="page"/>
      </div>

    </div>
  </div>

</template>

<script>
import Card from "./Card";
import OrderingOptionsMenu from "./OrderingOptionsMenu";
import PageButtons from "../PageButtons";
import {formatDate} from "../../dateUtils";
import LoadingDots from "../LoadingDots"
import Api from "../../Api";

export default {
  name: "MarketplaceTabSection",
  data() {
    return {
      cards: [],
      selectedCard: 0
    }
  },
  props: {
    hasDataLoaded: {
      type: Boolean,
      required: true
    },
    section: {
      type: String,
      default: "For Sale",
      required: true
    },
    sectionCards: {
      type: Array,
      required: true,
      default() { return []; }
    },
    totalPages: {
      type: Number,
      default: 1,
      required: true
    },
    page: {
      type: Number,
      default: 0,
      required: true
    }
  },
  components: {
    PageButtons,
    OrderingOptionsMenu,
    Card,
    LoadingDots
  },
  methods: {
    selectACard(index) {
      this.$emit('openCardDetail', index);
      this.selectedCard = index;
    },
    styleDate(date){
      return formatDate(date, false);
    },
    combineSuburbAndCity(suburb, city) {
      return (suburb === null) ? city : suburb + ", " + city;
    },
    getPrimaryImageSrc(images) {
      if (images.length > 0) {
        for (let image of images) {
          if (image.isPrimary) {
            return Api.getServerURL() + "/" + image.thumbnailFilename;
          }
        }
      }
      return require('../../../public/default-image.jpg')
    },
  },
}
</script>

<style scoped>

.spinner-grow {
  height: 14px;
  width: 14px;
  margin-right: 4px;
  margin-left: 4px;
}

#ordering-options-menu-container {
  margin-top: 4%;
  margin-bottom: 4%;
}

</style>
