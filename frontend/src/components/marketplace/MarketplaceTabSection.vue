<template>
  <div>
    <div class="container py-5">

      <!-- First Row -->
      <h2 class="font-weight-bold mb-2">{{ section }}</h2>
      <p class="font-italic text-muted mb-4">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
        tempor incididunt.</p>

      <!---------------------------------------------- ordering menu ------------------------------------------------>

      <div id="ordering-options-menu-container">
        <OrderingOptionsMenu></OrderingOptionsMenu>
      </div>

      <!------------------------------------------------ cards ------------------------------------------------------>

      <div class="row pb-5 mb-4">
        <!-- Card-->
        <div class="col-lg-3 col-md-6 mb-4 mb-lg-0"
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
                  v-bind:created="card.created"
                  v-bind:creator="card.creator"
            />
          </div>
        </div>
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

export default {
  name: "MarketplaceTabSection",
  data() {
    return {
      cards: [],
      selectedCard: 0
    }
  },
  props: {
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
  },
  methods: {
    selectACard(index) {
      this.$emit('openCardDetail', index);
      this.selectedCard = index
    }
  },
}
</script>

<style scoped>

#ordering-options-menu-container {
  margin-top: 4%;
  margin-bottom: 4%;
}

</style>
