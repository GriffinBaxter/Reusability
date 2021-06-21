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
          <MarketplaceTabSection @openCardDetail="openCardDetail"
                                 @orderedCards="orderedCards"
                                 @updatePage="updatePage"
                                 :sendData="selectedCard"
                                 v-bind:section="'For Sale'"
                                 v-bind:sectionCards="allCards.ForSale"
                                 @new-card-created="e => allCards.ForSale.unshift(e)"

                                 v-bind:totalPages="totalPages"
                                 v-bind:page="page"
          />
        </div>
        <div class="tab-pane fade" id="wanted" role="tabpanel" aria-labelledby="wanted-tab">
          <MarketplaceTabSection @openCardDetail="openCardDetail"
                                 @orderedCards="orderedCards"
                                 @updatePage="updatePage"
                                 :sendData="selectedCard"
                                 v-bind:section="'Wanted'"
                                 v-bind:sectionCards="allCards.Wanted"
                                 @new-card-created="e => allCards.Wanted.unshift(e)"
                                 v-bind:totalPages="totalPages"
                                 v-bind:page="page"
          />
        </div>
        <div class="tab-pane fade" id="exchange" role="tabpanel" aria-labelledby="exchange-tab">
          <MarketplaceTabSection @openCardDetail="openCardDetail"
                                 @orderedCards="orderedCards"
                                 @updatePage="updatePage"
                                 :sendData="selectedCard"
                                 v-bind:section="'Exchange'"
                                 v-bind:sectionCards="allCards.Exchange"
                                 @new-card-created="e => allCards.Exchange.unshift(e)"
                                 v-bind:totalPages="totalPages"
                                 v-bind:page="page"
          />
        </div>
      </div>

    </div>
    </div>
    <!--Footer contains links that are the same as those in the nav bar-->
    <Footer></Footer>

  </div>

</template>

<script>

import CardDetail from "../components/marketplace/CardDetailPopup";
import Footer from '../components/main/Footer';
import Navbar from '../components/main/Navbar';
import MarketplaceTabSection from "../components/marketplace/MarketplaceTabSection";
import Api from "../Api";

export default {
  name: "Marketplace",
  data() {
    return {
      selectSection: "For Sale",
      selectedCard: 0,
      allCards: {
        ForSale: [],
        Wanted: [],
        Exchange: [],
      },
      sortBy: "createdDESC",
      forSaleSortBy: "createdDESC",
      wantedSortBy: "createdDESC",
      exchangeSortBy: "createdDESC",
      page: 0,
      forSalePage: 0,
      wantedPage: 0,
      exchangePage: 0,
      totalPages: 1
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
      this.selectSection = newSection;
      switch (this.selectSection) {
        case "For Sale":
          this.sortBy = this.forSaleSortBy;
          this.page = this.forSalePage;
          break;
        case "Wanted":
          this.sortBy = this.wantedSortBy;
          this.page = this.wantedPage;
          break;
        case "Exchange":
          this.sortBy = this.exchangeSortBy;
          this.page = this.exchangePage;
          break;
      }
      this.updateUrl();
      this.retrieveAllCardsForSection(this.selectSection.replace(" ", ""));
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
      // Getting query params from the route
      this.sortBy = this.$route.query["orderBy"] || "createdDESC";
      this.page = parseInt(this.$route.query["page"]) - 1 || 0;

      switch (section) {
        case "ForSale":
          this.forSaleSortBy = this.sortBy;
          this.forSalePage = this.page;
          break;
        case "Wanted":
          this.wantedSortBy = this.sortBy;
          this.wantedPage = this.page;
          break;
        case "Exchange":
          this.exchangeSortBy = this.sortBy;
          this.exchangePage = this.page;
          break;
      }

      if (this.page < 0) {
        this.$router.push({path: '/pageDoesNotExist'});
      }
      
      Api.getAllCards(section, this.sortBy, this.page).then(response => {
        this.allCards[section] = response.data;
        this.totalPages = parseInt(response.headers["total-pages"]);

        if (this.totalPages > 0 && this.page > this.totalPages) {
          this.$router.push({path: '/pageDoesNotExist'});
        }
      }).catch((error) => {
        if (error.response.status === 400) {
          this.$router.push({path: '/pageDoesNotExist'});
        }
        console.log(error.message)
      })
    },

    orderedCards(orderByValue) {
      switch (this.selectSection) {
        case "For Sale":
          this.forSaleSortBy = orderByValue;
          break;
        case "Wanted":
          this.wantedSortBy = orderByValue;
          break;
        case "Exchange":
          this.exchangeSortBy = orderByValue;
          break;
      }
      this.sortBy = orderByValue;
      this.updateUrl();
      this.retrieveAllCardsForSection(this.selectSection.replace(" ", ""));
    },

    /**
     * Updates the display to show the new page when a user clicks to move to a different page.
     */
    updatePage(newPageNumber) {
      switch (this.selectSection) {
        case "For Sale":
          this.forSalePage = newPageNumber;
          break;
        case "Wanted":
          this.wantedPage = newPageNumber;
          break;
        case "Exchange":
          this.exchangePage = newPageNumber;
          break;
      }
      this.page = newPageNumber;
      this.updateUrl();
      this.retrieveAllCardsForSection(this.selectSection.replace(" ", ""));
    },

    /**
     * Updates the URL to match the updated section, sortBy, or page.
     */
    updateUrl() {
      this.$router.push({
        path: `/marketplace`,
        query: {"section": this.selectSection.replace(" ", ""), "orderBy": this.sortBy, "page": (this.page + 1).toString()}
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

#create-card-button {
  float: right;
}

</style>

