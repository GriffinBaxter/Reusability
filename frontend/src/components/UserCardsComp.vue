<template>
  <div>

  <!-- a placeholder so that when you click on a user's card on their profile it will open the more detailed display -->
  <CardDetail v-bind:id="selectedCard"
              v-bind:section="cardSection"/>

  <!-- Edit Modal for editing your own cards -->
  <EditCardModal ref="editCardModal"></EditCardModal>

    <div class="col" v-if="userHasCards()">
      <div class="row" id="user-cards">
        <div v-if="showTitle">
          <h4 v-if="otherUser">User's Cards</h4>
          <h4 v-else>My Cards</h4>
        </div>
        <!-- Card-->
        <div class="col-md-6 col-xl-4 mb-4 mb-lg-0"
             style="padding: 12px"
             v-for="card in usersCards"
             v-bind:key="card.index">
          <div type="button"
               @click="selectACard(card.id, card.section)"
               data-bs-toggle="modal"
               data-bs-target="#cardDetailPopUp">
            <Card v-bind:index="card.index"
                  v-bind:title="card.title"
                  v-bind:description="card.description"
                  v-bind:created="styleDate(card.created)"
                  v-bind:expires="styleDate(card.displayPeriodEnd)"
                  v-bind:creator="card.creator"
                  v-bind:address="combineSuburbAndCity(card.creator.homeAddress.suburb, card.creator.homeAddress.city)"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Card from "./marketplace/Card"
import {formatDate} from "../dateUtils";
import CardDetail from "./marketplace/CardDetailPopup";
import EditCardModal from "./marketplace/EditCardModal";

export default {
  name: "UserCardsComp",
  props: {
    usersCards: {
      type: Array,
      required: true
    },
    otherUser: {
      type: Boolean,
      required: false,
      default: false
    },
    showTitle: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  components: {
    Card,
    CardDetail,
    EditCardModal,
  },
  data() {return {
    selectedCard: 0,
    cardSection: "For Sale",
  }},
  methods: {
    /**
     * If a user has cards then they will need to be displayed.
     * @return boolean true if the user has cards (i.e the length of usersCards !== 0), otherwise false.
     */
    userHasCards() {
      return this.usersCards.length !== 0;
    },
    /**
     * If a card is selected then a custom event is emitted so the CardDetailPopup knows to open and display the
     * information for the selected card.
     * @param index the index of the selected card.
     * @param section the section the selected card appears in.
     */
    selectACard(index, section) {
      this.$emit('openCardDetail', index);
      this.selectedCard = index;
      this.cardSection = section;
    },
    /**
     * Format the date of a card using the date-fns library.
     * @param date the date the card was created
     * @return date a date formatted using the date-fns library.
     */
    styleDate(date) {
      return formatDate(date, false);
    },
    /**
     * Concat the suburb and city together to then be displayed on a user's card.
     * @param suburb the suburb the creator of the card lives in.
     * @param city the city the creator of the card lives in.
     * @return String a concatenation of the suburb and city.
     */
    combineSuburbAndCity(suburb, city) {
      return (suburb === null) ? city : suburb + ", " + city;
    }
  }
}
</script>

<style scoped>

</style>