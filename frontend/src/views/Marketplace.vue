<template>

  <div>

    <div id="main">
    <!------------------------- Nav bar; displays either business account or individual account nav bar --------------->
    <Navbar></Navbar>

    <CardDetail v-bind:index="selectedCard"
                v-bind:section="selectSection"/>

    <div id="marketplace-container">

      <!------------------------------------------------ marketplace tabs---------------------------------------------->

      <ul class="nav nav-tabs" id="marketplace-tabs" role="tablist">
        <li class="nav-item" role="presentation">
          <button class="nav-link active" id="for-sale-tab" data-bs-toggle="tab" data-bs-target="#for-sale"
                  type="button" role="tab" aria-controls="for-sale" aria-selected="true"
                  @click="changeSection('For Sale')">
            For Sale
          </button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="wanted-tab" data-bs-toggle="tab" data-bs-target="#wanted" type="button"
                  role="tab" aria-controls="wanted" aria-selected="false"
                  @click="changeSection('Wanted')">
            Wanted
          </button>
        </li>
        <li class="nav-item" role="presentation">
          <button class="nav-link" id="exchange-tab" data-bs-toggle="tab" data-bs-target="#exchange" type="button"
                  role="tab" aria-controls="exchange" aria-selected="false"
                  @click="changeSection('Exchange')">
            Exchange
          </button>
        </li>
      </ul>
      <div class="tab-content" id="marketplace-tabs-content">
        <div class="tab-pane fade show active" id="for-sale" role="tabpanel" aria-labelledby="for-sale-tab">
          <ForSale @openCardDetail="openCardDetail" :sendData="selectedCard"/>
        </div>
        <div class="tab-pane fade" id="wanted" role="tabpanel" aria-labelledby="wanted-tab">
          <Wanted @openCardDetail="openCardDetail" :sendData="selectedCard"/>
        </div>
        <div class="tab-pane fade" id="exchange" role="tabpanel" aria-labelledby="exchange-tab">
          <Exchange @openCardDetail="openCardDetail" :sendData="selectedCard"/>
        </div>
      </div>

    </div>
    </div>
    <!--Footer contains links that are the same as those in the nav bar-->
    <Footer></Footer>

  </div>

</template>

<script>

import CardDetail from "@/components/marketplace/CardDetailPopup";
import Footer from '../components/main/Footer';
import Navbar from '../components/main/Navbar';
import ForSale from '../components/marketplace/ForSale';
import Wanted from '../components/marketplace/Wanted';
import Exchange from '../components/marketplace/Exchange';

export default {
  name: "Marketplace",
  data() {
    return {
      selectSection: "ForSale",
      selectedCard: 0
    }
  },
  components: {
    CardDetail,
    Footer,
    Navbar,
    ForSale,
    Wanted,
    Exchange
  },
  methods: {
    changeSection(newSection) {
      this.selectSection = newSection
    },
    openCardDetail(selectedCard) {
      this.selectedCard = selectedCard
    }
  },

  /**
   * When mounted, if cookie is not present, redirect to login page.
   */
  mounted() {

  },
}
</script>

<style scoped>

#marketplace-container {
  margin: 4% 10% 10% 10%;
}

#for-sale-tab, #wanted-tab, #exchange-tab {
  font-family: 'Roboto', sans-serif;

}

</style>

