<template>

  <div>

    <div id="main">
    <!------------------------- Nav bar; displays either business account or individual account nav bar --------------->
    <Navbar></Navbar>

    <CardDetail v-bind:id="selectedCard"
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
          <MarketplaceTabSection @openCardDetail="openCardDetail" :sendData="selectedCard" v-bind:section="'For Sale'"/>
        </div>
        <div class="tab-pane fade" id="wanted" role="tabpanel" aria-labelledby="wanted-tab">
          <MarketplaceTabSection @openCardDetail="openCardDetail" :sendData="selectedCard" v-bind:section="'Wanted'"/>
        </div>
        <div class="tab-pane fade" id="exchange" role="tabpanel" aria-labelledby="exchange-tab">
          <MarketplaceTabSection @openCardDetail="openCardDetail" :sendData="selectedCard" v-bind:section="'Exchange'"/>
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
import MarketplaceTabSection from "@/components/marketplace/MarketplaceTabSection";
import Api from "@/Api";

export default {
  name: "Marketplace",
  data() {
    return {
      selectSection: "ForSale",
      selectedCard: 0,
      allCards: {
        ForSale: [],
        Wanted: [],
        Exchange: [],
      },
    }
  },
  components: {
    MarketplaceTabSection,
    CardDetail,
    Footer,
    Navbar,
  },
  methods: {

    /**
     * Change the current tab that the user is viewing in the marketplace.
     * @param newSection the section that the user is switching to
     */
    changeSection(newSection) {
      this.selectSection = newSection
    },

    /**
     * Sets the card to open
     * @param selectedCard the card that will be opened.
     */
    openCardDetail(selectedCard) {
      this.selectedCard = selectedCard
    },

    /**
     * Retrieve the cards for the given marketplace section.
     * The allowed sections are "ForSale", "Wanted" and "Exchange".
     * @param section the section that cards will be retrieved for.
     */
    retrieveAllCardsForSection(section) {
      Api.getAllCards(section).then(response => {
        this.allCards[section] = response.data;
      }).catch((error) => {
        console.log(error.message)
      })
    }
  },

  /**
   * When mounted, if cookie is not present, redirect to login page.
   */
  mounted() {
    this.retrieveAllCardsForSection("ForSale");
    this.retrieveAllCardsForSection("Wanted");
    this.retrieveAllCardsForSection("Exchange");
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

