<template>
  <div>
    <div class="container py-5">

      <!-- First Row -->
      <h2 class="font-weight-bold mb-2">{{ section }}</h2>
      <p class="font-italic text-muted mb-4">Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
        tempor incididunt.</p>

      <!---------------------------------------------- ordering menu ------------------------------------------------>

      <div id="ordering-options-menu-container">
        <OrderingOptionsMenu @new-card-created="(e) => $emit('new-card-created', e)"></OrderingOptionsMenu>
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

    </div>
  </div>

</template>

<script>
import Card from "@/components/marketplace/Card";
import OrderingOptionsMenu from "./OrderingOptionsMenu";

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
    }
  },
  components: {
    OrderingOptionsMenu,
    Card,
  },
  methods: {
    selectACard(index) {
      this.$emit('openCardDetail', index);
      this.selectedCard = index
    },
    testing(e) {
      console.log(e)
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
